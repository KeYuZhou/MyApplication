package com.example.chirag.librarybooklocator.Crawler;

/**
 * Created by effy on 2018/4/26.
 */


import com.example.chirag.librarybooklocator.BookComment;
import com.example.chirag.librarybooklocator.Reply;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


import net.sf.json.JSONObject;


public class Httprequest {


    public static HashMap<BookComment, ArrayList<Reply>> loadall(String bookname, String username) {
        String result = Httprequest.loadBookComment(bookname);
        ArrayList<BookComment> commentlist = BookComment.convert(result, username);
        HashMap<BookComment, ArrayList<Reply>> map = new HashMap<BookComment, ArrayList<Reply>>();
        for (int i = 0; i < commentlist.size(); i++) {
            String replyresult = loadreply(commentlist.get(i).id + "");

            map.put(commentlist.get(i), Reply.Convertreply(replyresult, commentlist.get(i).id + "", username));
        }
        return map;
    }

    public static String loadBookComment(String book)

    {
        String URL = "http://39.107.109.19:8080/Groupweb/LoadBookServlet";

        JSONObject jsonObject = null;
        OutputStreamWriter out = null;
        StringBuffer buffer = new StringBuffer();
        try {

            //1.连接部分

            URL url = new URL(URL);
            // http协议传输
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            //2.传入参数部分
            // 得到请求的输出流对象

            //将JSON数据添加到输出流中


            out = new OutputStreamWriter(httpUrlConn.getOutputStream(), "UTF-8");
            // 把数据写入请求的Body
            out.write("Book=" + book); //参数形式跟在地址栏的一样
            out.flush();
            out.close();

            //3.获取数据
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String loadreply(String CommentID)

    {
        String URL = "http://39.107.109.19:8080/Groupweb/LoadReplyServlet";

        JSONObject jsonObject = null;
        OutputStreamWriter out = null;
        StringBuffer buffer = new StringBuffer();
        try {

            //1.连接部分

            URL url = new URL(URL);
            // http协议传输
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            //2.传入参数部分
            // 得到请求的输出流对象

            //将JSON数据添加到输出流中


            out = new OutputStreamWriter(httpUrlConn.getOutputStream(), "UTF-8");
            // 把数据写入请求的Body
            out.write("CommentID=" + CommentID); //参数形式跟在地址栏的一样
            out.flush();
            out.close();

            //3.获取数据
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}

