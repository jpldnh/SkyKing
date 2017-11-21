package com.skingapp.commen.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseActivity;

/**
 * 完善个人资料
 */
public class PerfectInfoActivity extends BaseActivity {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_comment);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getExras() {

	}

	@Override
	protected void findView() {
		titleText = (TextView) findViewById(R.id.top_title_text);
		titleLeft = (TextView) findViewById(R.id.top_title_left);
		titleRight = (TextView) findViewById(R.id.top_title_right);
	}

	@Override
	protected void setListener() {
		titleText.setText("完善个人信息");
		titleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleRight.setVisibility(View.GONE);
	}


	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {

	}

	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {

	}

	@Override
	protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {

	}

	@Override
	protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {

	}

	@Override
	protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {

	}
}
