import java.util.*;
import java.io.*;
import java.net.*;
public class ClientIF implements Runnable
{
	private BufferedReader input;
	private PrintWriter output;
	private String msg,response,name;
	Socket client;
  public ClientIF(Socket csocket)
	{
		 client=csocket;
	}
public void run()
	{
		System.out.println("running");

		try
		{
			
			
				 input=new BufferedReader(new InputStreamReader(client.getInputStream()));
				 output=new PrintWriter(client.getOutputStream(),true);

				 output.println("Welcome,User");
				 output.println("Enter Your Name :");
				 name=input.readLine();
				 output.println("User :"+name+" added");
			 while(true)
			 {
				 msg=input.readLine();
				 if(msg!=null)
					 System.out.println(name+": "+msg);
				 if(msg.equalsIgnoreCase("exit"))
				 {
                                	   input.close();
			    		   output.close();
					  
					   System.out.println("Client Closed Connection");
					   client.close();
					   break;					  
				 }
			 }

		}
			catch(IOException e)
			{
				System.out.println("Error Occured in I/O");

			}
	
	}
}

