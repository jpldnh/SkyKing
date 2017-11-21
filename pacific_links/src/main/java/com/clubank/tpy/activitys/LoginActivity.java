package com.clubank.tpy.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.bases.BaseActivity;
import com.clubank.tpy.bases.BaseHttpInformation;
import com.clubank.tpy.models.User;
import com.sking.lib.base.SKActivityManager;
import com.sking.lib.config.SKConfig;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.utils.SKMd5Util;
import com.sking.lib.utils.SKPermissionUtil;
import com.sking.lib.utils.SKSharedPreferencesUtil;
import com.sking.lib.special.umen.utils.UmengEventUtils;
import com.sking.lib.special.umen.utils.UmengUtils;



public class LoginActivity extends BaseActivity {

	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;
	private Button login_butt;
	private EditText login_user;
	private EditText longin_pass;
	private TextView forget_pass;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
        setSwipEnable(false);//单独关闭某个页面的侧滑返回
		super.onCreate(savedInstanceState);
		requestPermission();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		titleText = (TextView) findViewById(R.id.top_title_text);
		titleLeft = (TextView) findViewById(R.id.top_title_left);
		titleRight = (TextView) findViewById(R.id.top_title_right);
		login_user = (EditText)findViewById(R.id.login_user);
		longin_pass = (EditText)findViewById(R.id.login_password);
		login_butt = (Button)findViewById(R.id.login_my_logbutt);
		forget_pass = (TextView)findViewById(R.id.login_forget_pass);
	}

	@Override
	protected void getExras() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		titleText.setText("登录");
		titleLeft.setVisibility(View.GONE);
		login_butt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myLogin();
			}
		});
		titleRight.setText("注册");
		titleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,RegisterFirstActivity.class);
				startActivity(intent);
			}
		});

		forget_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(LoginActivity.this,ForgetPassWordActivity.class);
//				StartActivity(intent);
			}
		});

	}

	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	private void myLogin() {

		String username = login_user.getText().toString();
		String userpass = longin_pass.getText().toString();
		getNetWorker().clientLogin(username, SKMd5Util.getMd5(SKConfig.DATAKEY + SKMd5Util.getMd5(userpass)));
	}


	//网络请求发送之前的调用的方法
	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {
		// TODO Auto-generated method stub
		BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGIN:
			showProgressDialog("正在登录...");
			break;
		default:
			break;
		}
	}

	//发送请求后调用
	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {
		// TODO Auto-generated method stub
		BaseHttpInformation information = (BaseHttpInformation) (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGIN:
			cancelProgressDialog();
			
			break;
		default:
			break;
		}
	}

	//请求成功，处理返回值
	@Override
	protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
		// TODO Auto-generated method stub
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGIN:
			setResult(RESULT_OK);
			@SuppressWarnings("unchecked")
//			HemaArrayResult<ResponseMsg> rResult = (HemaArrayResult<ResponseMsg>) baseResult;
//			ArrayList<ResponseMsg> replies = rResult.getObjects();
            SKRArrayBaseResult<User> uResult = (SKRArrayBaseResult<User>) baseResult;
			User user = uResult.getObjects().get(0);
			getApplicationContext().setUser(user);
			String username = netTask.getParams().get("username");
			String password = netTask.getParams().get("password");
			SKSharedPreferencesUtil.save(mContext, "username", username);
			SKSharedPreferencesUtil.save(mContext, "password", password);
			SKActivityManager.finishAll();
			//友盟统计
			UmengUtils.loginIn(user.getId());
			UmengEventUtils.loginEvent(mContext,user.getId(),username);
			Intent it = new Intent(this, MainActivity.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}

	
	//请求失败，处理失败信息
	@Override
	protected void networkRequestParseFailed(SKRNetTask netTask,
			SKRBaseResult baseResult) {
		// TODO Auto-generated method stub
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGIN:
//			showLongToast(baseResult.getMsg());

			break;
		default:
			break;
		}
		
	}

	@Override
	protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
		// TODO Auto-generated method stub
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGIN:
			showToastLong(failedType+"");
			break;
		default:
			break;
		}
	}

	/**
	 * 获取6.0动态权限
	 */
	public void requestPermission()
	{
		SKPermissionUtil.requesetPermission(this,new String[]{Manifest.permission.GET_ACCOUNTS,
				Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,
				Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE});
	}

	/**
	 * 授权回调
	 */
	@Override
	public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		SKPermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}
}
