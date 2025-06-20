import java.util.*;
import java.io.*;
import java.net.*;
class MainServer
{
	static volatile boolean active =true;
	static BufferedReader serverinput=new BufferedReader(new InputStreamReader(System.in));
	static Set<ClientIF> clients=Collections.synchronizedSet(new HashSet<>());
	public static void main(String[] args)
	{
		try
		(ServerSocket server=new ServerSocket(5000))
		{	
			System.out.println("Server is Started Running ");
			new Thread(()->{
				try
				{
					while(true)
					{
						if(serverinput.readLine().equalsIgnoreCase("exit"))
						{
							System.out.println("Server is Shutting Down ");
							active=false;
							server.close();	
							break;
						}
					}
				}
				catch(IOException e)
				{
					System.out.println("Could not close Server "+e.getMessage());
				}
			}).start();
		while(active)
		{
			try
			{
				Socket user=server.accept();
				ClientIF client=new ClientIF(user);
				clients.add(client);
				new Thread(client).start();
				System.out.println("Client is Connected");
			}
			catch(IOException e)
			{
				if(!active)
				{
					
					synchronized(clients)
					{
						for(ClientIF client:clients)
						{
							client.message(" Server is Shutting Down ");
							client.CloseConnection();
						}
						clients.clear();
					}
				}
				break;
			}
		}
		System.out.println("Server is Closed");
		}
		catch(IOException e)
		{
			System.err.println("Error Occured"+e.getMessage());
		}
	}

	//Removing the User From the clients hashSet
public static void removeClient(ClientIF user)
{
	clients.remove(user);
	System.out.println("User "+user.getName()+" left the Chat ");
}

//BroadCast the Message ...
public static void messageAll(String msg,ClientIF sender)
{
	synchronized(clients)
	{
		for(ClientIF client:clients)
		{
			if(client!=sender)
			{
				client.message(msg);
			}
		}
	}	
}
}

	
