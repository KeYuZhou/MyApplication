package com.example.chirag.slidingtabsusingviewpager;

/**
 * Created by effy on 2018/4/8.
 */
public interface SnappyScrollCalculator {
    int computeScrollToItemIndex(int velocityX, int velocityY);
}