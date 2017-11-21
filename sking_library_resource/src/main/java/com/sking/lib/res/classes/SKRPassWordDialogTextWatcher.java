package com.sking.lib.res.classes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 谁说青春不能错 on 2016/12/6.
 */

public class SKRPassWordDialogTextWatcher implements TextWatcher {

    private int state;
    private Context mContext;
    private int countNum;
    private View lastview, nextView;

    public SKRPassWordDialogTextWatcher(Context mContext, int state, View lastview, View nextView) {
        this.state = state;
        this.mContext = mContext;
        this.lastview = lastview;
        this.nextView = nextView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        // TODO Auto-generated method stub
        countNum = count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (state) {
            case 1:
                if (countNum > 0) {
                    nextView.requestFocus();
                } else {
                    lastview.requestFocus();
                }

                break;
            case 2:
                if (countNum > 0) {
                    nextView.requestFocus();
                } else {
                    lastview.requestFocus();
                }
                break;
            case 3:
                if (countNum > 0) {
                    nextView.requestFocus();
                } else {
                    lastview.requestFocus();
                }
                break;
            case 4:
                if (countNum > 0) {
                    nextView.requestFocus();
                } else {
                    lastview.requestFocus();
                }
                break;
            case 5:
                if (countNum > 0) {
                    nextView.requestFocus();
                } else {
                    lastview.requestFocus();
                }
                break;
            case 6:
                InputMethodManager mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(lastview.getWindowToken(), 0);
                break;
        }
    }
}
