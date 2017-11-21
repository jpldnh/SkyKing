package com.skingapp.commen.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRUtil;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseActivity;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.models.StringModel;

/**
 * 注册第一步
 */
public class RegisterFirstActivity extends BaseActivity {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	private EditText usernameEditText;
	private TextView textView;
	private EditText codeEditText;
	private Button sendButton;
	private TextView secondTextView;
	private Button nextButton;
	private TextView areementTextView;
	private CheckBox checkBox;

	private String username;

	private TimeThread timeThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register_first);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getExras() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findView() {
		titleText = (TextView) findViewById(R.id.top_title_text);
		titleLeft = (TextView) findViewById(R.id.top_title_left);
		titleRight = (TextView) findViewById(R.id.top_title_right);

		usernameEditText = (EditText) findViewById(R.id.username);
		textView = (TextView) findViewById(R.id.textview);
		codeEditText = (EditText) findViewById(R.id.code);
		secondTextView = (TextView) findViewById(R.id.second);
		sendButton = (Button) findViewById(R.id.sendcode);
		nextButton = (Button) findViewById(R.id.next);
		areementTextView = (TextView) findViewById(R.id.areement);
		checkBox = (CheckBox) findViewById(R.id.checkbox);
	}

	@Override
	protected void setListener() {
		titleText.setText("注册");
		titleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleRight.setVisibility(View.GONE);
		areementTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, WebViewActivity.class);
				it.putExtra("TITLE_NAME","注册声明");
				it.putExtra("URL_PATH","webview/parm/protocal");
				startActivity(it);
			}
		});
		sendButton.setOnClickListener(new SendButtonListener());
		codeEditText.addTextChangedListener(new OnTextChangeListener());
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNull(username)) {
					showToastLong("请先验证手机号");
					return;
				}
				String code = codeEditText.getText().toString();
				getNetWorker().codeVerify(username, code);
			}
		});
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				checkNextable();
			}
		});

	}

	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {
		BaseHttpInformation information = (BaseHttpInformation) netTask
				.getHttpInformation();
		switch (information) {
		case CLIENT_VERIFY:
			showProgressDialog("正在验证手机号");
			break;
		case CODE_GET:
			showProgressDialog("正在获取验证码");
			break;
		case CODE_VERIFY:
			showProgressDialog("正在验证随机码");
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
		case CLIENT_VERIFY:
		case CODE_GET:
		case CODE_VERIFY:
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
		case CLIENT_VERIFY:
			showToastLong("该手机号已经被注册了");
			break;
		case CODE_GET:
			textView.setText("验证码已发送到 " + SKRUtil.hide(username, "1"));
			textView.setVisibility(View.VISIBLE);
			timeThread = new TimeThread(new TimeHandler(this));
			timeThread.start();
			break;
		case CODE_VERIFY:
			@SuppressWarnings("unchecked")
            SKRArrayBaseResult<StringModel> sResult = (SKRArrayBaseResult<StringModel>) baseResult;
			String tempToken = sResult.getObjects().get(0).getTemp_token();
			Intent it = new Intent(mContext, RegisterSecondActivity.class);
			it.putExtra("username", username);
			it.putExtra("tempToken", tempToken);
			startActivity(it);
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
		case CLIENT_VERIFY:
			username = netTask.getParams().get("username");
			getNetWorker().codeGet(username);
			break;
		case CODE_GET:
			showToastLong(baseResult.getMsg());
			break;
		case CODE_VERIFY:
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
		case CLIENT_VERIFY:
			showToastLong("验证手机号失败");
			break;
		case CODE_GET:
			showToastLong("获取验证码失败");
			break;
		case CODE_VERIFY:
			showToastLong("验证随机码失败");
			break;
		default:
			break;
		}
	}

	private class SendButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String username = usernameEditText.getText().toString();
			if (isNull(username)) {
				showToastLong("请输入手机号");
				return;
			}

			// String mobile = "^[1][3-8]+\\d{9}";
			String mobile = "\\d{11}";// 只判断11位
			if (!username.matches(mobile)) {
				showToastLong("您输入的手机号不正确");
				return;
			}

			getNetWorker().clientVerify(username);

		}

	}

	private class TimeThread extends Thread {
		private int curr;

		private TimeHandler timeHandler;

		public TimeThread(TimeHandler timeHandler) {
			this.timeHandler = timeHandler;
		}

		void cancel() {
			curr = 0;
		}

		@Override
		public void run() {
			curr = 60;
			while (curr > 0) {
				timeHandler.sendEmptyMessage(curr);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// ignore
				}
				curr--;
			}
			timeHandler.sendEmptyMessage(-1);
		}
	}

	private static class TimeHandler extends Handler {
		RegisterFirstActivity activity;

		public TimeHandler(RegisterFirstActivity activity) {
			this.activity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				activity.sendButton.setText("重新发送");
				activity.sendButton.setVisibility(View.VISIBLE);
				break;
			default:
				activity.sendButton.setVisibility(View.GONE);
				activity.secondTextView.setText("" + msg.what);
				break;
			}
		}
	}

	private class OnTextChangeListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			checkNextable();
		}
	}

	private void checkNextable() {
		String code = codeEditText.getText().toString();
		// code.matches("\\d{4}$")
		boolean c = !isNull(code) && checkBox.isChecked();
		if (c) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
	}

	@Override
	protected void onDestroy() {
		if (timeThread != null)
			timeThread.cancel();
		super.onDestroy();
	}
}
