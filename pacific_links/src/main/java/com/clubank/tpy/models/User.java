package com.clubank.tpy.models;

import com.sking.lib.exception.SKDataParseException;
import com.sking.lib.res.bases.SKRUser;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 用户信息(注意User信息必须继承HemaUser,并且User中不用再包含token,android_must_update,
 * android_last_version,android_update_url字段)
 */
public class User extends SKRUser {

	private String id;// 用户主键
	private String username;// 登录名
	private String email;// 用户邮箱
	private String nickname;// 用户昵称
	private String mobile;// 手机号码 此为登录手机账户（一旦注册不能更改）
	private String password;// 登陆密码 服务器端存储的是32位的MD5加密串
	private String paypassword;// 支付密码 服务器端存储的是32位的MD5加密串
	private String sex;// 用户性别
	private String selfsign;// 个性签名
	private String realname;// 姓名
	private String company_id;// 所属公司主键 根据此值是否为空来判断是普通用户还是企业用户
	private String dept_id;// 所属公司主键
	private String worker;// 岗位（职位）名称
	private String sealflag;// 信息隐藏标记位 0：公开 1：隐藏
	private String adminflag;// 企业管理权限标记位 0：无 1：有（可移除员工）
	private String viptype;// 会员类别 1:1个月 2：6个月 3:12个月
	private String vipenddate;// 会员权限截止日期
	private String level;// 用户等级
	private String score;// 用户积分
	private String feeaccount;// 账户余额
	private String avatar;// 个人主页头像图片（小） 如果为空请显示系统默认头像（小）
	private String avatarbig;// 个人主页头像图片（大） 如果为空请显示系统默认头像（大）
	private String district_name;// 用户地区
	private String validflag;// 用户状态标记 0冻结1有效
	private String devicetype;// 用户客户端类型 1：苹果 2：安卓
	private String deviceid;// 客户端硬件标识码 等同百度推送userid
	private String chanelid;// 百度推送渠道id
	private String lastlogintime;// 最后一次登录的时间
	private String lastloginversion;// 最后一次登录的版本
	private String regdate;// 用户注册时间

	private String companyname;// 所属公司名称
	private String deptname;// 所属部门名称 多个模块均需用到
	private String level_name;// 用户等级中文名称 比如“初出茅庐”
	private String away_score;// 距离升到下一级别所需积分
	private String friendflag;// 好友标记 0需要显示“加为好友”,1需要显示“删除好友”

	public User(){
	};

	public User(JSONObject jsonObject) throws SKDataParseException {
		super(jsonObject);
		if (jsonObject != null) {
			try {
				id = get(jsonObject, "id");
				username = get(jsonObject, "username");
				email = get(jsonObject, "email");
				nickname = get(jsonObject, "nickname");
				mobile = get(jsonObject, "mobile");
				password = get(jsonObject, "password");
				paypassword = get(jsonObject, "paypassword");
				sex = get(jsonObject, "sex");
				selfsign = get(jsonObject, "selfsign");
				realname = get(jsonObject, "realname");
				company_id = get(jsonObject, "company_id");
				dept_id = get(jsonObject, "dept_id");
				worker = get(jsonObject, "worker");
				sealflag = get(jsonObject, "sealflag");
				adminflag = get(jsonObject, "adminflag");
				viptype = get(jsonObject, "viptype");
				vipenddate = get(jsonObject, "vipenddate");
				level = get(jsonObject, "level");
				score = get(jsonObject, "score");
				feeaccount = get(jsonObject, "feeaccount");
				avatar = get(jsonObject, "avatar");
				avatarbig = get(jsonObject, "avatarbig");
				district_name = get(jsonObject, "district_name");
				validflag = get(jsonObject, "validflag");
				devicetype = get(jsonObject, "devicetype");
				deviceid = get(jsonObject, "deviceid");
				chanelid = get(jsonObject, "chanelid");
				lastlogintime = get(jsonObject, "lastlogintime");
				lastloginversion = get(jsonObject, "lastloginversion");
				regdate = get(jsonObject, "regdate");

				companyname = get(jsonObject, "companyname");
				deptname = get(jsonObject, "deptname");
				away_score = get(jsonObject, "away_score");
				friendflag = get(jsonObject, "friendflag");

				log_i(toString());
			} catch (JSONException e) {
				throw new SKDataParseException(e);
			}
		}
	}

