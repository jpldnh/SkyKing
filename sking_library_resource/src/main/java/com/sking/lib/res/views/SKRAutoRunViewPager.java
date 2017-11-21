package com.sking.lib.res.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SKRAutoRunViewPager extends ViewPager {

    public SKRAutoRunViewPager(Context context) {
        super(context);
    }

    public SKRAutoRunViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Runnable nextRunnable = new Runnable() {
        @Override
        public void run() {
            PagerAdapter adapter = getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                if (count > 0) {
                    int next = getCurrentItem() + 1;
                    if (next == count)
                        next = 0;
                    setCurrentItem(next, true);
                }
                startNext();
            }
        }
    };

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        stopNext();
        startNext();
    }

    public void startNext() {
        stopNext();
        postDelayed(nextRunnable, 3000);
    }

    public void stopNext() {
        removeCallbacks(nextRunnable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        stopNext();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startNext();
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

}
