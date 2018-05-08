package com.example.chirag.librarybooklocator.Crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InformationCrawler {
    //Get ISBN of Book
    public static String ParseISBN(String marcNo) {
        String page = getBookInformationPage(marcNo);
        Pattern patternItem = Pattern.compile("<dt>ISBN:</dt>(?s)(.*?)</dd>");
        Matcher matchItem = patternItem.matcher(page);
        String result = null;
        if (matchItem.find()) {
            String paraGroup = matchItem.group();
            StringBuffer preParaBuffer = new StringBuffer();
            StringBuffer postParaBuffer = new StringBuffer();
            StringBuffer prepreParaBuffer = new StringBuffer();
            preParaBuffer.append("<dt>ISBN:</dt>");
            postParaBuffer.append("</dd>");
            prepreParaBuffer.append("<dd>");
            result = (paraGroup.replaceAll(prepreParaBuffer.toString(), "").replaceAll(preParaBuffer.toString(), "")).replaceAll(postParaBuffer.toString(), "").trim();
            if (result.contains(" ") == true) {
                result = result.split(" ")[0];
            }
        }
        return result;
    }

    //Get Status of Book
    public static String ParseAvailable(String marcNo) {
        String page = getBookInformationPage(marcNo);
        Pattern patternItem = Pattern.compile("<td  width=\"20%\" >(.*?)</td>");
        Matcher matchItem = patternItem.matcher(page);
        String result = null;
        int availableNum = 0;
        ArrayList<String> availableList = new ArrayList();
        while (matchItem.find()) {
            String paraGroup = matchItem.group();
            StringBuffer preParaBuffer = new StringBuffer();
            StringBuffer postParaBuffer = new StringBuffer();
            preParaBuffer.append("<td  width=\"20%\" >");
            postParaBuffer.append("</td>");
            result = (paraGroup.replaceAll(preParaBuffer.toString(), "")).replaceAll(postParaBuffer.toString(), "").trim();
            //System.out.println(result);
            availableList.add(result);
            if (result.equals("<font color=green>in stock</font>")) {
                availableNum++;
            }
        }
        //System.out.println("Total: " + String.valueOf(availableList.size()));
        //System.out.println("Available: " + String.valueOf(availableNum));
        String availableResult = "Total: " + String.valueOf(availableList.size()) + " Available: " + String.valueOf(availableNum);
        return availableResult;
    }

    //Get Book detail HTML on library website
    private static String getBookInformationPage(String marcNo) {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        String result = null;
        Pattern compile = Pattern.compile("&#.*?;");
        try {
            url = new URL("http://opac.lib.xjtlu.edu.cn/opac/item.php?marc_no=" + marcNo);
            urlconn = url.openConnection();
            urlconn.connect();
            br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = compile.matcher(line);
                while (matcher.find()) {
                    String group = matcher.group();
                    String hexcode = "0" + group.replaceAll("(&#|;)", "");
                    line = line.replaceAll(group, (char) Integer.decode(hexcode).intValue() + "");
                }
                result += line + "\r\n";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
