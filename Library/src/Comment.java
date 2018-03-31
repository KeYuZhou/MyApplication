import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Comment {
	private User user;
	private Book book;
	private Date commentDate;
	private String commentText;
	private static int LikeNumber;
	private static ArrayList listLike;
	private static ArrayList listReply;
	
	public Comment(User user, Book book, Date commentDate, String commentText) {
		this.user = user;
		this.book = book;
		this.commentDate = commentDate;
		this.commentText = commentText;
		LikeNumber = 0;
		listLike = new ArrayList<Praise>();
		listReply = new ArrayList<Reply>();
	}
	
	public static boolean beLiked(Comment c, User u) {
		boolean result = true;
		for (Iterator<Praise> list = User.getlistLike(u).iterator();list.hasNext();) {
			if (list == c) {
				result = false;
			}
		}
		LikeNumber++;
		return result;
	}
	
	public static boolean cancelBeLiked(Comment c, User u) {
		boolean result = false;
		for (Iterator<Praise> list = User.getlistLike(u).iterator();list.hasNext();) {
			if (list == c) {
				result = true;
			}
		}
		LikeNumber--;
		return result;
	}

	public Book getbook() {
		// TODO Auto-generated method stub
		return book;
	}

	public User getUser() {
		// TODO Auto-generated method stub
		return user;
	}
}
