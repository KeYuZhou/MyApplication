package com.example.chirag.slidingtabsusingviewpager.Crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FromISBNtoBook {
    public static Book getBookByISBN(String ISBN) {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        String result = null;
        Book b = null;
        Pattern compile = Pattern.compile("&#.*?;");
        try {
            String URL = "http://opac.lib.xjtlu.edu.cn/opac/search_rss.php?dept=ALL&isbn=" + ISBN + "&doctype=ALL&lang_code=ALL&match_flag=forward&displaypg=20&showmode=list&orderby=DESC&sort=CATA_DATE&onlylendable=yes&with_ebook=&with_ebook=";

            url = new URL(URL);
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
            b = ParseXML(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
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

        return b;
    }

    public static Book ParseXML(String result) {
        try {
            Pattern patternItem = Pattern.compile("<item>(.*?)</item>");
            Matcher matchItem = patternItem.matcher(result);
            Book b = null;
            while (matchItem.find()) {
                String item = "Found value: " + matchItem.group(0);
                String title = getParameter(item, "title").substring(2);
                String link = getParameter(item, "link");
                String marcNo = link.split("marc_no=")[1];
                String description = getParameter(item, "description");
                String authorName = (description.split(":")[1]).split(". call no")[0];
                String callNo = (description.split(":")[2]).split(" ")[0];
                String publichInformation = description.split(":")[3];
                String ISBN = InformationCrawler.ParseISBN(marcNo);
                String available = InformationCrawler.ParseAvailable(marcNo);
                String imgLink = DoubanCrawler.ParseImgLink(ISBN);
                String content = DoubanCrawler.ParseContent(ISBN);

                //System.out.println(title);
                //System.out.println(link);
                //System.out.println(marcNo);
                //System.out.println(authorName);
                //System.out.println(callNo);
                //System.out.println(publichInformation);

                //b = new Book(title, link, marcNo, description, authorName, callNo, publichInformation);
            }
            return b;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String getParameter(String item, String parameter) {
        try {
            String result = "";
            StringBuffer itemBuffer = new StringBuffer();
            itemBuffer.append("<");
            itemBuffer.append(parameter);
            itemBuffer.append(">(.*?)</");
            itemBuffer.append(parameter);
            itemBuffer.append(">");
            Pattern pattern = Pattern.compile(itemBuffer.toString());
            Matcher matcher = pattern.matcher(item);
            if (matcher.find()) {
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
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
