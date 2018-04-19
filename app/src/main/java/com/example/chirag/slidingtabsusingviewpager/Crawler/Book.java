package com.example.chirag.slidingtabsusingviewpager.Crawler;

/**
 * Created by effy on 2018/4/19.
 */

import java.net.*;
import java.util.ArrayList;

public class Book {
    private String title;
    private String link;
    private String marcNo;
    private String description;
    private String callNo;
    private String ISBN;
    private int favoriteNum;


    public Book(String title, String link, String marcNo, String description) {
        super();
        this.title = title;
        this.link = link;
        this.marcNo = marcNo;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMarcNo() {
        return marcNo;
    }

    public void setMarcNo(String marcNo) {
        this.marcNo = marcNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
