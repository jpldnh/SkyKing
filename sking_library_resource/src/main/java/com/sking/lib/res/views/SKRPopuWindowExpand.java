package com.sking.lib.res.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.sking.lib.res.R;
import com.sking.lib.res.interfaces.SKRInterfaces;

/**
 * 注 可以给此pupuwindow添加view填充
 * **/
public class SKRPopuWindowExpand extends PopupWindow {
  
	private Context context;
    private Button submitButton;//底部button
    private SKRScrollView scrollView;
    private View mMenuView,addView;  
    private OnClickListener itemsOnClick;
    private InputMethodManager imm;
	private int putInAnimal;//进出动画
    private boolean isSetScroll,isDismiss;//是否可滑动，点击空白是否可消失

    public SKRPopuWindowExpand(Context context, OnClickListener itemsOnClick, View addView, int putInAnimal, boolean isSetScroll, final boolean dismiss) {
        super(context);  
        this.context = context;
        this.itemsOnClick = itemsOnClick;
        this.addView = addView;
		this.putInAnimal = putInAnimal;
		this.isSetScroll = isSetScroll;
		this.isDismiss = dismiss;
		findView();
        setViewData();
		initPopu();
    }

	//初始化
	private void initPopu()
	{
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.Base_Style_PopuWindow_Bottom);
        this.setAnimationStyle(putInAnimal);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(00000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.bottom_scroll_view).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						if(isDismiss)
						{
							dismiss();
						}
					}
				}
				return true;
			}
		});
	}

    @SuppressLint("NewApi")
	private void findView()
	{
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    	mMenuView = inflater.inflate(R.layout.skr_layout_popu_window_view_expand, null);
    	LinearLayout linearLayout  = (LinearLayout) mMenuView.findViewById(R.id.popu_scroll_layout);
    	linearLayout.addView(addView);
		submitButton = (Button) mMenuView.findViewById(R.id.bottom_submit_butt);
    	scrollView = (SKRScrollView) mMenuView.findViewById(R.id.bottom_scroll_view);
	}
    
    @SuppressLint("NewApi")
	private void setViewData()
    {
    	//打开软键盘
    	imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				itemsOnClick.onClick(v);
				dismiss();
			}
		});
    	if(isSetScroll==true)
    	{
    		scrollView.setOnScrollChangedListener(new SKRInterfaces.SKROnScrollChangedListener() {
    			
    			@Override
    			public void onScrollChanged(int top, int oldTop) {
    				// TODO Auto-generated method stub
    				int buttTop = submitButton.getTop() - top;
    				int crollHeight = scrollView.getHeight()/5*4;
    				if(buttTop>crollHeight)
    				{
						submitButton.setVisibility(View.VISIBLE);
    					Animation anim = AnimationUtils.loadAnimation(context, R.anim.skr_animal_view_show);
						submitButton.startAnimation(anim);
    				}else
    				{
						submitButton.setVisibility(View.INVISIBLE);
    					Animation anim = AnimationUtils.loadAnimation(context, R.anim.skr_animal_view_dismiss);
						submitButton.startAnimation(anim);
    				}
    			}
    		});
    	}else
    	{
			submitButton.setVisibility(View.VISIBLE);
    	}
    }
    @Override
    public void dismiss() {
    	// TODO Auto-generated method stub
    	//关闭软键盘
    	imm.hideSoftInputFromWindow(submitButton.getWindowToken(), 0);
    	super.dismiss();
    }
}  
