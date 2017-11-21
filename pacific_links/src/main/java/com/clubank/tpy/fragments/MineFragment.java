package com.clubank.tpy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.clubank.tpy.R;
import com.clubank.tpy.activitys.LoginActivity;
import com.clubank.tpy.activitys.SettingActivity;
import com.clubank.tpy.bases.BaseApplication;
import com.clubank.tpy.bases.BaseFragment;
import com.clubank.tpy.bases.BaseHttpInformation;
import com.clubank.tpy.db.UserDBHelper;
import com.sking.lib.base.SKActivityManager;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRUtil;
import com.sking.lib.utils.SKSharedPreferencesUtil;

/**
 * 个人中心
 */
public class MineFragment extends BaseFragment {
	private TextView titleText;
	private TextView titleLeft;
	private TextView titleRight;

	private TextView settingView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.fragment_my_center);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findView() {
		titleText = (TextView) findViewById(R.id.top_title_text);
		titleLeft = (TextView) findViewById(R.id.top_title_left);
		titleRight = (TextView) findViewById(R.id.top_title_right);
		settingView = (TextView) findViewById(R.id.ac_center_setting);
	}

	@Override
	protected void setListener() {
		titleLeft.setVisibility(View.GONE);
		titleText.setText("个人中心");
		titleRight.setVisibility(View.GONE);

		//系统设置
		settingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(getActivity(), SettingActivity.class);
				startActivity(it);
			}
		});


	}
	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGINOUT:
			showProgressDialog("正在注销");
			break;
		default:
			break;
		}
	}

	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGINOUT:
			cancelProgressDialog();
			break;
		default:
			break;
		}
	}

	@Override
	protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGINOUT:
			cancellationSuccess();
			break;
		default:
			break;
		}
	}

	private void cancellationSuccess() {
		UserDBHelper dbHelper = new UserDBHelper(getActivity());
		dbHelper.clear();
		// 清空登录信息
		BaseApplication.getInstance().setUser(null);
		SKSharedPreferencesUtil.save(getActivity(), "username", "");// 清空用户名
		SKSharedPreferencesUtil.save(getActivity(), "password", "");// 青空密码
		SKRUtil.setThirdSave(getActivity(), false);// 将第三方登录标记置为false
		SKActivityManager.finishAll();
		Intent it = new Intent(getActivity(), LoginActivity.class);
		startActivity(it);
	}

	@Override
	protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGINOUT:
			 cancellationSuccess();
			log_i("退出登录失败");
			break;
		default:
			break;
		}
	}

	@Override
	protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
		BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
		switch (information) {
		case CLIENT_LOGINOUT:
			 cancellationSuccess();
			log_i("退出登录失败");
			break;
		default:
			break;
		}
	}






}
