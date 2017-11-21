package com.skingapp.commen.bases;

import android.content.Context;
import android.content.Intent;

import com.skingapp.commen.activitys.LoginActivity;
import com.sking.lib.base.SKActivityManager;
import com.sking.lib.net.SKNetWorker;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.bases.SKRNetTaskExecuteListener;
import com.sking.lib.res.bases.SKRNetWorker;
import com.sking.lib.res.results.SKRBaseResult;


/**
 * 网络任务执行监听
 */
public abstract class BaseNetTaskExecuteListener extends SKRNetTaskExecuteListener {

	public BaseNetTaskExecuteListener(Context context) {
		super(context);
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
				mContext.startActivity(it);
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

}
