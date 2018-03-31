import java.util.Date;

public class Praise {
	private User user;
	private Comment comment;
	private int status;
	private Book book;
	private Date likeDate;
	
	public Praise (User user, Comment comment, int status) {
		this.user = user;
		this.comment = comment;
		this.book = comment.getbook();
		this.status = status;
	}
}