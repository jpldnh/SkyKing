package com.skingapp.commen.config;

/**
 * 该项目配置信息
 */
public class BaseConfig {

	/**
	 * 后台服务接口根路径
	 */
//	public static final String SYS_ROOT = "http://192.168.2.146:8008/group1/hm_PHP/";
	public static final String SYS_ROOT = "http://124.128.23.74:8008/group1/hm_PHP/";
	/**
	 * 是否打印信息开关
	 */
	public static final boolean DEBUG = true;
	/**
	 * 是否启用友盟统计
	 */
	public static final boolean UMENG_ENABLE = false;
	/**
	 * 是否开启类似IOS侧滑关闭当前Activity功能
	 */
	public static boolean IS_OPEN_UP_SWIP = false;
	/**
	 * 图片压缩的最大宽度
	 */
	public static final int IMAGE_WIDTH = 640;
	/**
	 * 图片压缩的最大高度
	 */
	public static final int IMAGE_HEIGHT = 3000;
	/**
	 * 图片压缩的失真率
	 */
	public static final int IMAGE_QUALITY = 100;
	/**
	 * 银联支付环境--"00"生产环境,"01"测试环境
	 */
	public static final String UNIONPAY_TESTMODE = "01";
	/**
	 * 微信appid
	 */
	public static final String APPID_WEIXIN = "wx0789d6863bfbdca0";
	/**
	 * DATAKEY为Md5加密字符串
	 */
	public static String DATAKEY = "bY3kj9DKBuKsiETx";
	/**
	 * 程序运行密钥，切记要改，改，改
	 */
	public static String APPLICATION_KEY = "SKING_CONMMEN_KEY";

}
