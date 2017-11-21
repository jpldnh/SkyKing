package com.skingapp.commen.bases;

import com.sking.lib.res.bases.SKRHttpInfomation;
import com.sking.lib.res.config.SKRConfig;
import com.skingapp.commen.config.BaseConfig;
import com.skingapp.commen.models.SysInitInfo;

/**
 * 网络请求信息枚举类
 */
public enum BaseHttpInformation implements SKRHttpInfomation {

	/**
	 * 密钥验证
	 */
	VERIFY_APPKEY(SKRConfig.ID_VERIFYKEY, "index.php/webservice/index/init", "密钥验证", false),
	// 注意密钥验证接口id必须为BaseConfig.ID_VERIFYKEY
	/**
	 * 登录
	 */
	CLIENT_LOGIN(SKRConfig.ID_LOGIN, "client_login", "登录", false),
	// 注意登录接口id必须为BaseConfig.ID_LOGIN
	/**
	 * 第三方登录
	 */
	THIRD_SAVE(SKRConfig.ID_THIRDSAVE, "third_save", "第三方登录", false),
	// 注意第三方登录接口id必须为BaseConfig.ID_THIRDSAVE

	/**
	 * 后台服务接口根路径
	 */
	SYS_ROOT(0, BaseConfig.SYS_ROOT, "后台服务接口根路径", true),
	/**
	 * 系统初始化
	 */
	INIT(1, "index.php/webservice/index/init", "系统初始化", false),
	/**
	 * 验证用户名是否合法
	 */
	CLIENT_VERIFY(3, "client_verify", "验证用户名是否合法", false),
	/**
	 * 申请随机验证码
	 */
	CODE_GET(3, "code_get", "申请随机验证码", false),
	/**
	 * 验证随机码
	 */
	CODE_VERIFY(4, "code_verify", "验证随机码", false),
	/**
	 * 用户注册
	 */
	CLIENT_ADD(5, "client_add", "用户注册", false),
	/**
	 * 上传文件（图片，音频，视频）
	 */
	FILE_UPLOAD(6, "file_upload", "上传文件（图片，音频，视频）", false),
	/**
	 * 重设密码
	 */
	PASSWORD_RESET(7, "password_reset", "重设密码", false),
	/**
	 * 退出登录
	 */
	CLIENT_LOGINOUT(8, "client_loginout", "退出登录", false),
	/**
	 * 获取用户个人资料
	 */
	CLIENT_GET(9, "client_get", "获取用户个人资料", false),
	/**
	 * 保存用户资料
	 */
	CLIENT_SAVE(10, "client_save", "保存用户资料", false),
	/**
	 * 修改并保存密码
	 */
	PASSWORD_SAVE(11, "password_save", "修改并保存密码", false),
	/**
	 * 获取用户通知列表
	 */
	NOTICE_LIST(12, "notice_list", "获取用户通知列表", false),
	/**
	 * 保存用户通知操作
	 */
	NOTICE_SAVEOPERATE(13, "notice_saveoperate", "保存用户通知操作", false),
	/**
	 * 意见反馈
	 */
	ADVICE_ADD(14, "advice_add", "意见反馈", false),
	/**
	 * 硬件注册保存
	 */
	DEVICE_SAVE(15, "device_save", "硬件注册保存", false),
	/**
	 * 获取支付宝交易签名串
	 */
	ALIPAY(16, "OnlinePay/Alipay/alipaysign_get.php", "获取支付宝交易签名串", false),
	/**
	 * 获取银联交易签名串
	 */
	UNIONPAY(17, "OnlinePay/Unionpay/unionpay_get.php", "获取银联交易签名串", false),
	/**
	 * 获取微信预支付交易会话标识
	 */
	WEIXINPAY(18, "OnlinePay/Weixinpay/weixinpay_get.php", "获取微信预支付交易会话标识",false),

	/**
	 * 用户账户余额付款
	 */
	CLIENT_ACCOUNTPAY(19, "client_accountpay", "用户账户余额付款", false),
	/**
	 * 获取地区（城市）列表信息
	 * */
	DISTRICT_LIST(20, "district_list", "获取地区（城市）列表信息", false),
	/**
	 * 热门城市
	 * */
	HOTCITY_LIST(21, "popular_city_list", "热门城市", false),

	;

	private int id;// 对应NetTask的id
	private String urlPath;// 请求地址
	private String description;// 请求描述
	private boolean isRootPath;// 是否是根路径

	private BaseHttpInformation(int id, String urlPath, String description,
								boolean isRootPath) {
		this.id = id;
		this.urlPath = urlPath;
		this.description = description;
		this.isRootPath = isRootPath;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getUrlPath() {
		if (isRootPath)
			return urlPath;

		String path = SYS_ROOT.urlPath + urlPath;

		if (this.equals(INIT))
			return path;

		BaseApplication application = BaseApplication.getInstance();
		SysInitInfo info = application.getSysInitInfo();
		path = info.getSys_web_service() + urlPath;

		if (this.equals(ALIPAY))
			path = info.getSys_plugins() + urlPath;

		if (this.equals(UNIONPAY))
			path = info.getSys_plugins() + urlPath;

		if (this.equals(WEIXINPAY))
			path = info.getSys_plugins() + urlPath;

		if (this.equals(VERIFY_APPKEY))
			path = info.getSys_appkey() + urlPath;

		return path;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean isRootPath() {
		return isRootPath;
	}

}
