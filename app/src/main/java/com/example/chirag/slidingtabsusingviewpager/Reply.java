package com.example.chirag.slidingtabsusingviewpager;

import java.util.Date;

/**
 * Created by effy on 2018/4/6.
 */

import java.util.Date;

public class Reply {//true就是用户自己，false就是其他人,这是reply class 需要建立一个新的java文件
    String username;
    String content;
    String commentID;
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
}