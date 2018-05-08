package com.example.chirag.librarybooklocator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by effy on 2018/4/6.
 */

public class Reply {//true就是用户自己，false就是其他人,这是reply class 需要建立一个新的java文件
    String username;
    String content;
    public String commentID;
    Date date;
    Boolean user;

    public Reply(String username, String content, String commentID, Date date, Boolean user) {
        this.commentID = commentID;
        this.username = username;
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public String getTime() {

        return RelativeDateFormat.format(date);
    }

    public static ArrayList<Reply> Convertreply(String response, String CommentID, String usernanme) {
        ArrayList<Reply> reply = new ArrayList<Reply>();
        try {
            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
            int number = Integer.parseInt(jsonObject.getString("number"));  //注④
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

            if (number == 0) {

            } else {

                for (int i = 0; i < number; i++) {
                    boolean user = false;
                    if (usernanme.equals(jsonObject.getString(i + "username"))) {
                        user = true;
                    }
                    try {
                        reply.add(new Reply(jsonObject.getString(i + "username"),
                                jsonObject.getString(i + "content"),
                                CommentID, df.parse(jsonObject.getString(i + "date")), user));
                    } catch (Exception e) {

                    }

                }
                Collections.sort(reply, new comreply());

            }


        } catch (JSONException e) {
            //做自己的请求异常操作，如Toast提示（“无网络连接”等）

        }
        return reply;
    }

    static class comreply implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Reply x = (Reply) o1;
            Reply y = (Reply) o2;

            return x.date.compareTo(y.date);
        }
    }
}