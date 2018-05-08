package com.example.chirag.librarybooklocator;

import android.app.Activity;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.LargeTest;


import com.example.chirag.librarybooklocator.Crawler.Book;
import com.example.chirag.librarybooklocator.Crawler.FromISBNtoBook;
import com.example.chirag.librarybooklocator.Crawler.Httprequest;
import com.example.chirag.librarybooklocator.Crawler.SearchCrawler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by effy on 2018/5/7.
 */

public class UnitTest {


    String accountno = "effy";
    String passwords = "123456";
    String searchKeyword = "Database";

    //    Database systems : a practical approach to design, implementation, and management = Shu ju ku xi
//    http://opac.lib.xjtlu.edu.cn/opac/item.php?marc_no=0000105239
//            0000105239
//    Thomas M. Connolly, Carolyn E. Begg zhu
//    QA76.9.D26./C66/10.8/EN
//    Publishing House of Electronics Industry,2012.
//            9787121149962
//    Total: 5 Available: 0
//    http://img3.doubanio.com/view/subject/s/public/s8858037.jpg
//            《国外计算机科学教材系列•数据库系统:设计、实现与管理(第5版)(英文版)》是数据库领域的经典著作，内容系统全面，实用性强，被世界多所大学选为数据库相关课程的教材。全书主要内容有：数据库系统和数据库设计的基本知识；关系模型和关系语言；数据库分析和设计的主要技术；数据库设计方法学；数据库安全、事务管理、查询处理与优化；分布式DBMS与数据复制技术；面向对象数据库技术；DBMS与Web技术的结合，半结构化数据与XML；与商务智能有关的一些日益重要的技术，包括数据仓库、联机分析处理和数据挖掘以及数据库架构等。
    Book searchBook;

    BookComment bookComment;


//    @Rule
//        public ActivityUnitTestCase<LoginActivity> loginActivityActivityUnitTestCase = new ActivityUnitTestCase<LoginActivity>() {
//        @Override
//        public LoginActivity getActivity() {
//            return super.getActivity();
//        }
//    };
//    @Rule
//    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
//            LoginActivity.class);

    @Before
    public void setUp() throws Exception {


        String title = "Database systems : a practical approach to design, implementation, and management = Shu ju ku xi";


        String author = "Thomas M. Connolly, Carolyn E. Begg zhu";
        String callno = "QA76.9.D26./C66/10.8/EN";

        String ISBN = "9787121149962";

        String imglink = "http://img3.doubanio.com/view/subject/s/public/s8858037.jpg";

        System.out.println("Before");

        searchBook = new Book(title, callno, author, imglink, ISBN);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("After");
    }


//    @Rule
//    public ActivityUnitTestCase<LoginActivity> loginActivityActivityUnitTestCase = new ActivityUnitTestCase<LoginActivity>() {
//        @Override
//        public LoginActivity getActivity() {
//            return super.getActivity();
//        }
//    };

    //    public ActivityTestRule<LoginActivity> mActivityRule =
//            new ActivityTestRule<LoginActivity>(LoginActivity.class,
//                    false /* Initial touch mode */, false /*  launch activity */) {
//
//                @Override
//                protected void afterActivityLaunched() {
//                    // Enable JavaScript.
//                    onWebView().forceJavascriptEnabled();
//                }
//            }
    //LoginActivity
    @Test
    public void loginRequest() throws Exception {


    }

    @Test
    public void bookCrawler() throws Exception {
        ArrayList<Book> books = SearchCrawler.bookCrawler(searchKeyword);
        searchBook = books.get(0);
        assertNotNull(books);

    }

    @Test
    public void loadBookComments() throws Exception {
        String response = Httprequest.loadBookComment(searchBook.getTitle());
        ArrayList<BookComment> bookComments = Tab3.convert(response, accountno);
//
//        bookComment=bookComments.get(0);
        assertNotNull(bookComments);
    }

    @Test
    public void loadReply() throws Exception {
        String response = Httprequest.loadreply("93");
        ArrayList<Reply> replies = Reply.Convertreply(response, "93", accountno);
        assertNotNull(replies);

    }


    //FromISBNtoBook.class
    //Scan barcode to get book info
    @Test
    public void getBookByISBN() throws Exception {

        Book book = FromISBNtoBook.getBookByISBN(searchBook.getISBN());


        assertNotNull(book);
        assertEquals(searchBook.getCallNo(), book.getCallNo());
        // assertNotNull(FromISBNtoBook.getBookByISBN("9787111555391"));

    }

    @Test
    public void callNotoBookShelf() throws Exception {

        // CallNoToBookshelf callNoToBookshelf = new CallNoToBookshelf(loginActivityActivityUnitTestCase.getActivity().getAssets());
        CallNoToBookshelf callNoToBookshelf = new CallNoToBookshelf();

        int[] locations = callNoToBookshelf.findBookShelf(searchBook.getCallNo());
        assertNotNull(locations);

    }


}
