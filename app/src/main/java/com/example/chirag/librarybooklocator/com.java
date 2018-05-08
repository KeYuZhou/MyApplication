package com.example.chirag.librarybooklocator;

import java.util.Comparator;

/**
 * Created by effy on 2018/4/6.
 */

public class com implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        BookComment x = (BookComment) o1;
        BookComment y = (BookComment) o2;
        return y.date.compareTo(x.date);
    }
}
