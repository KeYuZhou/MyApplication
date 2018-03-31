//STEP 1. Import required packages
// See more detail at http://www.yiibai.com/jdbc/

import java.sql.*;
import java.util.*;
public class CreateTable {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/readerCircle";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "170624";

   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");

      //STEP 4: Execute a query
      System.out.println("Creating table in given database...");
      stmt = conn.createStatement();
      

      String sql = "CREATE TABLE user " +
                   "(id INTEGER not NULL, " +
                   " username VARCHAR(255), " + 
                   " password INTEGER, " + 
                   " email VARCHAR(255), " +
                   " headlink VARCHAR(255), " +
                   " PRIMARY KEY ( id ))"; 

      stmt.executeUpdate(sql);
      System.out.println("Created table in given database...");
      
      sql = "CREATE TABLE comment " +
              "(id INTEGER not NULL, " +
              " book_id INTEGER, " + 
              " user_id INTEGER, " + 
              " content VARCHAR(255), " +
              " PRIMARY KEY ( id ))"; 
      
      stmt.executeUpdate(sql);
      System.out.println("Created table in given database...");
      
      sql = "CREATE TABLE reply " +
              "(id INTEGER not NULL, " +
              " comment_id INTEGER, " + 
              " from_userid INTEGER, " + 
              " to_userid INTEGER, " + 
              " content VARCHAR(255), " +
              " PRIMARY KEY ( id ))"; 
      
      stmt.executeUpdate(sql);
      System.out.println("Created table in given database...");
      
      sql = "CREATE TABLE praise " +
              "(id INTEGER not NULL, " +
              " type_id INTEGER, " + 
              " type INTEGER, " + 
              " user_id INTEGER, " + 
              " status INTEGER, " +
              " PRIMARY KEY ( id ))"; 
      
      stmt.executeUpdate(sql);
      System.out.println("Created table in given database...");
      
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            conn.close();
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end JDBCExample