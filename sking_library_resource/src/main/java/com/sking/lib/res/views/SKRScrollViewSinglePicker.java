package com.sking.lib.res.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.sking.lib.res.R;
import com.sking.lib.utils.SKTransfromUtil;

/**
 * 注 动态添加button,可选择圆角，直角两种样式
 * 关闭按钮颜色跟随主题，可以修改替换bg_button_popuwindow_cancle.xml，base_bg_button_popuwindow_cancle_r.xml文件更改关闭按钮样式
 * **/

public class SKRScrollViewSinglePicker extends PopupWindow {

    private Button btn_cancel;
    private ScrollView scrollView;
    private LinearLayout layout_body;
    private View mMenuView;
    private OnClickListener itemsOnClick;
    private Context context;
    private String[] arrys;
    private boolean isDismiss = true;
    private int styleType = 0;//1直角布局,0圆角布局

    public SKRScrollViewSinglePicker(Context context, OnClickListener itemsOnClick, String[] arrys) {
        super(context);
        this.context = context;
        this.itemsOnClick = itemsOnClick;
        this.arrys = arrys;
//        findView();
//        addButtonView();
//        initPopuWindow();
    }


    private void initPopuWindow() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.SKR_Style_PopuWindow_Bottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        if (isDismiss)
                            dismiss();
                    }
                }
                return true;
            }
        });
    }

    //更新数据
    public void notifyDataSetChanged(int id, String str) {
        Button butt = (Button) layout_body.findViewById(0);
        butt.setText(str);
    }

    /**
     * 是否点击消失
     * **/
    public void setCancelable(boolean isCancle)
    {
        isDismiss = isCancle;
    }


    /**
     * 设置关闭按钮样式
     **/
    public void setCancleButtonBackground(int resid) {
        btn_cancel.setBackgroundResource(resid);
    }

    /**
     * 圆角样式
     * **/
    public void showConnerlayoutStyle(View parent,int gravity,int x,int y)
    {
        styleType = 0;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.skr_layout_popu_scroll_single_picker_conner, null);
        findView();
        addButtonView();
        initPopuWindow();
        showAtLocation(parent,gravity, x, y); //设置layout在PopupWindow中显示的位置
    }

    /**
     * 直角布局
     **/
    public void showRightAnglelayoutStyle(View parent,int gravity,int x,int y)
    {
        styleType = 1;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.skr_layout_popu_scroll_single_picker, null);
        findView();
        addButtonView();
        initPopuWindow();
        showAtLocation(parent,gravity, x, y); //设置layout在PopupWindow中显示的位置
    }

    @SuppressLint("NewApi")
    private void findView() {
        scrollView = (ScrollView) mMenuView.findViewById(R.id.popu_scroll_view);
        layout_body = (LinearLayout) mMenuView.findViewById(R.id.bottom_layout_body);
        btn_cancel = (Button) mMenuView.findViewById(R.id.botom_butt_negitive);
    }

    @SuppressLint("ResourceAsColor")
    private void addButtonView() {
        for (int i = 0; i < arrys.length; i++) {
            Button button = new Button(context);
            button.setId(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            button.setPadding(0, 30, 0, 30);
            button.setLayoutParams(params);
            button.setBackgroundResource(R.drawable.skr_bg_button_white);
            button.setText(arrys[i]);
            button.setTextSize(16);
            button.setTextColor(Color.parseColor("#666666"));
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    itemsOnClick.onClick(v);
                }
            });
            layout_body.addView(button);
            if (i < arrys.length - 1) {
                View lineView = new View(context);
                lineView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
                lineView.setBackgroundColor(Color.parseColor("#f7f6f6"));
                layout_body.addView(lineView);
            }
        }
        if (arrys.length < 5) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            if(styleType==0)
            {
                layoutParams.leftMargin = SKTransfromUtil.dipTopx(context,15);
                layoutParams.rightMargin = SKTransfromUtil.dipTopx(context,15);
            }
            scrollView.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, SKTransfromUtil.dipTopx(context,300));
            if(styleType==0)
            {
                layoutParams.leftMargin = SKTransfromUtil.dipTopx(context,15);
                layoutParams.rightMargin = SKTransfromUtil.dipTopx(context,15);
            }
            scrollView.setLayoutParams(layoutParams);
        }
        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                itemsOnClick.onClick(v);
            }
        });
    }
}
