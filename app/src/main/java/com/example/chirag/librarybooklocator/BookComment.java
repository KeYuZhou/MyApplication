package com.example.chirag.librarybooklocator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by effy on 2018/4/6.
 */

public class BookComment {
    int kind; //0: 他人的评论，1：我的评论，2：他人的热评，3：我的热评
    Date date;
    String usernmae;
    public int id;
    String content;
    int like;


    public BookComment(String usernmae, String content) {
        this.usernmae = usernmae;
        this.date = getNow();
        this.content = content;
        this.like = 0;
        this.kind = 1;
    }

    public BookComment(int id, String usernmae, Date date, String content, int like, int kind) {
        this.id = id;
        this.usernmae = usernmae;
        this.date = date;
        this.content = content;
        this.like = like;
        this.kind = kind;
    }


    public String getTime() {

        return RelativeDateFormat.format(date);
    }

    public Date getNow() {
        return Calendar.getInstance().getTime();
    }


    public static ArrayList<BookComment> convert(String result, String username) {
        ArrayList<BookComment> com = new ArrayList<BookComment>();
        try {


            JSONObject jsonObject = (JSONObject) new JSONObject(result).get("params");
            int number = Integer.parseInt(jsonObject.getString("number"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            if (number == 0) {
                System.out.println("fail");
            } else {
                System.out.println("good");
                for (int i = 0; i < number; i++) {

                    try {
                        int kind = 0;
                        if (jsonObject.getString(i + "username").equals(username)) {
                            kind = 1;
                        }
                        if (Integer.parseInt(jsonObject.getString(i + "like")) >= 10) {
                            kind += 2;
                        }

                        BookComment a = new BookComment(Integer.parseInt(jsonObject.getString(i + "id")),
                                jsonObject.getString(i + "username"),
                                df.parse(jsonObject.getString(i + "date")),
                                jsonObject.getString(i + "content"), Integer.parseInt(jsonObject.getString(i + "like")), kind);
                        com.add(a);

                        // Log.e("TAG",result);
                    } catch (Exception e) {
                        System.out.println("error");
                    }

                }
                Collections.sort(com, new com());


            }


        } catch (JSONException e) {
            //做自己的请求异常操作，如Toast提示（“无网络连接”等）

        }
        return com;
    }



}
