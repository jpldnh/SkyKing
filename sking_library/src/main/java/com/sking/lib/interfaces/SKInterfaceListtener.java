package com.sking.lib.interfaces;

import android.view.View;

/**
 * 一些基本的接口方法，
 *
 * Created by 谁说青春不能错 on 2016/11/29.
 */

public interface SKInterfaceListtener {

    /**
     * 回调接口无返回值
     *
     * @params top oldTop
     * @return
     */
    public interface SKOnClickLitener {
        void onBackClick();
    }

    /**
     * 回调接口返回类型为String类型接口
     *
     * @params string
     * @return onBackResult
     */
    public abstract interface SKOnStringBackClickListener {
        public abstract void onBackResult(String str);
    }

    /**
     * 回调接口返回类型为Object类型接口
     *
     * @params string
     * @return onBackResult
     */
    public abstract interface SKOnObjectBackClickListener {
        public abstract void onBackResult(Object result);
    }

    /**
     * 回调接口返回类型为view,String类型接口
     *
     * @params view string
     * @return onBackResult
     */
    public abstract interface SKOnViewBackClickListener {
        public abstract void onBackResult(View view, String result);
    }
}
