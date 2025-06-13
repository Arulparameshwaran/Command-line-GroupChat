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

	//Reading Clients Message ..	
	 	 while(clientState)
			 {
				 if(clientState!=false)
				 {
				 msg=input.readLine();
				 if(msg!=null)
				 {
					 if(msg.equalsIgnoreCase("exit"))
					 {
					   this.CloseConnection();	
				   	   MainServer.removeClient(this);	   
					   MainServer.messageAll("User "+name+" left the Chat ",this);
					   this.clientState=false;
					   break;	   					  
				         }
					 System.out.println(name+": "+msg);
					 MainServer.messageAll(name+": "+msg,this);
				}
				 }
				 else
					 break;
			 }

		}
			catch(IOException e)
			{
				System.out.println("Client Socket("+name+") Has Been Closed ");
					
			}
	
	}

//Closing Client Socket at Server Side ..
public void CloseConnection()
{
	try
	{
	output.println("Closing Connection");
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
//Receiving Message or BroadCast
public void message(String servermsg)
{
	output.println(servermsg);
}
}

