package com.sking.lib.res.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.sking.lib.res.R;


/**
 * 配合侧滑删除使用
 * Created by 谁说青春不能错 on 2016/3/16.
 */
public class SKRScrollLinearLayout extends LinearLayout {

    private int offset = 0;

    public SKRScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SKR_ScrollLinarLayoutView);
        offset = (int)a.getDimension(R.styleable.SKR_ScrollLinarLayoutView_frame_scrolloffset, 200);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    private Scroller mScroller;

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public void snapToScreen() {
        int curscrollerx = getScrollX();
        mScroller.startScroll(curscrollerx, 0, offset - curscrollerx, 0, offset+10);
        invalidate();
    }

    public int getOffset()
    {
        return offset;
    }

    public void resetLinearLayout()
    {
        int curscrollerx = getScrollX();
        mScroller.startScroll(curscrollerx, 0, 0 - curscrollerx, 0, offset);
        invalidate();
    }

}