import java.net.*;
import java.util.ArrayList;

public class Book {
	private String title;
	private String link;
	private String marcNo;
	private String discription;
	private String callNo;
	private String ISBN;
	private int favoriteNum;
	private ArrayList<Comment> listComment;

	
	public Book(String title, String link, String marcNo, String discription){
		super();
		this.title = title;
		this.link = link;
		this.marcNo = marcNo;
		this.discription = discription;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	

	
}
