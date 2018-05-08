package com.example.chirag.librarybooklocator.Crawler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by effy on 2018/5/7.
 */
public class HttprequestTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadall() throws Exception {

    }

    @Test
    public void loadBookComment() throws Exception {
        //assertNotNull(Httprequest.loadBookComment(""));
    }

    @Test
    public void loadreply() throws Exception {
        assertNotNull(Httprequest.loadreply("93"));
    }

}