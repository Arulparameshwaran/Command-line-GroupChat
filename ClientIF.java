import java.util.*;
import java.io.*;
import java.net.*;
public class ClientIF implements Runnable
{
	private BufferedReader input;
	private PrintWriter output;
	private String msg,response,name;
	Socket client;
	private boolean clientState=true;
  public ClientIF(Socket csocket)
	{
		 client=csocket;
	}
  public String getName()
  	{
		return name;
  	}
public void run()
	{


		try
		{
				 input=new BufferedReader(new InputStreamReader(client.getInputStream()));
				 output=new PrintWriter(client.getOutputStream(),true);

				 output.println("Welcome,User");
				 output.println("Enter Your Name :");
				 name=input.readLine();
				 output.println("Your Name "+name+" has been Added");
				 System.out.println("User :"+name+" added");
			 while(clientState)
			 {
				 if(clientState!=false)
				 {
				 msg=input.readLine();
				 if(msg!=null)
				{	 System.out.println(name+": "+msg);
					 if(msg.equalsIgnoreCase("exit"))
					 {
					   this.CloseConnection();	
				   	  MainServer.removeClient(this);	   
					   this.clientState=false;
					   break;
					   					  
				         }
				}
				 }
				 else
					 break;
			 }

		}
			catch(IOException e)
			{
				System.out.println("Client Socket Has Been Closed ");
					
			}
	
	}
public void CloseConnection()
{
	try
	{
	System.out.println("Closing Connection");
	this.clientState=false;
	output.println("exit");
	output.close();
	input.close();
	client.close();
	}
	catch(IOException e){
		System.out.println("Error Occured at Closing client Connection");
		e.printStackTrace();
	}
}
}

