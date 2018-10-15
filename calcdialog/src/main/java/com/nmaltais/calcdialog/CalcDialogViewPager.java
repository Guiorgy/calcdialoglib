package com.nmaltais.calcdialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

class CalcDialogViewPager extends ViewPager {
    private boolean allowSwipe = false;

    public CalcDialogViewPager(Context context) {
        super(context);
        setNoScroller();
    }

    public CalcDialogViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setNoScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(allowSwipe){
            return super.onInterceptTouchEvent(event);
        }
        // stop swipe
        setCurrentItem(getCurrentItem());
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(allowSwipe){
            return super.onTouchEvent(event);
        }
        // stop switching pages
        setCurrentItem(getCurrentItem());
        return false;
    }

    @Override
    public boolean arrowScroll(final int direction){
        setCurrentItem(getCurrentItem());
        // stop Dpad swiping
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

    private class NoScroller extends Scroller {
        NoScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }

    public boolean isSwipeAllowed(){
        return allowSwipe;
    }

    public void setAllowSwipe(final boolean allowSwipe){
        this.allowSwipe = allowSwipe;
    }
}
