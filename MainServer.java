import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
class MainServer
{
	static volatile boolean active =true;
	static BufferedReader serverinput=new BufferedReader(new InputStreamReader(System.in));
	static Set<ClientIF> clients=Collections.synchronizedSet(new HashSet<>());
	static DBActive DBIF=new DBActive();
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
				client.message(sender.getName()+" : "+msg);
			}
		}
	}	
	DBIF.storeMsg(msg,sender.getName());
}
// Creating New User or Validating Already Existing User
public static int checkUser(ClientIF user)
{
	String name=user.getName();
	String password=user.getPassword();
	System.out.println("Name :"+name+"\nPassword :"+password);
	int index=DBIF.insertUser(name,password);
	return index;
}

//Retrieve OLD MESSAGES
public static void retrieveMsg(ClientIF olduser)
{
	ArrayList<String> result=DBIF.getOldMsgs(olduser.getName());
	System.out.println("Messages Retrieved for user :"+olduser.getName());
	for(String msgs:result)
	{
		olduser.message(msgs);
	}
}

}


	
