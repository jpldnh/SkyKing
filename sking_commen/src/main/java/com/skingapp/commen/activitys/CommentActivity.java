package com.skingapp.commen.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseActivity;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.models.User;

/**
 * 公共Activity
 */
public class CommentActivity extends BaseActivity{
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
		titleText.setText("公共Activity");
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
		BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
			case CLIENT_GET:
				showProgressDialog("加载中...");
				break;
			default:
				break;
		}
	}

	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {
		BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
			case CLIENT_GET:
				cancelProgressDialog();
				break;
			default:
				break;
		}
	}

	@Override
	protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
		BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
			case CLIENT_GET:
				SKRArrayBaseResult<User> user = (SKRArrayBaseResult<User>) baseResult;
				break;
			default:
				break;
		}
	}

	@Override
	protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {
		BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
			case CLIENT_GET:
				showToastLong(baseResult.getMsg());
				break;
			default:
				break;
		}
	}

	@Override
	protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
		BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
			case CLIENT_GET:
				showToastLong("请求失败请重试！");
				break;
			default:
				break;
		}
	}
}
