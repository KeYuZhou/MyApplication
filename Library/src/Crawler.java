import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crawler {
	static ArrayList bookList = new ArrayList();
	
	public static ArrayList<Book> bookCrawler(Search s) {
		URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        String result = null;
        Pattern compile = Pattern.compile("&#.*?;");  
        try{
        	url = new URL(s.makeURL());
        	System.out.println(s.makeURL());
        	urlconn = url.openConnection();
        	urlconn.connect();
        	br = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
        	String line;
        	while((line = br.readLine()) != null)
        	{
        		Matcher matcher = compile.matcher(line);
        		while (matcher.find()) {  
        	            String group = matcher.group();
        	            String hexcode = "0" + group.replaceAll("(&#|;)", "");
        	            line = line.replaceAll(group, (char) Integer.decode(hexcode).intValue() + "");  
        		 }
        		result += line + "\r\n";
        	}
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
        	try{
        		if (br != null) {
        			br.close();
        		}
        	} catch (Exception e){
        		e.printStackTrace();
        	}
        }
        System.out.println(result);
		return ParseXML(result);
	}
	
	public static ArrayList<Book> ParseXML(String result) {
		Pattern patternItem = Pattern.compile("<item>(.*?)</item>");
		Matcher matchItem = patternItem.matcher(result);
		while(matchItem.find()) {
	         String item = "Found value: " + matchItem.group(0);
	         System.out.println(getParameter(item,"title"));  
	         String link = getParameter(item,"link");
	         System.out.println(link);  
	         System.out.println(getParameter(item,"description"));  
	         String marcNo = link.split("marc_no=")[1];
	         Book b = new Book(getParameter(item,"title"),getParameter(item,"link"),marcNo,getParameter(item,"description"));
	         bookList.add(b);
		}
		return bookList;
	}
	
	public static String getParameter(String item, String parameter) {
		String result="";
		StringBuffer itemBuffer = new StringBuffer();
		itemBuffer.append("<");  
		itemBuffer.append(parameter);   
		itemBuffer.append(">(.*?)</");  
		itemBuffer.append(parameter);  
		itemBuffer.append(">"); 
	    Pattern pattern=Pattern.compile(itemBuffer.toString());  
	    Matcher matcher=pattern.matcher(item);
	    if (matcher.find()){
	    	String paraGroup = matcher.group();
	    	StringBuffer preParaBuffer = new StringBuffer();
	    	StringBuffer postParaBuffer = new StringBuffer();
	    	preParaBuffer.append("<");
	    	preParaBuffer.append(parameter);
	    	preParaBuffer.append(">*");
	    	postParaBuffer.append("</");
	    	postParaBuffer.append(parameter);
	    	postParaBuffer.append(">*");
	    	result = (paraGroup.replaceAll(preParaBuffer.toString(), "")).replaceAll(postParaBuffer.toString(), "");
	    }
	    return result;
	}
	
}
