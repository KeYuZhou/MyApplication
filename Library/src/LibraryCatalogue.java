import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryCatalogue {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/readerCircle";
	static final String USER = "root";
	static final String PASS = "170624";
	
	public static void main(String[]args){
		Search s = new Search("title","forward" ,"ios","01","M_TITLE","DESC" ); 
		Crawler.bookCrawler(s);
	}
}