	public User(String id, String username, String email, String nickname,
				String mobile, String password, String paypassword, String sex,
				String selfsign, String realname, String company_id,
				String dept_id, String worker, String sealflag, String adminflag,
				String viptype, String vipenddate, String level, String score,
				String feeaccount, String avatar, String avatarbig,
				String district_name, String validflag, String devicetype,
				String deviceid, String chanelid, String lastlogintime,
				String lastloginversion, String regdate, String token,
				String companyname, String deptname, String level_name,
				String away_score, String friendflag) {
		super(token);
		this.id = id;
		this.username = username;
		this.email = email;
		this.nickname = nickname;
		this.mobile = mobile;
		this.password = password;
		this.paypassword = paypassword;
		this.sex = sex;
		this.selfsign = selfsign;
		this.realname = realname;
		this.company_id = company_id;
		this.dept_id = dept_id;
		this.worker = worker;
		this.sealflag = sealflag;
		this.adminflag = adminflag;
		this.viptype = viptype;
		this.vipenddate = vipenddate;
		this.level = level;
		this.score = score;
		this.feeaccount = feeaccount;
		this.avatar = avatar;
		this.avatarbig = avatarbig;
		this.district_name = district_name;
		this.validflag = validflag;
		this.devicetype = devicetype;
		this.deviceid = deviceid;
		this.chanelid = chanelid;
		this.lastlogintime = lastlogintime;
		this.lastloginversion = lastloginversion;
		this.regdate = regdate;
		this.companyname = companyname;
		this.deptname = deptname;
		this.level_name = level_name;
		this.away_score = away_score;
		this.friendflag = friendflag;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email
				+ ", nickname=" + nickname + ", mobile=" + mobile
				+ ", password=" + password + ", paypassword=" + paypassword
				+ ", sex=" + sex + ", selfsign=" + selfsign + ", realname="
				+ realname + ", company_id=" + company_id + ", dept_id="
				+ dept_id + ", worker=" + worker + ", sealflag=" + sealflag
				+ ", adminflag=" + adminflag + ", viptype=" + viptype
				+ ", vipenddate=" + vipenddate + ", level=" + level
				+ ", score=" + score + ", feeaccount=" + feeaccount
				+ ", avatar=" + avatar + ", avatarbig=" + avatarbig
				+ ", district_name=" + district_name + ", validflag="
				+ validflag + ", devicetype=" + devicetype + ", deviceid="
				+ deviceid + ", chanelid=" + chanelid + ", lastlogintime="
				+ lastlogintime + ", lastloginversion=" + lastloginversion
				+ ", regdate=" + regdate + ", companyname=" + companyname
				+ ", deptname=" + deptname + ", level_name=" + level_name
				+ ", away_score=" + away_score + ", friendflag=" + friendflag
				+ "]";
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the paypassword
	 */
	public String getPaypassword() {
		return paypassword;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @return the selfsign
	 */
	public String getSelfsign() {
		return selfsign;
	}

	/**
	 * @return the realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * @return the company_id
	 */
	public String getCompany_id() {
		return company_id;
	}

	/**
	 * @return the worker
	 */
	public String getWorker() {
		return worker;
	}

	/**
	 * @return the sealflag
	 */
	public String getSealflag() {
		return sealflag;
	}

	/**
	 * @return the adminflag
	 */
	public String getAdminflag() {
		return adminflag;
	}

	/**
	 * @return the viptype
	 */
	public String getViptype() {
		return viptype;
	}

	/**
	 * @return the vipenddate
	 */
	public String getVipenddate() {
		return vipenddate;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @return the feeaccount
	 */
	public String getFeeaccount() {
		return feeaccount;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @return the avatarbig
	 */
	public String getAvatarbig() {
		return avatarbig;
	}

	/**
	 * @return the district_name
	 */
	public String getDistrict_name() {
		return district_name;
	}

	/**
	 * @return the validflag
	 */
	public String getValidflag() {
		return validflag;
	}

	/**
	 * @return the devicetype
	 */
	public String getDevicetype() {
		return devicetype;
	}

	/**
	 * @return the deviceid
	 */
	public String getDeviceid() {
		return deviceid;
	}

	/**
	 * @return the chanelid
	 */
	public String getChanelid() {
		return chanelid;
	}

	/**
	 * @return the lastlogintime
	 */
	public String getLastlogintime() {
		return lastlogintime;
	}

	/**
	 * @return the lastloginversion
	 */
	public String getLastloginversion() {
		return lastloginversion;
	}

	/**
	 * @return the regdate
	 */
	public String getRegdate() {
		return regdate;
	}

	/**
	 * @return the friendflag
	 */
	public String getFriendflag() {
		return friendflag;
	}

	/**
	 * @param friendflag
	 *            the friendflag to set
	 */
	public void setFriendflag(String friendflag) {
		this.friendflag = friendflag;
	}

	/**
	 * @return the dept_id
	 */
	public String getDept_id() {
		return dept_id;
	}

	/**
	 * @return the companyname
	 */
	public String getCompanyname() {
		return companyname;
	}

	/**
	 * @return the deptname
	 */
	public String getDeptname() {
		return deptname;
	}

	/**
	 * @return the level_name
	 */
	public String getLevel_name() {
		return level_name;
	}

	/**
	 * @return the away_score
	 */
	public String getAway_score() {
		return away_score;
	}

}
