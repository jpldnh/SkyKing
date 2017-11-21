package com.clubank.tpy.bases;

import android.content.Context;

import com.clubank.tpy.models.AlipayTrade;
import com.clubank.tpy.models.FileUploadResult;
import com.clubank.tpy.models.StringModel;
import com.clubank.tpy.models.SysInitInfo;
import com.clubank.tpy.models.UnionTrade;
import com.clubank.tpy.models.User;
import com.clubank.tpy.models.WeixinTrade;
import com.clubank.tpy.nettasks.ExecuteNetTask;
import com.sking.lib.res.bases.SKRNetWorker;
import com.sking.lib.res.utils.SKRUtil;
import com.sking.lib.utils.SKDeviceUtil;
import com.sking.lib.utils.SKSharedPreferencesUtil;

import java.util.HashMap;


/**
 * 网络请求工具类
 */
public class BaseNetWorker extends SKRNetWorker {
	/**
	 * 实例化网络请求工具类
	 * 
	 * @param mContext
	 */
	private Context mContext;

	public BaseNetWorker(Context mContext) {
		super(mContext);
		this.mContext = mContext;
	}

	/**
	 * 系统初始化
	 */
	public void init() {
		BaseHttpInformation information = BaseHttpInformation.INIT;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("lastloginversion", SKRUtil.getAppVersionForSever(mContext));// 版本号码(默认：1.0.0)
		params.put("device_sn", SKDeviceUtil.getDeviceUuid(mContext));// 客户端硬件串号

		BaseNetTask task = new ExecuteNetTask(information, params, SysInitInfo.class);
		executeTask(task);
	}

	/**
	 * 密钥验证
	 */
	@Override
	public void verifyAppkey() {
		BaseHttpInformation information = BaseHttpInformation.VERIFY_APPKEY;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("app_key", getAppkey());//

		BaseNetTask task = new ExecuteNetTask(information, params);
		executeTask(task);
	}

	@Override
	public void clientLogin() {
		BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGIN;
		HashMap<String, String> params = new HashMap<String, String>();
		String username = SKSharedPreferencesUtil.get(mContext, "username");
		params.put("username", username);// 用户登录名 手机号或邮箱
		String password = SKSharedPreferencesUtil.get(mContext, "password");
		params.put("password", password); // 登陆密码 服务器端存储的是32位的MD5加密串
		params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
		String version = SKRUtil.getAppVersionForSever(mContext);
		params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计

		BaseNetTask task = new ExecuteNetTask(information, params,User.class);
		executeTask(task);
	}

	@Override
	public boolean thirdSave() {
		if (SKRUtil.isThirdSave(mContext)) {
			BaseHttpInformation information = BaseHttpInformation.THIRD_SAVE;
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
			String version = SKRUtil.getAppVersionForSever(mContext);
			params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
			String thirdtype = SKSharedPreferencesUtil.get(mContext,
					"thirdtype");
			params.put("thirdtype", thirdtype);// 平台类型 1：微信 2：QQ 3：微博
			String thirduid = SKSharedPreferencesUtil.get(mContext,
					"thirduid");
			params.put("thirduid", thirduid);// 平台用户id 该平台唯一的id
			String avatar = SKSharedPreferencesUtil.get(mContext, "avatar");
			params.put("avatar", avatar);// 平台用户头像 图片地址
			String nickname = SKSharedPreferencesUtil.get(mContext,
					"nickname");
			params.put("nickname", nickname);// 平台用户昵称
			String sex = SKSharedPreferencesUtil.get(mContext, "sex");
			params.put("sex", sex);// 姓名 "男"或"女"
			String age = SKSharedPreferencesUtil.get(mContext, "age");
			params.put("age", age);// 年龄

			BaseNetTask task = new ExecuteNetTask(information, params,User.class);
			executeTask(task);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 登录
	 */
	public void clientLogin(String username, String password) {
		BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGIN;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);// 用户登录名 手机号或邮箱
		params.put("password", password); // 登陆密码 服务器端存储的是32位的MD5加密串
		params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
		String version = SKRUtil.getAppVersionForSever(mContext);
		params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计

//		BaseNetTask task = new ExecuteNetTask(information, params);
		BaseNetTask task = new ExecuteNetTask(information, params,User.class);
		executeTask(task);
	}

	/**
	 * 退出登录
	 */
	public void clientLoginout(String token) {
		BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGINOUT;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);// 登陆令牌

		BaseNetTask task = new ExecuteNetTask(information, params);
		executeTask(task);
	}

	/**
	 * 获取用户个人资料
	 */
	public void clientGet(String token, String id) {
		BaseHttpInformation information = BaseHttpInformation.CLIENT_GET;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("id", id);

		BaseNetTask task = new ExecuteNetTask(information, params,User.class);
		executeTask(task);
	}

