package com.skingapp.commen.models;

import com.sking.lib.base.SKObject;
import com.sking.lib.exception.SKDataParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseMsg extends SKObject {

	private String id;
	private String username;
	private String nickname;
	private String mobileNumber;
	private String password;
	private String sex;
	private String mobile;
	private String avatar;
	private String avatarbig;
	private String district_name;
	private String onlineflag;
	private String validflag;
	private String devicetype;
	private String channelid;
	private String lastlogintime;
	private String lastloginversion;
	private String regdate;
	private String token;
	private String android_must_update;
	private String android_last_version;
	private String android_update_url;
	
	public ResponseMsg(JSONObject jsonObject) throws SKDataParseException {
		if (jsonObject != null) {
			try {
				id = get(jsonObject, "id");
				username = get(jsonObject, "username");
				nickname = get(jsonObject, "nickname");
				mobileNumber = get(jsonObject, "mobileNumber");
				password = get(jsonObject, "password");
				sex = get(jsonObject, "sex");
				mobile = get(jsonObject, "mobile");
				avatar = get(jsonObject, "avatar");
				avatarbig = get(jsonObject, "avatarbig");
				district_name = get(jsonObject, "district_name");
				onlineflag = get(jsonObject, "onlineflag");
				validflag = get(jsonObject, "validflag");
				devicetype = get(jsonObject, "devicetype");
				channelid = get(jsonObject, "channelid");
				lastlogintime = get(jsonObject, "lastlogintime");
				lastloginversion = get(jsonObject, "lastloginversion");
				regdate = get(jsonObject, "regdate");
				token = get(jsonObject, "token");
				android_must_update = get(jsonObject, "android_must_update");
				android_last_version = get(jsonObject, "android_last_version");
				android_update_url = get(jsonObject, "android_update_url");

				log_i(toString());
			} catch (JSONException e) {
				throw new SKDataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", username=" + username + ", nickname="
				+ nickname + ", mobileNumber=" + mobileNumber + ", password=" + password
				+ ", sex=" + sex + ", mobile=" + mobile
				+ ", avatar=" + avatar + ", avatarbig=" + avatarbig
				+ ", district_name=" + district_name + ", onlineflag=" + onlineflag
				+ ", validflag=" + validflag + ", devicetype=" + devicetype
				+ ", channelid=" + channelid + ", lastlogintime=" + lastlogintime + ", lastloginversion="
				+ lastloginversion +", regdate="+ regdate +", token="+ token +", android_must_update="
				+ android_must_update +", android_last_version="+ android_last_version 
				+", android_update_url="+ android_update_url + "]";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getAvatarbig() {
		return avatarbig;
	}
	public void setAvatarbig(String avatarbig) {
		this.avatarbig = avatarbig;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	public String getOnlineflag() {
		return onlineflag;
	}
	public void setOnlineflag(String onlineflag) {
		this.onlineflag = onlineflag;
	}
	public String getValidflag() {
		return validflag;
	}
	public void setValidflag(String validflag) {
		this.validflag = validflag;
	}
	public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getLastlogintime() {
		return lastlogintime;
	}
	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	public String getLastloginversion() {
		return lastloginversion;
	}
	public void setLastloginversion(String lastloginversion) {
		this.lastloginversion = lastloginversion;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAndroid_must_update() {
		return android_must_update;
	}
	public void setAndroid_must_update(String android_must_update) {
		this.android_must_update = android_must_update;
	}
	public String getAndroid_last_version() {
		return android_last_version;
	}
	public void setAndroid_last_version(String android_last_version) {
		this.android_last_version = android_last_version;
	}
	public String getAndroid_update_url() {
		return android_update_url;
	}
	public void setAndroid_update_url(String android_update_url) {
		this.android_update_url = android_update_url;
	}

}
