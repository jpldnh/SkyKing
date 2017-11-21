package com.skingapp.commen.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 */
public class BaseDBHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "demo.db";
	/**
	 * 系统初始化信息
	 */
	protected static final String SYSINITINFO = "sysinfo";
	/**
	 * 当前登录用户信息
	 */
	protected static final String USER = "user";

	public BaseDBHelper(Context context) {
		super(context, DBNAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建系统初始化信息缓存表
		String sys = "sys_web_service text,sys_plugins text,sys_appkey text,start_img text,android_must_update text,android_last_version text,iphone_must_update text,"
				+ "iphone_last_version text,sys_chat_ip text,sys_chat_port text,sys_pagesize text,sys_service_phone text,android_update_url text,"
				+ "iphone_update_url text,apad_update_url text,ipad_update_url text,iphone_comment_url text,msg_invite text";
		String sysSQL = "create table " + SYSINITINFO
				+ " (id integer primary key," + sys + ")";
		db.execSQL(sysSQL);
		// 创建当前登录用户信息缓存表
		String user = "id text,username text,email text,nickname text,mobile text,password text,paypassword text,sex text,selfsign text,realname text,company_id text,dept_id text,worker text,sealflag text,"
				+ "adminflag text,viptype text,vipenddate text,level text,score text,feeaccount text,avatar text,avatarbig text,district_name text,validflag text,devicetype text,deviceid text,"
				+ "chanelid text,lastlogintime text,lastloginversion text,regdate text,token text,companyname text,deptname text,level_name text,away_score text,friendflag text";
		String userSQL = "create table " + USER + " (" + user + ")";
		db.execSQL(userSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {

	}

}
