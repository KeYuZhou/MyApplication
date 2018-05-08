package com.example.chirag.librarybooklocator;

import java.util.Comparator;

/**
 * Created by effy on 2018/4/6.
 */

public class ComReply implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Reply x = (Reply) o1;
        Reply y = (Reply) o2;

        return y.date.compareTo(x.date);
    }
}