	/**
	 * 硬件注册保存
	 */
	public void deviceSave(String token, String deviceid, String devicetype,
			String channelid) {
		BaseHttpInformation information = BaseHttpInformation.DEVICE_SAVE;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);// 登陆令牌
		params.put("deviceid", deviceid);// 登陆手机硬件码 对应百度推送userid
		params.put("devicetype", devicetype);// 登陆手机类型 1:苹果 2:安卓
		params.put("channelid", channelid);// 百度推送渠道id 方便直接从百度后台进行推送测试

		BaseNetTask task = new ExecuteNetTask(information, params);
		executeTask(task);
	}

	/**
	 * 申请随机验证码
	 */
	public void codeGet(String username) {
		BaseHttpInformation information = BaseHttpInformation.CODE_GET;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);// 用户登录名 手机号或邮箱

		BaseNetTask task = new ExecuteNetTask(information, params);
		executeTask(task);
	}
	
	/**
	 * 验证随机码
	 */
	public void codeVerify(String username, String code) {
		BaseHttpInformation information = BaseHttpInformation.CODE_VERIFY;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);// 用户登录名 手机号或邮箱
		params.put("code", code);// 6位随机号码 测试阶段固定向服务器提交“123456”

		BaseNetTask task = new ExecuteNetTask(information, params, StringModel.class);
		executeTask(task);
	}
	
	/**
	 * 验证用户名是否合法
	 */
	public void clientVerify(String username) {
		BaseHttpInformation information = BaseHttpInformation.CLIENT_VERIFY;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);// 用户登录名 手机号或邮箱

		BaseNetTask task = new ExecuteNetTask(information, params);
		executeTask(task);
	}
	
	/**
	 * 用户注册
	 */
	public void clientAdd(String temp_token, String username, String password,
			String nickname, String sex, String district_name, String selfsign,
			String email) {
		BaseHttpInformation information = BaseHttpInformation.CLIENT_ADD;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("temp_token", temp_token);// 临时令牌 可以有效防止机器人恶意注册（该值从验证随机码接口获取）
		params.put("username", username);// 用户注册名 本项目只允许邮箱注册 (客户端判断所填文本是否符合邮件格式)
		params.put("password", password);// 登陆密码
		params.put("nickname", nickname);// 用户昵称
		params.put("sex", sex);// 性别 男或女
		params.put("district_name", district_name);// 地区
		params.put("selfsign", selfsign);// 个性签名
		params.put("email", email);// 邮箱

		BaseNetTask task = new ExecuteNetTask(information, params);
		executeTask(task);
	}

	/**
	 * 上传文件（图片，音频，视频）
	 */
	public void fileUpload(String token, String keytype, String keyid,
						   String duration, String orderby, String content, String temp_file) {
		BaseHttpInformation information = BaseHttpInformation.FILE_UPLOAD;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);//
		params.put("keytype", keytype); //
		params.put("keyid", keyid); //
		params.put("duration", duration); //
		params.put("orderby", orderby); //
		params.put("content", content);// 内容描述 有的项目中，展示性图片需要附属一段文字说明信息。默认传"无"
		HashMap<String, String> files = new HashMap<String, String>();
		files.put("temp_file", temp_file); //

		BaseNetTask task = new ExecuteNetTask(information, params, files, FileUploadResult.class);
		executeTask(task);
	}


	/**
	 * 意见反馈
	 */
	public void adviceAdd(String token, String content) {
		BaseHttpInformation information = BaseHttpInformation.ADVICE_ADD;
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("token", token);// 登陆令牌
		params.put("content", content);// 意见内容

		BaseNetTask task = new ExecuteNetTask(information, params);
		executeTask(task);
	}
	/**
	 * 获取支付宝交易签名串
	 */
	public void alipay(String token, String keytype, String keyid) {
		BaseHttpInformation information = BaseHttpInformation.ALIPAY;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);// 登陆令牌
		params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
		params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id

		BaseNetTask task = new ExecuteNetTask(information, params, AlipayTrade.class);
		executeTask(task);
	}

	/**
	 * 获取微信预支付交易会话标识
	 */
	public void weixinpay(String token, String keytype, String keyid) {
		BaseHttpInformation information = BaseHttpInformation.WEIXINPAY;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);// 登陆令牌
		params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
		params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
		params.put("paytype", "0");// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
		params.put("total_fee", "0.01");// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id

		BaseNetTask task = new ExecuteNetTask(information, params, WeixinTrade.class);
		executeTask(task);
	}

	/**
	 * 获取银联交易签名串
	 */
	public void unionpay(String token, String keytype, String keyid) {
		BaseHttpInformation information = BaseHttpInformation.UNIONPAY;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);// 登陆令牌
		params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
		params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id

		BaseNetTask task = new ExecuteNetTask(information, params, UnionTrade.class);
		executeTask(task);
	}


}
