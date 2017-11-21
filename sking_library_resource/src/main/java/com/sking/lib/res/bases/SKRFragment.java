package com.sking.lib.res.bases;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sking.lib.base.SKFragment;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.res.results.SKRBaseResult;

import java.util.List;


/**
 * 本项目中对XtomFragment的一些拓展
 * <p>
 * 1.本项目中发送网络请求不建议使用{@link # getDataFromServer(SKNetTask)} 建议使用如下
 * 
 * <pre>
 * SKRNetWorker netWorker = {@link #getNetWorker()};
 * netWorker.login();
 * </pre>
 * 
 */
public abstract class SKRFragment extends SKFragment {
	private SKRNetWorker netWorker;

	public View findViewById(int id) {
		View view = null;
		if (rootView != null)
			view = rootView.findViewById(id);
		return view;
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
		FragmentManager manager = getChildFragmentManager();
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
				if (!fm.equals(fragment))
					transaction.hide(fm);

		transaction.show(fragment);
		transaction.commit();
	}

	public void showProgressDialog(String text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showProgressDialog(text);
	}

	public void showProgressDialog(int text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showProgressDialog(text);
	}

	public void cancelProgressDialog() {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.cancelProgressDialog();
	}

	public void showTextDialog(String text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showTextDialog(text);
	}

	public void showTextDialog(int text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showTextDialog(text);
	}

	public void showLongToast(String text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showToastLong(text);
	}

	public void showLongToast(int text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showToastLong(text);
	}

	public void showShortToast(String text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showToastShort(text);
	}

	public void showShortToast(int text) {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.showToastShort(text);
	}

	public void cancelTextDialog() {
		SKRFragmentActivity activity = (SKRFragmentActivity) getActivity();
		activity.cancelTextDialog();
	}

	@Override
	public void onDestroy() {
		if (netWorker != null)
			netWorker.setOnTaskExecuteListener(null);
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return rootView;
	}

	/**
	 * 获取网络请求工具类
	 */
	public SKRNetWorker getNetWorker() {
		if (netWorker == null) {
			netWorker = initNetWorker();
			netWorker.setOnTaskExecuteListener(new SKNetTaskExecuteListener(
					getActivity()));
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
			return SKRFragment.this.onAutoLoginFailed(netWorker, netTask,
					failedType, baseResult);
		}
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

//	// 友盟相关
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (SKRConfig.UMENG_ENABLE)
//			MobclickAgent.onPageStart(getClass().getSimpleName());
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		if (SKRConfig.UMENG_ENABLE)
//			MobclickAgent.onPageEnd(getClass().getSimpleName());
//	}
//	// 友盟相关end
}
