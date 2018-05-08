package com.example.chirag.librarybooklocator.Crawler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by effy on 2018/5/7.
 */
public class DoubanCrawlerTest {
    @Before
    public void setUp() throws Exception {
        System.out.println("Before");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("After");
    }

    @Test
    public void parseImgLink() throws Exception {
        //DoubanCrawler.ParseImgLink("9787302328810");
        assertNotNull(DoubanCrawler.ParseImgLink("9787302328810"));
        // fail("pareseImgLink-fail");
    }

    @Test
    public void parseContent() throws Exception {
        //  DoubanCrawler.ParseContent("9787302328810");
        assertNotNull(DoubanCrawler.ParseImgLink("9787302328810"));

        // fail("parseContent-fail");
    }

}