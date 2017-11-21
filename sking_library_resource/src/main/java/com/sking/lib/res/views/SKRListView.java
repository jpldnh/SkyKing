package com.sking.lib.res.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SKRListView extends ListView {

	//1.建立MyXtomListView 继承子XtomListView
	//2.建立一全局标志位，在onMeasure()方法中设置标志位为ture(该控件正在计算大小中不可进行图像载入)，在onLayout()方法中设置为false.
	private boolean isOnMeasure;
	public SKRListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SKRListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SKRListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public SKRListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
		setOnMeasure(true);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// Log.d("onLayout", "onLayout");
		setOnMeasure(false);
		super.onLayout(changed, l, t, r, b);
	}

	public boolean isOnMeasure() {
		return isOnMeasure;
	}

	public void setOnMeasure(boolean isOnMeasure) {
		this.isOnMeasure = isOnMeasure;
	}

}
