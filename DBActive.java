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
}
