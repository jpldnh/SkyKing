package com.sking.lib.exception;

/**
 * 数据解析异常
 */
public class SKDataParseException extends Exception {

	/**
	 * @param e
	 */
	public SKDataParseException(Exception e) {
		e.printStackTrace();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
