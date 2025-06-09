import java.io.*;
import java.util.*;
import java.net.*;
class Client
{
	static Scanner input=new Scanner(System.in);
	public static void main(String[] args)
	{
		try(Socket clientsocket=new Socket("localhost",5000))
		{
	 
			System.out.println("Connected to Server");
			while(true)
			{
			if(input.nextLine().equalsIgnoreCase("exit"))
			{
				System.out.println("Closing Connection");
				clientsocket.close();
				break;
			
			}
			}
		}
		catch(IOException e)
		{
			System.err.println("Server Error"+e.getMessage());
		}
	}
}
