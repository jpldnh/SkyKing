package com.sking.lib.base;

import android.app.Application;

import com.sking.lib.utils.SKBaseUtil;
import com.sking.lib.utils.SKLogger;

public abstract class SKApplication extends Application {
	/**
	 * 打印TAG，类名
	 */
	private String TAG;

	protected SKApplication() {
		TAG = getLogTag();
	}

	// 获取打印TAG，即类名
	private String getLogTag() {
		return getClass().getSimpleName();
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

}
