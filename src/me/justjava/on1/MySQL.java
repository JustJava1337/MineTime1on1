package me.justjava.on1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	  public static String user = On1.instance.getConfig().getString("MySQL.User");
	  public static String pass = On1.instance.getConfig().getString("MySQL.Pass");
	  public static String host = On1.instance.getConfig().getString("MySQL.Host");
	  public static String data = On1.instance.getConfig().getString("MySQL.DB");	
	  public static Connection connection;

	  public static void close()
	  {
	    try
	    {
	      if (connection != null)
	        connection.close();
	    }
	    catch (Exception ex) {
	      System.out.println(ex.getMessage());
	    }
	  }

	  public static void connect() {
	    
	    try
	    {
	      Class.forName("com.mysql.jdbc.Driver");
	      connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + data, 
	        user, pass);
	    } catch (Exception ex) {
	      System.out.println(ex.getMessage());
	      
	    }

	    
	  }

	  public static void Update(String qry) {
	    try {
	      Statement stmt = connection.createStatement();
	      stmt.executeUpdate(qry);

	      stmt.close();
	    } catch (Exception ex) {
	      System.out.println(ex.getMessage());
	    }
	  }

	  public static ResultSet Query(String qry) {
	    ResultSet rs = null;
	    try
	    {
	      Statement stmt = connection.createStatement();
	      rs = stmt.executeQuery(qry);
	    }
	    catch (Exception ex) {
	      System.out.println(ex.getMessage());
	    }

	    return rs;
	  }
}
