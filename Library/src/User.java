import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;

public class User {
	private static int id;
	private static String username;
	private static String password;
	private static String emailAddress;
	private static Date registerTime;
	private static String headLink;
	private ArrayList listFavorite;
	private ArrayList listComment;
	private static ArrayList listLike;
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/readerCircle";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "170624";
	
	public User(String username, String password, String emailAddress) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.username = username;
        registerTime = new Date();       
        listFavorite = new ArrayList<Book>();
        listComment = new ArrayList<Comment>();
        listLike = new ArrayList<Praise>();
	}
	

	public static ArrayList<Praise> getlistLike(User user) {
		//JDBC
		return listLike;
	}
	
	public boolean addToFavorateList(Book b, User user) {
		// JDBC
		return true;
	}
	
	public boolean removeFromFavorateList(Book b, User user) {
		// JDBC
		return true;
	}
}
