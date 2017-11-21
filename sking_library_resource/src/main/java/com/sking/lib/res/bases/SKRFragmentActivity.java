package com.sking.lib.res.bases;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sking.lib.base.SKFragmentActivity;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.res.R;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRProgressDialogUtil;
import com.sking.lib.res.utils.SKRTextDialogUtil;
import com.sking.lib.utils.SKScreenUtil;
import com.sking.lib.utils.SKToastUtil;

import java.util.List;


/**
 * 本项目中对XtomFragmentActivity的一些拓展
 * <p>
 * 1.本项目中发送网络请求不建议使用{@link # getDataFromServer(SKNetTask)} 建议使用如下
 * 
 * <pre>
 * SKRNetWorker netWorker = {@link #getNetWorker()};
 * netWorker.login();
 * </pre>
 * 
 * </p>
 * 
 */
public abstract class SKRFragmentActivity extends SKFragmentActivity {
	private SKRNetWorker netWorker;
	private SKRTextDialogUtil textDialog;
	private SKRProgressDialogUtil progressDialog;
	protected boolean isStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStatusBarAndNevagitBar();
	}


	@Override
	protected void onStop() {
		isStop = true;
		super.onStop();
	}

	/**
	 * 显示或更换Fragment
	 * 
	 * @param fragmentClass
	 *            Fragment.class
	 * @param containerViewId
	 *            Fragment显示的空间ID
	 * @param replace
	 *            是否替换
	 */
	public void toogleFragment(Class<? extends Fragment> fragmentClass,
			int containerViewId, boolean replace) {
		if (isStop)
			return;
		FragmentManager manager = getSupportFragmentManager();
		String tag = fragmentClass.getName();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment fragment = manager.findFragmentByTag(tag);

		if (fragment == null) {
			try {
				fragment = fragmentClass.newInstance();
				if (replace)
					transaction.replace(containerViewId, fragment, tag);
				else
					// 替换时保留Fragment,以便复用
					transaction.add(containerViewId, fragment, tag);
			} catch (Exception e) {
				// ignore
			}
		} else {
			// nothing
		}
		// 遍历存在的Fragment,隐藏其他Fragment
		List<Fragment> fragments = manager.getFragments();
		if (fragments != null)
			for (Fragment fm : fragments)
				if (fm != null && !fm.equals(fragment))
					transaction.hide(fm);

		transaction.show(fragment);
		transaction.commit();
	}

	/**
	 * 关闭Activity
	 * 
	 * @param enterAnim
	 *            进入Activity的动画,若没有传0即可
	 * @param exitAnim
	 *            退出Activity的动画,若没有传0即可
	 */
	public void finish(int enterAnim, int exitAnim) {
		finish();
		overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 
	 * @param enterAnim
	 *            进入Activity的动画,若没有传0即可
	 * @param exitAnim
	 *            退出Activity的动画,若没有传0即可
	 */
	public void startActivityForResult(Intent intent, int requestCode,
			int enterAnim, int exitAnim) {
		startActivityForResult(intent, requestCode);
		if (getParent() != null)
			getParent().overridePendingTransition(enterAnim, exitAnim);
		else
			overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 
	 * @param enterAnim
	 *            进入Activity的动画,若没有传0即可
	 * @param exitAnim
	 *            退出Activity的动画,若没有传0即可
	 */
	public void startActivity(Intent intent, int enterAnim, int exitAnim) {
		startActivity(intent);
		if (getParent() != null)
			getParent().overridePendingTransition(enterAnim, exitAnim);
		else
			overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 *  @time 2017/9/29  17:15
	 *  @author Im_jingwei
	 *  @desc 设置状态栏和导航栏，若要修改重写次方法
	 */
	public void setStatusBarAndNevagitBar()
	{
//        SKScreenUtil.setStatusBarTransparent(this,true);//透明化状态栏
		SKScreenUtil.setStatusBar(this,skGetColor(R.color.colorPrimary));//设置状态栏颜色
		SKScreenUtil.setNevigationBar(this,skGetColor(R.color.colorPrimary));
	}


	/**
	 * 显示交互弹窗(默认不可以点击弹窗外侧取消)
	 * 
	 * @param text
	 *            弹窗提示语
	 */
	public void showProgressDialog(String text) {
		if (progressDialog == null)
			progressDialog = new SKRProgressDialogUtil(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	/**
	 * 显示交互弹窗
	 * 
	 * @param text
	 *            弹窗提示语id
	 * @param cancelable
	 *            是否可以点击弹窗外侧取消
	 */
	public void showProgressDialog(String text, boolean cancelable) {
		if (progressDialog == null)
			progressDialog = new SKRProgressDialogUtil(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(cancelable);
		progressDialog.show();
	}

	/**
	 * 显示交互弹窗(默认不可以点击弹窗外侧取消)
	 * 
	 * @param text
	 *            弹窗提示语
	 */
	public void showProgressDialog(int text) {
		if (progressDialog == null)
			progressDialog = new SKRProgressDialogUtil(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	/**
	 * 显示交互弹窗
	 * 
	 * @param text
	 *            弹窗提示语
	 * @param cancelable
	 *            是否可以点击弹窗外侧取消
	 */
	public void showProgressDialog(int text, boolean cancelable) {
		if (progressDialog == null)
			progressDialog = new SKRProgressDialogUtil(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(cancelable);
		progressDialog.show();
	}

	/**
	 * 取消交互弹窗(同时setCancelable(false))
	 */
	public void cancelProgressDialog() {
		if (progressDialog != null) {
			progressDialog.setCancelable(false);
			progressDialog.cancel();
		}
	}

	/**
	 * 显示提示弹窗
	 * 
	 * @param text
	 *            弹窗提示语
	 */
	public void showTextDialog(String text) {
		if (textDialog == null)
			textDialog = new SKRTextDialogUtil(this);
		textDialog.setText(text);
		textDialog.show();
	}

	/**
	 * 显示提示弹窗
	 * 
	 * @param text
	 *            弹窗提示语id
	 */
	public void showTextDialog(int text) {
		if (textDialog == null)
			textDialog = new SKRTextDialogUtil(this);
		textDialog.setText(text);
		textDialog.show();
	}

	/**
	 * 取消提示弹窗
	 */
	public void cancelTextDialog() {
		if (textDialog != null)
			textDialog.cancel();
	}

	/**
	 * 土司信息
	 */
	public void showToastLong(int text) {
		SKToastUtil.showLongToast(mContext, text);
	}

	/**
	 * 土司信息
	 */
	public void showToastLong(String text) {
		SKToastUtil.showLongToast(mContext, text);
	}

	/**
	 * 土司信息
	 */
	public void showToastShort(int text) {
		SKToastUtil.showShortToast(mContext, text);
	}

	/**
	 * 土司信息
	 */
	public void showToastShort(String text) {
		SKToastUtil.showShortToast(mContext, text);
	}


	/**
	 * 获取颜色
	 */
	public int skGetColor(int color)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			return getResources().getColor(color ,null);
		else
			return getResources().getColor(color);
	}

	@Override
	protected void onDestroy() {
		if (netWorker != null) {
			netWorker.cancelTasks();
			netWorker.setOnTaskExecuteListener(null);
		}
		super.onDestroy();
	}

	@Override
	public void finish() {
		cancelTextDialog();
		if (progressDialog != null)
			progressDialog.cancelImmediately();
		super.finish();
	}

	/**
	 * 获取网络请求工具类
	 */
	public SKRNetWorker getNetWorker() {
		if (netWorker == null) {
			netWorker = initNetWorker();
			netWorker
					.setOnTaskExecuteListener(new SKNetTaskExecuteListener(this));
		}
		return netWorker;
	}

	/**
	 * 初始化NetWorker
	 */
	protected abstract SKRNetWorker initNetWorker();

	/**
	 * 返回数据前的操作，如显示进度条
	 * 
	 * @param netTask
	 */
	protected abstract void networkRequestBefore(SKRNetTask netTask);

	/**
	 * 返回数据后的操作，如关闭进度条
	 * 
	 * @param netTask
	 */
	protected abstract void networkRequestAfter(SKRNetTask netTask);

	/**
	 * 服务器处理成功
	 * 
	 * @param netTask
	 * @param baseResult
	 */
	protected abstract void networkRequestSuccess(SKRNetTask netTask,
			SKRBaseResult baseResult);

	/**
	 * 服务器处理失败
	 * 
	 * @param netTask
	 * @param baseResult
	 */
	protected abstract void networkRequestParseFailed(SKRNetTask netTask,
			SKRBaseResult baseResult);

	/**
	 * 获取数据失败
	 * 
	 * @param netTask
	 * @param failedType
	 *            失败原因
	 *            <p>
	 *            See {@links FrameNetWorker#FAILED_DATAPARSE
	 *            XtomNetWorker.FAILED_DATAPARSE},
	 *            {@links FrameNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
	 *            {@links FrameNetWorker#FAILED_NONETWORK
	 *            XtomNetWorker.FAILED_HTTP}
	 *            </p>
	 */
	protected abstract void networkRequestExecuteFailed(SKRNetTask netTask,
			int failedType);

	/**
	 * 自动登录失败
	 * 
	 * @param netWorker
	 * @param netTask
	 * @param failedType
	 *            如果failedType为0表示服务器处理失败,其余参照
	 *            {@links FrameNetWorker#FAILED_DATAPARSE
	 *            XtomNetWorker.FAILED_DATAPARSE},
	 *            {@links FrameNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
	 *            {@links FrameNetWorker#FAILED_NONETWORK
	 *            XtomNetWorker.FAILED_NONETWORK}
	 * @param baseResult
	 *            执行结果(仅当failedType为0时有值,其余为null)
	 * @return true表示拦截该任务执行流程,
	 *         不会继续调用callBackForServerFailed或者callBackForGetDataFailed方法;
	 *         false反之
	 */
	public abstract boolean onAutoLoginFailed(SKRNetWorker netWorker,
											  SKRNetTask netTask, int failedType, SKRBaseResult baseResult);

	private class SKNetTaskExecuteListener extends SKRNetTaskExecuteListener {

		public SKNetTaskExecuteListener(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute(SKRNetWorker netWorker, SKRNetTask netTask) {
			networkRequestBefore(netTask);
		}

		@Override
		public void onPostExecute(SKRNetWorker netWorker, SKRNetTask netTask) {
			networkRequestAfter(netTask);
		}

		@Override
		public void onServerSuccess(SKRNetWorker netWorker,
									SKRNetTask netTask, SKRBaseResult baseResult) {
			networkRequestSuccess(netTask, baseResult);
		}

		@Override
		public void onServerFailed(SKRNetWorker netWorker,
								   SKRNetTask netTask, SKRBaseResult baseResult) {
			networkRequestParseFailed(netTask, baseResult);
		}

		@Override
		public void onExecuteFailed(SKRNetWorker netWorker,
									SKRNetTask netTask, int failedType) {
			networkRequestExecuteFailed(netTask, failedType);
		}

		@Override
		public boolean onAutoLoginFailed(SKRNetWorker netWorker,
										 SKRNetTask netTask, int failedType, SKRBaseResult baseResult) {
			return SKRFragmentActivity.this.onAutoLoginFailed(netWorker,
					netTask, failedType, baseResult);
		}
	}

	@Override
	protected boolean onKeyBack() {
		finish();
		return true;
	}

	@Override
	protected boolean onKeyMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void networkRequestBefore(SKNetTask netTask) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void networkRequestAfter(SKNetTask netTask) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void networkRequestSuccess(SKNetTask netTask, Object result) {
		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void getDataFromServer(SKNetTask netTask) {
		log_e("本项目中不建议使用此方法");
	}
//
//	// 友盟相关
//	@Override
//	protected void onResume() {
//		isStop = false;
//		super.onResume();
//		if (SKRConfig.UMENG_ENABLE)
//			MobclickAgent.onResume(this);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (SKRConfig.UMENG_ENABLE)
//			MobclickAgent.onPause(this);
//	}
//
//	// 友盟相关end

	@Override
	protected void onNewIntent(Intent intent) {
		isStop = false;
		super.onNewIntent(intent);
	}
}
