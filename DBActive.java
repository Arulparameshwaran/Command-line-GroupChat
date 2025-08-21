import java.util.*;
import java.io.*;
import java.sql.*;
import java.time.*;
public class DBActive
{
	String url="jdbc:mariadb://localhost:3306/Groupchat";
	String dbUser="Arul";
	String password="Arul01";
Connection dbcon=null;
Statement st=null;
PreparedStatement ps=null;
LocalDateTime dt;
DBActive()
{
	try
	{
	
	dbcon=DriverManager.getConnection(url,dbUser,password);
	System.out.println("Connected to Database ");
	st=dbcon.createStatement();
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
}
	//Inserting User if Not Already Exists
	protected int insertUser(String name,String password)
	{
		try
		{
			ps=dbcon.prepareStatement("select password from User_details where name = ?");
			ps.setString(1,name);
			ResultSet result=ps.executeQuery();
			if(result.next())
			{
				String pass=result.getString("password");
				if(pass.equals(password))
				{
					return 1;
				}
				else
				{
					return -2;
				}
			}
			else
			{
				ps=dbcon.prepareStatement("insert into User_details(name,password,Joined_At) Values(?,?,?)");
				ps.setString(1,name);
				ps.setString(2,password);
				ps.setTimestamp(3,Timestamp.valueOf(LocalDateTime.now()));
				ps.executeUpdate();
				return 0;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
		
	}
	//Stroing Messages 
	protected void storeMsg(String msg,String user)
	{
		try
		{
			dt=LocalDateTime.now();
			ps=dbcon.prepareStatement("insert into Messages(msg,sender,date_time) Values(?,?,?)");
			ps.setString(1,msg);
			ps.setString(2,user);
			ps.setTimestamp(3,Timestamp.valueOf(dt));
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	//Retriving Old Messages of Old User
	protected ArrayList<String> getOldMsgs(String name)
	{
		ArrayList<String> msgs=new ArrayList<>();
		ArrayList<String> result=new ArrayList<>();
		try
		{
			ResultSet res;
			ps=dbcon.prepareStatement("select msgno from Messages where sender = ? AND msg like 'New User %' ");
			ps.setString(1,name);
			res=ps.executeQuery();
			Timestamp ts=null;
			ArrayList<LocalDateTime> datetime=new ArrayList<>();
			LocalDate ld=null;
			if(res.next())
			{
				int no=res.getInt("msgno");
				System.out.println("Result set");
				System.out.println("No :"+no);
			
				if(no!=0)
				{
				ps=dbcon.prepareStatement("select msg,sender,date_time from Messages where msgno > ?");
				ps.setInt(1,no);
				res=ps.executeQuery();
				while(res.next())
				{
					msgs.add(res.getString("sender")+" : "+res.getString("msg"));
					ts=res.getTimestamp("date_time");
					datetime.add(ts.toLocalDateTime());
					
				}
				//Dividing Messages As per Date
				
				for(int i=0;i<msgs.size();i++)
				{
					ld=datetime.get(i).toLocalDate();
					if(i>0)
					{
						if(ld.equals(datetime.get(i-1).toLocalDate()))
						{
							ld=null;
						}
							
					}
					if(ld!=null)
					{
						result.add("\n\t"+ld+"\n"+msgs.get(i));
					}
					else
					{
						result.add(msgs.get(i));
					}
						
				}
				return result;
				}	
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return msgs;
	}
}
