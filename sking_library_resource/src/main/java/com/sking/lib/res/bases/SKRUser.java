package com.sking.lib.res.bases;

import com.sking.lib.base.SKObject;
import com.sking.lib.exception.SKDataParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 用户信息
 */
public class SKRUser extends SKObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7023376661413431880L;
	private static String token;// 登陆令牌 由系统随机生成，作为后续请求服务的必传字段。
	private String android_must_update;// 安卓强制更新标记0：不强制1：强制（当软件架构进行了较大变动，客户端必须强制用户升级到最新版本）
	private String android_last_version;// 安卓最新版本号 将该信息与安卓本机版本号比对，如果不相等，则提醒在线升级
	private String android_update_url;// 安卓软件更新地址,类似
										// http://192.168.2.146:8008/group1/hm_php/download/yourprojectname.apk
	public SKRUser() {};

	public SKRUser(JSONObject jsonObject) throws SKDataParseException {
		if (jsonObject != null) {
			try {
				String token = get(jsonObject, "token");
				if (!isNull(token))
					SKRUser.token = token;
				android_must_update = get(jsonObject, "android_must_update");
				android_last_version = get(jsonObject, "android_last_version");
				android_update_url = get(jsonObject, "android_update_url");
			} catch (JSONException e) {
				throw new SKDataParseException(e);
			}
		}
	}

	public SKRUser(String token) {
		SKRUser.token = token;
	}

	/**
	 * @return the token
	 */
	public final String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public final void setToken(String token) {
		SKRUser.token = token;
	}

	/**
	 * @return the android_must_update
	 */
	public String getAndroid_must_update() {
		return android_must_update;
	}

	/**
	 * @return the android_last_version
	 */
	public String getAndroid_last_version() {
		return android_last_version;
	}

	/**
	 * @return the android_update_url
	 */
	public String getAndroid_update_url() {
		return android_update_url;
	}

}
