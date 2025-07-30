import java.util.*;
import java.io.*;
import java.net.*;
public class ClientIF implements Runnable
{
	private BufferedReader input;
	private PrintWriter output;
	private String msg,response,name,password;
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
  	public String getPassword()
  	{
		return password;
	}
public void run()
	{


		try
		{
				 input=new BufferedReader(new InputStreamReader(client.getInputStream()));
				 output=new PrintWriter(client.getOutputStream(),true);
				 output.println("Welcome,User");
				 boolean usr=true;
				 while(usr)
				 {
					output.println("Enter Your Name :");
					name=input.readLine();
					output.println("Enter Your Password :");
					password=input.readLine();
					int index=MainServer.checkUser(this);	
					switch(index)
					{
						case -1:
						{
							output.println("Error occured");
							System.out.println(name +"Error occured in verification ");
							usr=false;
							break;
						}	
						case 1:
						{
							output.println("Welcome Back");
							System.out.println(name+": Already Exists User");
							MainServer.messageAll("User :"+name+" Joined",this);
							output.println("Do you want to read old Messages (Y/N )?");
							String s=input.readLine();
							if(s.equalsIgnoreCase("Y"))
							{
								MainServer.retrieveMsg(this);
								System.out.println("Messages retrived");
							}
							usr=false;
							break;
						}
						case -2:
						{
							output.println("Incorrect Password");
							System.out.println(name+"Entered Incorrect Password");
							break;
						}
						case 0:
						{
							output.println("Your Name has Been Added");
							System.out.println("User "+name+" Created ");
							MainServer.messageAll("New User : "+name+" Joined ",this);
							usr=false;
							break;
						}
					}
				}
				
				

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
					 MainServer.messageAll(msg,this);
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

