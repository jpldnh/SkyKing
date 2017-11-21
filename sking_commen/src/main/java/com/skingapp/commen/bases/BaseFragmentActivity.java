package com.skingapp.commen.bases;

import android.content.Intent;

import com.sking.lib.base.SKActivityManager;
import com.sking.lib.net.SKNetWorker;
import com.sking.lib.res.bases.SKRFragmentActivity;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.bases.SKRNetWorker;
import com.sking.lib.res.interfaces.SKRNetTaskCallBackListener;
import com.sking.lib.res.results.SKRBaseResult;
import com.skingapp.commen.activitys.LoginActivity;
import com.skingapp.commen.models.User;


/**
 * 
 */
public abstract class BaseFragmentActivity extends SKRFragmentActivity {

	private SKRNetTaskCallBackListener netTaskCallBackListener;


//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		PushAgent.getInstance(mContext).onAppStart();//启用友盟统计
//		MobclickAgent.setDebugMode(true);//启用错误日志
//	}

	@Override
	protected SKRNetWorker initNetWorker() {
		return new BaseNetWorker(mContext);
	}

	@Override
	public BaseNetWorker getNetWorker() {
		return (BaseNetWorker) super.getNetWorker();
	}

	@Override
	public BaseApplication getApplicationContext() {
		return (BaseApplication) super.getApplicationContext();
	}

	@Override
	public boolean onAutoLoginFailed(SKRNetWorker netWorker,
									 SKRNetTask netTask, int failedType, SKRBaseResult baseResult) {
		switch (failedType) {
		case 0:// 服务器处理失败
			int error_code = baseResult.getError_code();
			switch (error_code) {
			case 102:// 密码错误
				SKActivityManager.finishAll();
				Intent it = new Intent(mContext, LoginActivity.class);
				startActivity(it);
				return true;
			default:
				break;
			}
		case SKNetWorker.FAILED_HTTP:// 网络异常
		case SKNetWorker.FAILED_DATAPARSE:// 数据异常
		case SKNetWorker.FAILED_NONETWORK:// 无网络
			break;
		}
		return false;
	}
	// ------------------------2017.08.24 update ---------------------------
	@Override
	protected void networkRequestBefore(SKRNetTask netTask) {
		netTaskCallBackListener.networkRequestBefore(netTask);
	}

	@Override
	protected void networkRequestAfter(SKRNetTask netTask) {
		netTaskCallBackListener.networkRequestAfter(netTask);
	}

	@Override
	protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
		netTaskCallBackListener.networkRequestSuccess(netTask,baseResult);
	}

	@Override
	protected void networkRequestParseFailed(SKRNetTask netTask, SKRBaseResult baseResult) {
		netTaskCallBackListener.networkRequestParseFailed(netTask,baseResult);
	}

	@Override
	protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
		netTaskCallBackListener.networkRequestExecuteFailed(netTask,failedType);
	}

	/**
	 * 判断用户是否为空
	 *
	 * @return 用户为空返回true, else false
	 */
	protected boolean isNullUser() {
		return getApplicationContext().getUser() == null ? true : false;
	}

	/**
	 * 获取用户
	 *
	 * @return 返回user
	 */
	protected User getUser() {
		return getApplicationContext().getUser();
	}

	// ------------------------下面填充项目自定义方法---------------------------

//	// 友盟相关统计
//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (SKRConfig.UMENG_ENABLE)
//		{
//			UmengUtils.onActivityResume(this);
//			MobclickAgent.onResume(this);
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (SKRConfig.UMENG_ENABLE)
//		{
//			UmengUtils.onActivityPause(this);
//			MobclickAgent.onPause(this);
//		}
//	}
	// 友盟相关end
}
