package com.example.chirag.slidingtabsusingviewpager.Crawler;

/**
 * Created by effy on 2018/4/19.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageCrawler {
    public static String BookImageCrawler(String ISBN) {
        Log.e("ImageCrawler", "start");
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
        Pattern patternItem = Pattern.compile("<link href=(.*?)rel=\"image\"/>");
        Matcher matchItem = patternItem.matcher(result);
        while (matchItem.find()) {
            String paraGroup = matchItem.group();
            StringBuffer preParaBuffer = new StringBuffer();
            StringBuffer postParaBuffer = new StringBuffer();
            preParaBuffer.append("<link href=\"");
            postParaBuffer.append("\" rel=\"image\"/>");
            imgLink = (paraGroup.replaceAll(preParaBuffer.toString(), "")).replaceAll(postParaBuffer.toString(), "");
            System.out.println(imgLink);
        }
        return imgLink;
    }
}