package com.nmaltais.calcdialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

class CalcDialogViewPager extends ViewPager {

    CalcDialogViewPager(Context context) {
        super(context);
    }

    CalcDialogViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // stop swipe
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // stop switching pages
        return false;
    }

    private void setNoScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new NoScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class NoScroller extends Scroller {
        NoScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}
