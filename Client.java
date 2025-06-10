import java.io.*;
import java.util.*;
import java.net.*;
class Client
{
	static Scanner input=new Scanner(System.in);

	public static void main(String[] args)
	{
		try(Socket clientsocket=new Socket("localhost",5000);
			BufferedReader in=new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
	   		PrintWriter out=new PrintWriter(clientsocket.getOutputStream(),true);	  
		  	BufferedReader input=new BufferedReader(new InputStreamReader(System.in)); )
		{
	 
			String msg;
			System.out.println("Connected to Server");
			//Receiving Data from Client
			new Thread(()->{
				while(true)
				{
				try
				{
				String servermsg=in.readLine();
				if(servermsg!=null)
				{
					System.out.println(servermsg);
				}
				}
				catch(IOException e)
				{
					System.out.println("Server not Responding "+e.getMessage());
					break;
				}
			}
			}).start();
			//User Input Sending to Server
			while(true)
			{
				msg=input.readLine();
				if(msg!=null)
				{
					out.println(msg);
				}
			
				if(msg.equalsIgnoreCase("exit"))
				{
					out.println(msg);
					in.close();
					out.close();
					input.close();	
					clientsocket.close();
					System.out.println("Closedddd");
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
