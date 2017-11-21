package com.sking.lib.res.interfaces;

import android.view.View;

/**
 * resources资源内用到的接口方法，
 * 基本的接口方法请放在jw_frame接口类中
 * <p>
 * Created by 谁说青春不能错 on 2016/4/21.
 */
public class SKRInterfaces {

    /**
     * 回调接口，确定取消按钮无返回值
     *
     * @params index
     * @return
     */
    public interface SKROnItemSelectedListener {
        void onItemSelected(int index);
    }

    /**
     * 回调接口，确定取消按钮无返回值
     *
     * @params top oldTop
     * @return
     */
    public interface SKROnPnButtonClickLitener {
        void onPositiveClick();
        void onNegetiveClick();
    }

    /**
     * 回调接口，确定取消按钮有返回值
     *
     * @params top oldTop
     * @return
     */
    public interface SKROnPnButtonClickLitenerBackString {
        void onPositiveClick(String result);

        void onNegetiveClick(String result);
    }

    /**
     * 滑动监听
     *
     * @params top oldTop
     * @return
     */
    public interface SKROnScrollChangedListener {
        public abstract void onScrollChanged(int start, int end);
    }

    /**
     * 自定义listview左右滑动回调接口
     * <p>
     * *@params top oldTop
     *
     * @return
     */
    public interface SKROnListViewGestureListener {
        public abstract void onItemDownListener(View view, int position);

        public abstract void onLeftScrollListener(View view, int position);

        public abstract void onRightScrollListener(View view, int position);
    }

}
