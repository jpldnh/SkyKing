package com.jwapp.demo;

import android.os.Bundle;
import android.widget.TextView;

import com.sking.lib.res.exp.base.BaseActivity;

/**
 * 新控件Activity
 */
public class NewWidgetActivity extends BaseActivity {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		setContentView(R.layout.activity_coordinatorlayout);
		setContentView(R.layout.activity_constraintlayout);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getExras() {

	}

	@Override
	protected void findView() {
	}

	@Override
	protected void setListener() {
	}



}
