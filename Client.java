import java.io.*;
import java.util.*;
import java.net.*;
class Client
{
	static BufferedReader in,input;
	static PrintWriter out;
	static Socket clientsocket;
	public static void main(String[] args)
	{
		try
		{
			 clientsocket=new Socket("localhost",5000);
			 in=new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
		   	 out=new PrintWriter(clientsocket.getOutputStream(),true);	  
			 input=new BufferedReader(new InputStreamReader(System.in)); 
			String msg;
			System.out.println("Connected to Server");
			//Receiving Data from Server
			new Thread(()->{
				String servermsg;
			while(true)
			{
				try
				{	
					servermsg=in.readLine();
					if(servermsg!=null)
					{
						if(servermsg.equalsIgnoreCase("exit"))//If Server is going Down.., Client connection will Closed
						{
							closeSelf();
							break;
						}
						System.out.println(servermsg);
					}
					
				}
				catch(IOException e)
				{
					System.out.println("Server Connection Lost :"+e.getMessage());
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
					if(msg.equalsIgnoreCase("exit"))
					{
						out.println(msg);
						break;
					}
				out.println(msg);
				
				}
			}
					
			


		}
		catch(IOException e)
		{
			System.err.println("Server Error "+e.getMessage());
		}
	}
	//To Exit From The Server ...
private static void closeSelf()
{
	try
	{
		in.close();
		out.close();
		input.close();	
		clientsocket.close();
		System.out.println("Closed");
	}
	catch(IOException e)
	{
		System.out.println("Error Occured while Closing Client "+e.getMessage());
	}
		
}
}
