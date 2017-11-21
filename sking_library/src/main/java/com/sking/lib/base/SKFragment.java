package com.sking.lib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sking.lib.imageload.SKImageCache;
import com.sking.lib.imageload.SKImageWorker;
import com.sking.lib.interfaces.SKNetTaskExecuteListener;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.net.SKNetWorker;
import com.sking.lib.utils.SKBaseUtil;
import com.sking.lib.utils.SKDeviceUtil;
import com.sking.lib.utils.SKLogger;
import com.sking.lib.utils.SKToastUtil;

import java.util.List;


/**
 * 基本框架.
 * 特别注意setContentView();应在super.onCreate(savedInstanceState);之前调用否则会导致findView
 * ();等初始化方法失效。 该框架内置了网络访问、图片下载、文件下载功能。
 * <p>
 * 1.网络访问使用方法：直接调用{@link # getDataFromServer(task)}方法即可;
 * </p>
 * <p>
 * 2.图片下载使用方法：imageWorker.loadImage(task);
 * </p>
 * <p>
 * 3.集成了log_v(msg)等打印方法以及println(Object)。
 * </p>
 */
public abstract class SKFragment extends Fragment {
	protected static final String NO_NETWORK = "无网络连接，请检查网络设置。";
	protected static final String FAILED_GETDATA_HTTP = "请求异常。";
	protected static final String FAILED_GETDATA_DATAPARSE = "数据异常。";

	/**
	 * Context
	 * */
	protected Context mContext;

	/**
	 * 打印TAG，类名
	 */
	private String TAG;
	/**
	 * 下载图片使用
	 */
	public SKImageWorker imageWorker;
	private SKNetWorker netWorker;
	/**
	 * 获取传参使用
	 */
	protected Intent mIntent;
	/**
	 * 根view
	 */
	protected View rootView;

	/**
	 * 根view id
	 */
	private int rootViewId;

	private static Fragment currForResultFragment;

