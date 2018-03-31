import java.util.Date;

public class Reply {
	private User fromUser;
	private Comment comment;
	private User toUser;
	private Book book;
	private Date replyDate;
	private String content;
	
	public Reply (User fromUser, Comment comment, String content, User toUser) {
		this.fromUser =fromUser;
		this.comment = comment;
		this.toUser = toUser;
		this.content = content;
		this.book = comment.getbook();
	}
}
