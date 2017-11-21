package com.clubank.tpy.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.bases.BaseActivity;
import com.clubank.tpy.bases.BaseHttpInformation;
import com.clubank.tpy.models.User;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;

/**
 * 意见反馈页面
 */
public class AdviceActivity extends BaseActivity {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_advice);
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

		editText = (EditText) findViewById(R.id.edittext);
	}

	@Override
	protected void setListener() {
		titleText.setText("意见反馈");
		titleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleRight.setText("提交");
		titleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = editText.getText().toString();
				if (isNull(content)) {
					showToastLong("请输入您的意见");
					return;
				}
				User user = getApplicationContext().getUser();
				getNetWorker().adviceAdd(user.getToken(), content);
			}
		});
	}


	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {
		BaseHttpInformation information = (BaseHttpInformation) netTask
				.getHttpInformation();
		switch (information) {
		case ADVICE_ADD:
			showProgressDialog("正在提交您的宝贵意见");
			break;
		default:
			break;
		}
	}

	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {
		BaseHttpInformation information = (BaseHttpInformation) netTask
				.getHttpInformation();
		switch (information) {
		case ADVICE_ADD:
			cancelProgressDialog();
			break;
		default:
			break;
		}
	}

	@Override
	protected void networkRequestSuccess(SKRNetTask netTask,
		SKRBaseResult baseResult) {
		BaseHttpInformation information = (BaseHttpInformation) netTask
				.getHttpInformation();
		switch (information) {
		case ADVICE_ADD:
			showToastLong(baseResult.getMsg());
			titleText.postDelayed(new Runnable() {

				@Override
				public void run() {
					finish();
				}
			}, 1000);
			break;
		default:
			break;
		}

	}

	@Override
	protected void networkRequestParseFailed(SKRNetTask netTask,
			SKRBaseResult baseResult) {
		BaseHttpInformation information = (BaseHttpInformation) netTask
				.getHttpInformation();
		switch (information) {
		case ADVICE_ADD:
			showToastLong(baseResult.getMsg());
			break;
		default:
			break;
		}
	}

	@Override
	protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
		BaseHttpInformation information = (BaseHttpInformation) netTask
				.getHttpInformation();
		switch (information) {
		case ADVICE_ADD:
			showToastLong("意见提交失败");
			break;
		default:
			break;
		}
	}



}
