import java.util.ArrayList;

public class Search {
	private static String strSearchType;
	private static String match_flag;
	private static String strText;
	private static String doctype;
	private static String sort;
	private static String orderby;
	private static ArrayList<Book> listBook;
	
	public Search(String strSearchType, String match_flag, String strText, String doctype, String sort, String orderby) {
		this.strSearchType = strSearchType;
		this.match_flag = match_flag;
		this.strText = strText;
		this.doctype = doctype;
		this.sort = sort;
		this.orderby = orderby;
	}
	
	public static String makeURL(){
		String URL = "http://opac.lib.xjtlu.edu.cn/opac/search_rss.php?";
		URL = URL+ "dept=00&"  + strSearchType +"="+ strText + "&doctype="+ doctype  + "&lang_code=ALL&match_flag=" + match_flag + "&displaypg=20&showmode=list&orderby="  + orderby + "&sort="+ sort ;
		return URL;
	}
	
	public static ArrayList<Book> SearchBook(Search s) {
		Crawler.bookCrawler(s);
		return listBook;
		
	}
	
}
