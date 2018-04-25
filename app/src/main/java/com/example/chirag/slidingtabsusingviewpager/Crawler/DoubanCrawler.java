package com.example.chirag.slidingtabsusingviewpager.Crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubanCrawler {
    //Get image link from Douban API
    public static String ParseImgLink(String ISBN) {
        String page = getDoubanPage(ISBN);
        String imgLink = null;
        try {
            Pattern patternItem = Pattern.compile("<link href=(.*?)rel=\"image\"/>");
            Matcher matchItem = patternItem.matcher(page);
            while (matchItem.find()) {
                String paraGroup = matchItem.group();
                StringBuffer preParaBuffer = new StringBuffer();
                StringBuffer postParaBuffer = new StringBuffer();
                preParaBuffer.append("<link href=\"");
                postParaBuffer.append("\" rel=\"image\"/>");
                imgLink = (paraGroup.replaceAll(preParaBuffer.toString(), "")).replaceAll(postParaBuffer.toString(), "");
                //System.out.println(imgLink);
            }

        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return imgLink;
    }

    //Get content summary from Douban API
    public static String ParseContent(String ISBN) {
        String page = getDoubanPage(ISBN);
        String content = null;
        try {
            Pattern patternItem = Pattern.compile("<summary>(?s)(.*?)</summary>");
            Matcher matchItem = patternItem.matcher(page);

            while (matchItem.find()) {
                String paraGroup = matchItem.group();
                StringBuffer preParaBuffer = new StringBuffer();
                StringBuffer postParaBuffer = new StringBuffer();
                preParaBuffer.append("<summary>");
                postParaBuffer.append("</summary>");
                content = (paraGroup.replaceAll(preParaBuffer.toString(), "")).replaceAll(postParaBuffer.toString(), "");
                //System.out.println(content);
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return content;
    }

    //Get Book detail HTML on Douban API
    private static String getDoubanPage(String ISBN) {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        String result = null;
        String imgLink = null;
        try {
            url = new URL("http://api.douban.com/book/subject/isbn/" + ISBN);
            urlconn = url.openConnection();
            urlconn.connect();
            br = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line + "\r\n";
            }
            //System.out.println(result);
        } catch (FileNotFoundException e) {
            System.out.println(e);
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