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
				if(serverinput.readLine().equalsIgnoreCase("exit"))
				{
					System.out.println("Server is Closed");
					active=false;
					server.close();	

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
							client.CloseConnection();
							removeClient(client);
						}
					}
				}
				System.out.println("Socket Closed");
				break;
			}
		}
		}
		catch(IOException e)
		{
			System.err.println("Error Occured"+e.getMessage());
		}
	}
public static void removeClient(ClientIF user)
{
	clients.remove(user);
	System.out.println("User "+user.getName()+" Removed ");
}
}

	
