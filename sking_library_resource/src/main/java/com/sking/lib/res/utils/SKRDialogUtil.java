package com.sking.lib.res.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sking.lib.base.SKObject;
import com.sking.lib.interfaces.SKInterfaceListtener;
import com.sking.lib.res.R;
import com.sking.lib.res.interfaces.SKRInterfaces;

public class SKRDialogUtil extends SKObject {
    private Dialog mDialog;
    private ViewGroup mContent;
    private TextView mTextView;
    private Button leftButton;
    private Button rightButton;
    private LinearLayout rootView;
    private Button confirmButton;
    private LinearLayout bottomLayout;
    private SKRInterfaces.SKROnPnButtonClickLitener buttonListener;
    private SKInterfaceListtener.SKOnClickLitener confirmOnListener;

    public SKRDialogUtil(Context context) {
        mDialog = new Dialog(context, R.style.SKR_Style_Dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skr_layout_dialog_botton, null);
        mContent = (ViewGroup) view.findViewById(R.id.dialog_content);
        mTextView = (TextView) view.findViewById(R.id.dialog_textview);
        confirmButton = (Button) view.findViewById(R.id.dialog_confirm_button);
        bottomLayout = (LinearLayout) view.findViewById(R.id.dialog_bottom_layout);
        leftButton = (Button) view.findViewById(R.id.dialog_left);
        rightButton = (Button) view.findViewById(R.id.dialog_right);
        rootView = (LinearLayout) view.findViewById(R.id.dialog_root_view);
        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (confirmOnListener != null)
                    confirmOnListener.onBackClick();
            }
        });

        leftButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (buttonListener != null)
                    buttonListener.onNegetiveClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (buttonListener != null)
                    buttonListener.onPositiveClick();
            }
        });
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
    }

    /**
     * 给弹框添加自定义View
     *
     * @param v 自定义View
     */
    public void setView(View v) {
        mContent.removeAllViews();
        mContent.addView(v);
    }

    /**
     * 显示只有确定按钮的dialog
     */
    public void show() {
        confirmButton.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);
        if (mDialog != null)
            mDialog.show();
    }

    /**
     * 显示只有确定，取消按钮的dialog
     */
    public void showExpand() {
        confirmButton.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        if (mDialog != null)
            mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setText(int textID) {
        mTextView.setText(textID);
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    public void setTextBackground(int resId) {
        mTextView.setBackgroundResource(resId);
    }

    public void setConfirmButtonText(int textID) {
        confirmButton.setText(textID);
    }

    public void setConfirmButtonText(String text) {
        confirmButton.setText(text);
    }

    public void setConfirmButtonTextColor(int color) {
        confirmButton.setTextColor(color);
    }

    public void setConfirmButtonBackground(int resId) {
        confirmButton.setBackgroundResource(resId);
    }

    public void setLeftButtonText(int textID) {
        leftButton.setText(textID);
    }

    public void setLeftButtonText(String text) {
        leftButton.setText(text);
    }

    public void setLeftButtonTextColor(int color) {
        leftButton.setTextColor(color);
    }

    public void setLeftButtonBackground(int resId) {
        leftButton.setBackgroundResource(resId);
    }

    public void setRightButtonText(int textID) {
        rightButton.setText(textID);
    }

    public void setRightButtonText(String text) {
        rightButton.setText(text);
    }

    public void setRightButtonTextColor(int color) {
        rightButton.setTextColor(color);
    }

    public void setRightButtonBackground(int resId) {
        rightButton.setBackgroundResource(resId);
    }

    public void setRootViewBackground(int resId) {
        rootView.setBackgroundResource(resId);
    }

    public SKRInterfaces.SKROnPnButtonClickLitener getButtonOnClickListener() {
        return buttonListener;
    }

    public void setButtonOnclickListener(SKRInterfaces.SKROnPnButtonClickLitener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public void setButtonOnclickListener(SKInterfaceListtener.SKOnClickLitener onclickListener) {
        this.confirmOnListener = onclickListener;
    }

    public SKInterfaceListtener.SKOnClickLitener getConfirmButtonOnClickListener() {
        return confirmOnListener;
    }

    public void setCancelable(boolean isCancle)
    {
        mDialog.setCancelable(isCancle);
    }
}
