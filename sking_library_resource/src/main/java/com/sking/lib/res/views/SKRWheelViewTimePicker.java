package com.sking.lib.res.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.sking.lib.interfaces.SKInterfaceListtener;
import com.sking.lib.res.R;
import com.sking.lib.res.adapters.SKRWheelAdapter;
import com.sking.lib.res.interfaces.SKRInterfaces;
import com.sking.lib.res.models.SKRRegionInfo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 依赖BaseWheel实现时间选择器
 **/

public class SKRWheelViewTimePicker extends PopupWindow {

    private Context context;
    private Button topCancleButt;
    private Button topConfirmButt;
    private RelativeLayout topLayout;
    private View mMenuView;
    private boolean isDismiss = true;
    private SKRWheelView picker_day, picker_month, picker_year;
    private int startYear = -1;//0代表从当前年份开始,else 输入年份如2016
    private int yearCount = 20;//数量
    private ArrayList<SKRRegionInfo> day_array;
    private ArrayList<SKRRegionInfo> month_array;
    private ArrayList<SKRRegionInfo> year_array;
    private int select_day = 0, select_month = 0, select_year = 2016;
    private SKInterfaceListtener.SKOnStringBackClickListener itemsOnClick;

    public SKRWheelViewTimePicker(Context context, SKInterfaceListtener.SKOnStringBackClickListener itemsOnClick) {
        super(context);
        this.context = context;
        this.itemsOnClick = itemsOnClick;
        findView();
        initPopu();
        setListerner();
    }

    //初始化
    private void initPopu() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
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
        mMenuView = inflater.inflate(R.layout.skr_layout_popu_wheel_time_picker, null);
        picker_year = (SKRWheelView) mMenuView.findViewById(R.id.picker_first);
        picker_month = (SKRWheelView) mMenuView.findViewById(R.id.picker_second);
        picker_day = (SKRWheelView) mMenuView.findViewById(R.id.picker_third);
        topCancleButt = (Button) mMenuView.findViewById(R.id.top_cancle_butt);
        topConfirmButt = (Button) mMenuView.findViewById(R.id.top_confirm_butt);
        topLayout = (RelativeLayout) mMenuView.findViewById(R.id.top_linear_layout);
    }

    @SuppressLint("NewApi")
    private void setListerner() {
        topConfirmButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.onBackResult(year_array.get(picker_year.getCurrentItem()).getName() + "-" + month_array.get(picker_month.getCurrentItem()).getName() + "-" + day_array.get(picker_day.getCurrentItem()).getName());
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

    /**
     * 显示view
     **/
    public void showPopuAtLocation(View parent, int gravity, int x, int y) {
        setData();
        showAtLocation(parent, gravity, x, y); //设置layout在PopupWindow中显示的位置
    }

    //设置数据
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setData() {
        Calendar calendar = Calendar.getInstance();
        select_year = calendar.get(Calendar.YEAR);
        select_month = calendar.get(Calendar.MONTH) + 1;
        select_day = calendar.get(Calendar.DAY_OF_MONTH);

        year_array = getYearArray();
        picker_year.setAdapter(new SKRWheelAdapter(year_array));// 设置显示数据
        picker_year.setCurrentItem(0);// 初始化时显示的数据
        picker_year.setLabel("年");
        picker_year.setOnItemSelectedListener(new SKRInterfaces.SKROnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                select_year = Integer.parseInt(year_array.get(index).getName());
                int new_position = picker_day.getCurrentItem();//上一个opt3的选中位置
                new_position = new_position > day_array.size() - 1 ? day_array.size() - 1 : new_position;
                day_array.clear();
                day_array = getDayArray(select_month, select_year);
                picker_day.setAdapter(new SKRWheelAdapter(day_array));
                picker_day.setCurrentItem(new_position);
            }
        });

        month_array = getMonthArray();
        picker_month.setAdapter(new SKRWheelAdapter(month_array));// 设置显示数据
        if (startYear != -1)
            picker_month.setCurrentItem(0);// 初始化时显示的数据
        else
            picker_month.setCurrentItem(select_month - 1);
        picker_month.setLabel("月");
        picker_month.setOnItemSelectedListener(new SKRInterfaces.SKROnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                Log.v("index", index + "");
                select_month = Integer.parseInt(month_array.get(index).getName());
                int new_position = picker_day.getCurrentItem();//上一个opt3的选中位置
                new_position = new_position > day_array.size() - 1 ? day_array.size() - 1 : new_position;
                day_array.clear();
                day_array = getDayArray(index + 1, select_year);
                picker_day.setAdapter(new SKRWheelAdapter(day_array));
                picker_day.setCurrentItem(new_position);
            }
        });
        if (startYear != -1)
            day_array = getDayArray(select_month, select_year);
        else
            day_array = getDayArray(select_month, startYear);
        picker_day.setAdapter(new SKRWheelAdapter(day_array));// 设置显示数据
        if (startYear != -1)
            picker_day.setCurrentItem(0);// 初始化时显示的数据、
        else
            picker_day.setCurrentItem(select_day - 1);
        picker_day.setLabel("日");
        picker_day.setOnItemSelectedListener(new SKRInterfaces.SKROnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                select_day = Integer.parseInt(day_array.get(index).getName());
            }
        });
    }

    public ArrayList<SKRRegionInfo> getDayArray(int month, int year) {
        String day = "01";
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        int nowMothDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        ArrayList<SKRRegionInfo> list = new ArrayList<SKRRegionInfo>();
        for (int i = 0; i < nowMothDays; i++) {
            if (i < 9)
                day = "0" + (i + 1);
            else
                day = (i + 1) + "";
            SKRRegionInfo regionInfo = new SKRRegionInfo(i, 2, day);
            list.add(regionInfo);
        }
        return list;
    }

    private ArrayList<SKRRegionInfo> getMonthArray() {
        String month = "01";
        ArrayList<SKRRegionInfo> list = new ArrayList<SKRRegionInfo>();
        for (int i = 0; i < 12; i++) {
            if (i < 9)
                month = "0" + (i + 1);
            else
                month = (i + 1) + "";
            SKRRegionInfo regionInfo = new SKRRegionInfo(i, 1, month);
            list.add(regionInfo);
        }
        return list;
    }

    private ArrayList<SKRRegionInfo> getYearArray() {
        ArrayList<SKRRegionInfo> list = new ArrayList<SKRRegionInfo>();
        for (int i = 0; i < yearCount; i++) {
            SKRRegionInfo regionInfo = null;
            if (startYear != -1)//自定义开始年份
                regionInfo = new SKRRegionInfo(i, 0, (startYear + i) + "");
            else
                regionInfo = new SKRRegionInfo(i, 0, (select_year + i) + "");
            list.add(regionInfo);
        }
        return list;
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
        picker_year.setCyclic(iscyclic);
        picker_month.setCyclic(iscyclic);
        picker_day.setCyclic(iscyclic);
    }

    /**
     * 设置开始年份
     **/
    public void setStartYear(int startyear) {
        startYear = startyear;
    }

    /**
     * 设置设置数量
     **/
    public void setYearCount(int count) {
        yearCount = count;
    }

    /**
     * 设置后缀名称
     **/
    public void setLable(String first, String second, String third) {
        picker_year.setLabel(first);
        picker_month.setLabel(second);
        picker_day.setLabel(third);
    }

    /**
     * 设置标题背景
     **/
    public void setTitleBackground(int resid) {
        topLayout.setBackgroundResource(resid);
    }

}  
