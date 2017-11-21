package com.sking.lib.res.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sking.lib.interfaces.SKInterfaceListtener;
import com.sking.lib.res.R;
import com.sking.lib.res.adapters.SKRWheelAdapter;
import com.sking.lib.res.interfaces.SKRInterfaces;
import com.sking.lib.res.models.SKRRegionInfo;

import java.util.ArrayList;

/**
 * 基于basewhell单极选择器
 **/
public class SKRWheelViewSinglePicker extends PopupWindow {

    private Context context;
    private Button topCancleButt;
    private Button topConfirmButt;
    private boolean isDismiss = true;
    private RelativeLayout topLayout;
    private TextView topTitle;
    private View mMenuView;
    private SKRWheelView picker_view;
    private int select_state = 0;
    private ArrayList<SKRRegionInfo> info_array;
    private SKInterfaceListtener.SKOnObjectBackClickListener itemsOnClick;

    public SKRWheelViewSinglePicker(Context context, SKInterfaceListtener.SKOnObjectBackClickListener itemsOnClick, ArrayList<SKRRegionInfo> list) {
        super(context);
        this.context = context;
        this.itemsOnClick = itemsOnClick;
        this.info_array = list;
        findView();
        initPopu();
        setListerner();
        setData();
    }

    //初始化
    private void initPopu() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
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
                    if (y < height && isDismiss) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @SuppressLint("NewApi")
    private void findView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.skr_layout_popu_wheel_single_picker, null);
        picker_view = (SKRWheelView) mMenuView.findViewById(R.id.picker_view);
        topCancleButt = (Button) mMenuView.findViewById(R.id.top_cancle_butt);
        topConfirmButt = (Button) mMenuView.findViewById(R.id.top_confirm_butt);
        topTitle = (TextView) mMenuView.findViewById(R.id.top_title_text);
        topLayout = (RelativeLayout) mMenuView.findViewById(R.id.top_linear_layout);
    }

    @SuppressLint("NewApi")
    private void setListerner() {
        //设置按钮监听
        topConfirmButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.onBackResult(info_array.get(select_state));
                dismiss();
            }
        });

        topCancleButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //设置数据
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setData() {
        picker_view.setAdapter(new SKRWheelAdapter(info_array));// 设置显示数据
        picker_view.setCurrentItem(0);// 初始化时显示的数据
        picker_view.setOnItemSelectedListener(new SKRInterfaces.SKROnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                select_state = index;
            }
        });
    }

    /**
     * 显示view
     **/
    public void showPopuAtLocation(View parent, int gravity, int x, int y) {
        showAtLocation(parent, gravity, x, y); //设置layout在PopupWindow中显示的位置
    }

    /**
     * 是否点击消失
     **/
    public void setCancelable(boolean isCancle) {
        isDismiss = isCancle;
    }

    /**
     * 是否可以循环滚动
     **/
    public void setSyclic(boolean iscyclic) {
        picker_view.setCyclic(iscyclic);
    }

    /**
     * 设置标题
     **/
    public void setTitleName(String title) {
        topTitle.setText(title);
    }

    /**
     * 设置标题背景
     **/
    public void setTitleBackground(int resid) {
        topLayout.setBackgroundResource(resid);
    }
}
