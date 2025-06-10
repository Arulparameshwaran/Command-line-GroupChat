import java.util.*;
import java.io.*;
import java.net.*;
class MainServer
{
	static volatile boolean active =true;
	static Scanner serverinput=new Scanner(System.in);
	static List<ClientIF> clients=new ArrayList<>();
	public static void main(String[] args)
	{
		try
		(ServerSocket server=new ServerSocket(5000))
		{	
			System.out.println("Server is Started Running ");
			new Thread(()->{
				try
				{
				if(serverinput.nextLine().equalsIgnoreCase("exit"))
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
				System.err.println("Socket Error"+e.getMessage());
				break;
			}
		}
		}
		catch(IOException e)
		{
			System.err.println("Error Occured"+e.getMessage());
		}
	}
}

		
