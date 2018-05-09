package com.example.chirag.librarybooklocator.Crawler;

/**
 * Created by effy on 2018/4/19.
 */

import java.util.ArrayList;

public class Book {
    private String title;
    private String link;
    private String marcNo;
    private String description;
    private String authorName;
    private String callNo;
    private String publisherInformation;
    private String ISBN;
    private String available;
    private String imageLink;
    private String content;

    public Book() {

    }

    //    booklist[i] = new Book(jsonObject.getString("Title" + i), jsonObject.getString("Library Link" + i),
//                        jsonObject.getString("MarcNo" + i), null,
//            jsonObject.getString("Author" + i),
//            jsonObject.getString("CallNo" + i),
//            jsonObject.getString("Publish Information" + i), null, null,
//            jsonObject.getString("Image Link" + i)
//            , jsonObject.getString("Content" + i)
    public Book(String title, String link, String marcNo, String authorName, String callNo, String publisherInformation, String imageLink, String content) {
        this.authorName = authorName;
        this.title = title;
        this.link = link;
        this.marcNo = marcNo;
        this.callNo = callNo;
        this.publisherInformation = publisherInformation;
        if (imageLink.equals(null) || imageLink.isEmpty() || imageLink.equals("not found")) {
            this.imageLink = "http://www.51allout.co.uk/wp-content/uploads/2012/02/Image-not-found.gif";
        } else {

            this.imageLink = imageLink;
        }
        this.content = content;
    }

    public Book(String title, String link, String marcNo, String description, String authorName, String callNo, String publisherInformation) {
        super();
        this.title = title;
        this.link = link;
        this.marcNo = marcNo;
        this.description = description;
        this.authorName = authorName;
        this.callNo = callNo;
        this.publisherInformation = publisherInformation;
    }

    public Book(String title, String link, String marcNo, String description, String authorName, String callNo, String publisherInformation, String ISBN, String available, String imgLink, String content) {
        super();
        this.title = title;
        this.link = link;
        this.marcNo = marcNo;
        this.description = description;
        this.authorName = authorName;
        this.callNo = callNo;
        this.publisherInformation = publisherInformation;
        this.ISBN = ISBN;
        this.available = available;
        this.imageLink = imgLink;
        this.content = content;
    }

    public Book(String title, String callNo, String authorName, String imageLink, String ISBN) {
        super();
        this.title = title;
        this.callNo = callNo;
        this.authorName = authorName;
        this.imageLink = imageLink;
        this.ISBN = ISBN;
    }

    public Book(String title, String link, String marcNo, String description) {
        super();
        this.title = title;
        this.link = link;
        this.marcNo = marcNo;
        this.description = description;
    }

    public String getAvailable() {
        return this.available;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }

    public String getPublisherInformation() {
        return publisherInformation;
    }

    public void setPublisherInformation(String publisherInformation) {
        this.publisherInformation = publisherInformation;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getimageLink() {
        return imageLink;
    }

    public void setimageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getContent() {
        return content;
    }


    public void setAvailable(String string) {
        this.available = string;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBook(ArrayList<String> packInfo) {
//        return new Book(packInfo.get(0),packInfo.get(1),packInfo.get(2),packInfo.get(3),packInfo.get(4),packInfo.get(5),packInfo.get(6),packInfo.get(7),packInfo.get(8),packInfo.get(9),packInfo.get(10));
        this.title = packInfo.get(0);
        this.link = packInfo.get(1);
        this.marcNo = packInfo.get(2);
        this.description = packInfo.get(3);
        this.authorName = packInfo.get(4);
        this.callNo = packInfo.get(5);
        this.publisherInformation = packInfo.get(6);
        this.ISBN = packInfo.get(7);
        this.available = packInfo.get(8);
        this.imageLink = packInfo.get(9);
        this.content = packInfo.get(10);
    }

    public ArrayList<String> packInfo() {
        //public Book(String title, String link, String marcNo, String description, String authorName,
        // String callNo, String publisherInformation, String ISBN, String available, String imgLink, String content) {

        ArrayList<String> packInfo = new ArrayList<>();
        packInfo.add(this.title);
        packInfo.add(this.link);
        packInfo.add(this.marcNo);
        packInfo.add(this.description);
        packInfo.add(this.authorName);
        packInfo.add(this.callNo);
        packInfo.add(this.publisherInformation);
        packInfo.add(this.ISBN);
        packInfo.add(this.available);
        packInfo.add(this.imageLink);
        packInfo.add(this.content);

        return packInfo;
    }


}