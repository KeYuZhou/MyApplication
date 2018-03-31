import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class popularWord {
	static ArrayList<String> popularWordsList = new ArrayList();
	public static ArrayList<String>  wordsCrawler() {
		URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        String result = null;
        Pattern compile = Pattern.compile("&#.*?;");  
        try{
        	url = new URL("http://opac.lib.xjtlu.edu.cn/opac/top100.php");
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
        
		Pattern patternItem = Pattern.compile("<TD width=\"25%\">(.*?)</a>");
		Matcher matchItem = patternItem.matcher(result);
		while(matchItem.find()) {
			String group = matchItem.group();
	    	StringBuffer preParaBuffer = new StringBuffer();
	    	StringBuffer postParaBuffer = new StringBuffer();
	    	preParaBuffer.append("<(.*?)>");
	    	postParaBuffer.append("</(.*?)>");
	    	result = (group.replaceAll(preParaBuffer.toString(), "")).replaceAll(postParaBuffer.toString(), "");
	    	System.out.println(result);
	    	popularWordsList.add(result);
		}
		
		return popularWordsList;
	}
}
