package com.skingapp.commen.framents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sking.lib.base.SKActivityManager;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRUtil;
import com.sking.lib.utils.SKSharedPreferencesUtil;
import com.skingapp.commen.R;
import com.skingapp.commen.activitys.LoginActivity;
import com.skingapp.commen.activitys.SettingActivity;
import com.skingapp.commen.bases.BaseApplication;
import com.skingapp.commen.bases.BaseFragment;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.db.UserDBHelper;

/**
 * 个人中心
 */
public class MineFragment extends BaseFragment {

	private TextView settingView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.fragment_my_center);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findView() {
		settingView = (TextView) findViewById(R.id.ac_center_setting);
	}

	@Override
	protected void setListener() {
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
