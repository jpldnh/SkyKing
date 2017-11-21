package com.sking.lib.res.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class SKRGrideView extends GridView {
	public SKRGrideView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SKRGrideView(Context context) {
		super(context);
	}

	public SKRGrideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
