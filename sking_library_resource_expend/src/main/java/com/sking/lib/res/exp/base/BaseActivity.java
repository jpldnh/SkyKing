package com.sking.lib.res.exp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

public abstract class BaseActivity extends Activity {

    /**
     * 上下文对象，等同于this
     */
    protected Activity mContext;
    /**
     * 获取传参使用
     */
    protected Intent mIntent;
    /**
     * 输入法管理器
     */
    protected InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mIntent = getIntent();
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        init(savedInstanceState);
    }

    // 初始化三部曲
    private void init(Bundle savedInstanceState) {
        if ((savedInstanceState != null)
                && (savedInstanceState.getSerializable("intent") != null)) {
            mIntent = (Intent) savedInstanceState.getSerializable("intent");
        }
        getExras();
        findView();
        setListener();
    }

    /**
     * 初始化三部曲之：获取传参
     */
    protected abstract void getExras();

    /**
     * 初始化三部曲之：查找控件
     */
    protected abstract void findView();

    /**
     * 初始化三部曲之：设置监听
     */
    protected abstract void setListener();

    /**
     * 是否为null
     */
    protected boolean isNull(String str)
    {
        return str==null||"".equals(str)||"null".equals(str);
    }

}
