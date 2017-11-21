package com.sking.lib.exception;

/**
 * 网络请求异常
 */
public class SKHttpException extends Exception {

	/**
	 * @param e
	 */
	public SKHttpException(Exception e) {
		e.printStackTrace();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
