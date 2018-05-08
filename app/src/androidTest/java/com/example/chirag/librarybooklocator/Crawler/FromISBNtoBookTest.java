package com.example.chirag.librarybooklocator.Crawler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by effy on 2018/5/7.
 */
public class FromISBNtoBookTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        System.out.println("Before");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("After");
    }

    @Test
    public void getBookByISBN() throws Exception {
        assertNotNull(FromISBNtoBook.getBookByISBN("9787302328810"));
        assertNotNull(FromISBNtoBook.getBookByISBN("9787111555391"));
//        Throwable t = null;
//        try{
//            FromISBNtoBook.getBookByISBN("$^#^#%&");
//        }catch(Exception ex){
//            t = ex;
//        }

        // assertNull(FromISBNtoBook.getBookByISBN("$^#^#%&"));
//        assertNotNull(t);
//assertNull(FromISBNtoBook.getBookByISBN("9787111555391"));
//        assertTrue(t instanceof MalformedURLException);
        //       assertTrue(t instanceof NullPointerException);
        //       expectedEx.expect(MalformedURLException.class);
//        expectedEx.expect(NullPointerException.class);


    }

    @Test
    public void parseXML() throws Exception {
    }

    @Test
    public void getParameter() throws Exception {
    }

}