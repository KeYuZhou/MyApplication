package com.example.chirag.slidingtabsusingviewpager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by effy on 2018/4/6.
 */

public class BookComment {
    int kind; //0: 他人的评论，1：我的评论，2：他人的热评，3：我的热评
    Date date;
    String usernmae;
    int id;
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



}
