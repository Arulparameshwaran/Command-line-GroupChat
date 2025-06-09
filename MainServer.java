import java.util.*;
import java.io.*;
import java.net.*;
class MainServer
{
	
	static Scanner serverinput=new Scanner(System.in);
	public static void main(String[] args)
	{
		try
		(ServerSocket server=new ServerSocket(5000))
		{	
			System.out.println("Server is Started Running ");
		while(true)
		{
			try
			{
				Socket user=server.accept();
				System.out.println("Client is Connected");
				if(serverinput.nextLine().equalsIgnoreCase("exit"))
				{
					System.out.println("Server is Closed");
					user.close();
					break;
				}
			}
			catch(IOException e)
			{
				System.err.println("Socket Error"+e.getMessage());
			}
		}
		}
		catch(IOException e)
		{
			System.err.println("Error Occured"+e.getMessage());
		}
	}
}

		
