package com.clubank.tpy.activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.bases.BaseActivity;
import com.clubank.tpy.bases.BaseHttpInformation;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;

/**
 * 注册第二步
 */
public class RegisterSecondActivity extends BaseActivity {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	private EditText passwordEditText;
	private EditText repeatEditText;
	private Button nextButton;

	private String username;
	private String tempToken;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register_second);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getExras() {
		username = mIntent.getStringExtra("username");
		tempToken = mIntent.getStringExtra("tempToken");
		log_i("username = " + username);
		log_i("tempToken = " + tempToken);
	}


	@Override
	protected void findView() {
		titleText = (TextView) findViewById(R.id.top_title_text);
		titleLeft = (TextView) findViewById(R.id.top_title_left);
		titleRight = (TextView) findViewById(R.id.top_title_right);

		passwordEditText = (EditText) findViewById(R.id.password);
		repeatEditText = (EditText) findViewById(R.id.repeat);
		nextButton = (Button) findViewById(R.id.next);
	}

	@Override
	protected void setListener() {
		titleText.setText("设置密码");
		titleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleRight.setVisibility(View.GONE);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent it = new Intent(mContext, Register2Activity.class);
//				it.putExtra("username", username);
//				it.putExtra("password", password);
//				it.putExtra("tempToken", tempToken);
//				startActivity(it);
			}
		});
		passwordEditText.addTextChangedListener(new OnTextChangeListener());
		repeatEditText.addTextChangedListener(new OnTextChangeListener());
	}

	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void networkRequestSuccess(SKRNetTask netTask,
			SKRBaseResult baseResult) {
		BaseHttpInformation information = (BaseHttpInformation) netTask
				.getHttpInformation();
		switch (information) {
		case INIT:
			break;
		case CLIENT_LOGIN:
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
		case INIT:
			// getInitFailed();
			break;
		case CLIENT_LOGIN:
			// toLogin();
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
		case INIT:
			// getInitFailed();
			break;
		case CLIENT_LOGIN:
			// toLogin();
			break;
		default:
			break;
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
		password = passwordEditText.getText().toString();
		String repeat = repeatEditText.getText().toString();

		boolean c = !isNull(password) && password.equals(repeat);
		if (c) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
	}
}