	protected SKFragment() {
		TAG = getLogTag();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		imageWorker = new SKImageWorker(getActivity());
		if (rootView == null)
			rootView = LayoutInflater.from(getActivity()).inflate(rootViewId,
					null, false);
		init();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if (currForResultFragment == null)
			currForResultFragment = this;
		if (getParentFragment() != null)
			getParentFragment().startActivityForResult(intent, requestCode);
		else
			super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null == currForResultFragment || currForResultFragment.equals(this))
			super.onActivityResult(requestCode, resultCode, data);
		else {
			currForResultFragment.onActivityResult(requestCode, resultCode,
					data);
		}
		currForResultFragment = null;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		List<Fragment> fragments = getChildFragmentManager().getFragments();
		if (fragments != null)
			for (Fragment fragment : fragments) {
				fragment.onHiddenChanged(hidden);
			}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopNetThread();// 杀掉网络线程
		if (imageWorker != null)
			imageWorker.clearTasks();// 取消图片下载任务
		recyclePics();// 回收图片
		SKToastUtil.cancelAllToast();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return rootView;
	}

	// 初始化
	private void init() {
		findView();
		setListener();
	}

	/**
	 * 设置根view
	 * 
	 * @param layoutResID
	 */
	public void setContentView(int layoutResID) {
		rootViewId = layoutResID;
	}

	/**
	 * 设置根view
	 * 
	 * @param v
	 */
	public void setContentView(View v) {
		rootView = v;
	}

	/**
	 * 发送post请求并且获取数据.该方法可发送文件数据
	 * 
	 * @param task
	 *            网络请求任务new SKNetTask(任务ID,任务URL, 任务参数集(参数名,参数值))
	 */
	public void getDataFromServer(SKNetTask task) {
		if (netWorker == null) {
			netWorker = new SKNetWorker(getActivity());
			netWorker.setOnTaskExecuteListener(new SKTaskExecuteListener());
		}
		netWorker.executeTask(task);
	}

	/**
	 * 无网络提示
	 * 
	 * @param task
	 */
	protected void noNetWork(SKNetTask task) {
		noNetWork(task.getId());
	}

	/**
	 * 无网络提示
	 * 
	 * @param taskID
	 */
	protected void noNetWork(int taskID) {
		noNetWork();
	}

	/**
	 * 无网络提示
	 */
	protected void noNetWork() {
		SKToastUtil.showLongToast(getActivity(), NO_NETWORK);
	}

	/**
	 * 初始化三部曲之：查找控件
	 */
	protected abstract void findView();

	/**
	 * 初始化三部曲之：设置监听
	 */
	protected abstract void setListener();

	/**
	 * 返回数据前的操作，如显示进度条
	 * 
	 * @param netTask
	 *            所执行的任务
	 */
	protected abstract void networkRequestBefore(SKNetTask netTask);

	/**
	 * 返回数据后的操作，如关闭进度条
	 * 
	 * @param netTask
	 *            所执行的任务
	 */
	protected abstract void networkRequestAfter(SKNetTask netTask);

	/**
	 * 获取数据成功后回调方法
	 * 
	 * @param netTask
	 *            所执行的任务
	 * @param result
	 *            服务器返回结果
	 */
	protected abstract void networkRequestSuccess(SKNetTask netTask,
			Object result);

	/**
	 * 暴漏给外部的刷新界面方法
	 */
	public void fresh() {

	}

	/**
	 * 获取数据失败后回调方法
	 * 
	 * @param type
	 *            -2请求异常，-3数据异常
	 * @param netTask
	 *            所执行的任务
	 */
	protected void networkRequestFailed(int type, SKNetTask netTask) {
		networkRequestFailed(type, netTask.getId());
	}

	/**
	 * 获取数据失败后回调方法
	 * 
	 * @param type
	 *            -2请求异常，-3数据异常
	 * @param taskID
	 *            所执行的任务id
	 */
	protected void networkRequestFailed(int type, int taskID) {
		switch (type) {
		case SKNetWorker.FAILED_HTTP:
			SKToastUtil.showLongToast(getActivity(), FAILED_GETDATA_HTTP
					+ "TASKID:" + taskID);
			break;
		case SKNetWorker.FAILED_DATAPARSE:
			SKToastUtil.showLongToast(getActivity(), FAILED_GETDATA_DATAPARSE
					+ "TASKID:" + taskID);
			break;
		default:
			break;
		}

	}

	/**
	 * 打印v级别信息
	 * 
	 * @param msg
	 */
	protected void log_v(String msg) {
		SKLogger.v(TAG, msg);
	}

	/**
	 * 打印d级别信息
	 * 
	 * @param msg
	 */
	protected void log_d(String msg) {
		SKLogger.d(TAG, msg);
	}

	/**
	 * 打印i级别信息
	 * 
	 * @param msg
	 */
	protected void log_i(String msg) {
		SKLogger.i(TAG, msg);
	}

	/**
	 * 打印w级别信息
	 * 
	 * @param msg
	 */
	protected void log_w(String msg) {
		SKLogger.w(TAG, msg);
	}

	/**
	 * 打印e级别信息
	 * 
	 * @param msg
	 */
	protected void log_e(String msg) {
		SKLogger.e(TAG, msg);
	}

	/**
	 * 打印
	 * 
	 * @param msg
	 */
	protected void println(Object msg) {
		SKLogger.println(msg);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return true如果该字符串为null或者"",否则false
	 */
	protected boolean isNull(String str) {
		return SKBaseUtil.isNull(str);
	}

	/**
	 * 判断网络任务是否都已完成
	 * 
	 * @return
	 */
	protected boolean isNetTasksFinished() {
		return netWorker == null || netWorker.isNetTasksFinished();
	}

	// 回收图片
	private void recyclePics() {
		SKImageCache.get().reMoveCacheInMemByObj(this);
		SKImageCache.get().recyclePics();
	}

	// 杀掉网络线程
	private void stopNetThread() {
		if (netWorker != null) {
			netWorker.cancelTasks();
		}
	}

	// 获取打印TAG，即类名
	private String getLogTag() {
		return getClass().getSimpleName();
	}

	/**
	 * 判断当前是否有可用网络
	 * 
	 * @return 如果有true否则false
	 */
	public boolean hasNetWork() {
		return SKDeviceUtil.hasNetWork(getActivity());
	}

	private class SKTaskExecuteListener implements SKNetTaskExecuteListener {

		@Override
		public void onPreExecute(SKNetWorker netWorker, SKNetTask task) {
			networkRequestBefore(task);
		}

		@Override
		public void onPostExecute(SKNetWorker netWorker, SKNetTask task) {
			networkRequestAfter(task);
		}

		@Override
		public void onExecuteSuccess(SKNetWorker netWorker, SKNetTask task,
				Object result) {
			networkRequestSuccess(task, result);
		}

		@Override
		public void onExecuteFailed(SKNetWorker netWorker, SKNetTask task,
				int failedType) {
			switch (failedType) {
			case SKNetWorker.FAILED_DATAPARSE:
			case SKNetWorker.FAILED_HTTP:
				networkRequestFailed(failedType, task);
				break;
			case SKNetWorker.FAILED_NONETWORK:
				noNetWork(task);
				break;
			default:
				break;
			}
		}
	}
}
