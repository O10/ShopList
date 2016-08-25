package com.example.shoplist.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by O10 on 25.08.2016.
 * ViewPager capable of disabling swiping between pages (used when CAB is active)
 */

public class NonSwipeViewPager extends ViewPager {
    boolean shouldSwipe = true;

    public NonSwipeViewPager(Context context) {
        super(context);
    }

    public NonSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public boolean isShouldSwipe() {
        return shouldSwipe;
    }

    public void setShouldSwipe(boolean shouldSwipe) {
        this.shouldSwipe = shouldSwipe;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return shouldSwipe && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return shouldSwipe && super.onTouchEvent(ev);
    }
}
