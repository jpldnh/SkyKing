package com.sking.lib.res.config;

/**
 * 该项目配置信息
 */
public class SKRConfig {
	/**
	 * 网络请求连接超时时限(单位:毫秒)
	 */
	public static final int TIMEOUT_HTTP = 30000;
	/**
	 * 网络请求尝试次数
	 */
	public static final int TRYTIMES_HTTP = 1;
	/**
	 * 登录请求id
	 */
	public static final int ID_LOGIN = -1;
	/**
	 * 第三方登录请求id
	 */
	public static final int ID_THIRDSAVE = -2;
	/**
	 * 验证密钥
	 */
	public static final int ID_VERIFYKEY = -3;
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
	 * 程序运行密钥,SKING_CONMMEN_KEY为通用密钥
	 */
	public static String APPLICATION_KEY = "";
	/**
	 * 是否启用友盟统计
	 */
	public static boolean UMENG_ENABLE = false;
	/**
	 * 是否显示网络不给力异常Toast提示
	 */
	public static boolean TOAST_NET_ENABLE = true;
	/**
	 * 是否开启类似IOS侧滑关闭当前Activity功能
	 */
	public static boolean IS_OPEN_UP_SWIP = false;
}
