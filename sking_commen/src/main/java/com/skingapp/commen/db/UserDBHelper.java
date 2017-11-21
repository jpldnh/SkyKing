package com.skingapp.commen.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.skingapp.commen.models.User;


/**
 * 用户信息数据库帮助类
 */
public class UserDBHelper extends BaseDBHelper {
	String tableName = USER;

	String columns = "id,username,email,nickname,mobile,password,paypassword,sex,selfsign,realname,company_id,dept_id,worker,sealflag,"
			+ "adminflag,viptype,vipenddate,level,score,feeaccount,avatar,avatarbig,district_name,validflag,devicetype,deviceid,"
			+ "chanelid,lastlogintime,lastloginversion,regdate,token,companyname,deptname,level_name,away_score,friendflag";

	String updateColumns = "id=?,username=?,email=?,nickname=?,mobile=?,password=?,paypassword=?,sex=?,selfsign=?,realname=?,company_id=?,dept_id=?,worker=?,sealflag=?,"
			+ "adminflag=?,viptype=?,vipenddate=?,level=?,score=?,feeaccount=?,avatar=?,avatarbig=?,district_name=?,validflag=?,devicetype=?,deviceid=?,"
			+ "chanelid=?,lastlogintime=?,lastloginversion=?,regdate=?,token=?,companyname=?,deptname=?,level_name=?,away_score=?,friendflag=?";

	/**
	 * 实例化系统初始化信息数据库帮助类
	 * 
	 * @param context
	 */
	public UserDBHelper(Context context) {
		super(context);
	}

	public boolean insertOrUpdate(User user) {
		if (isExist(user)) {
			return update(user);
		} else {
			return insert(user);
		}
	}

	/**
	 * 插入一条记录
	 * 
	 * @return 是否成功
	 */
	public boolean insert(User user) {
		String sql = "insert into "
				+ tableName
				+ " ("
				+ columns
				+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Object[] bindArgs = new Object[] { user.getId(), user.getUsername(),
				user.getEmail(), user.getNickname(), user.getMobile(),
				user.getPassword(), user.getPaypassword(), user.getSex(),
				user.getSelfsign(), user.getRealname(), user.getCompany_id(),
				user.getDept_id(), user.getWorker(), user.getSealflag(),
				user.getAdminflag(), user.getViptype(), user.getVipenddate(),
				user.getLevel(), user.getScore(), user.getFeeaccount(),
				user.getAvatar(), user.getAvatarbig(), user.getDistrict_name(),
				user.getValidflag(), user.getDevicetype(), user.getDeviceid(),
				user.getChanelid(), user.getLastlogintime(),
				user.getLastloginversion(), user.getRegdate(), user.getToken(),
				user.getCompanyname(), user.getDeptname(),
				user.getLevel_name(), user.getAway_score(),
				user.getFriendflag() };

		SQLiteDatabase db = getWritableDatabase();
		boolean success = true;
		try {
			db.execSQL(sql, bindArgs);
		} catch (SQLException e) {
			success = false;
		}
		db.close();
		return success;
	}

	/**
	 * 更新
	 * 
	 * @return 是否成功
	 */
	public boolean update(User user) {
		String conditions = " where id=" + user.getId();
		String sql = "update " + tableName + " set " + updateColumns
				+ conditions;
		Object[] bindArgs = new Object[] { user.getId(), user.getUsername(),
				user.getEmail(), user.getNickname(), user.getMobile(),
				user.getPassword(), user.getPaypassword(), user.getSex(),
				user.getSelfsign(), user.getRealname(), user.getCompany_id(),
				user.getDept_id(), user.getWorker(), user.getSealflag(),
				user.getAdminflag(), user.getViptype(), user.getVipenddate(),
				user.getLevel(), user.getScore(), user.getFeeaccount(),
				user.getAvatar(), user.getAvatarbig(), user.getDistrict_name(),
				user.getValidflag(), user.getDevicetype(), user.getDeviceid(),
				user.getChanelid(), user.getLastlogintime(),
				user.getLastloginversion(), user.getRegdate(), user.getToken(),
				user.getCompanyname(), user.getDeptname(),
				user.getLevel_name(), user.getAway_score(),
				user.getFriendflag() };

		SQLiteDatabase db = getWritableDatabase();
		boolean success = true;
		try {
			db.execSQL(sql, bindArgs);
		} catch (SQLException e) {
			success = false;
		}
		db.close();
		return success;
	}

	public boolean isExist(User user) {
		String id = user.getId();
		String sql = ("select * from " + tableName + " where id=" + id);
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		boolean exist = cursor.getCount() > 0;
		cursor.close();
		db.close();
		return exist;
	}

	/**
	 * 清空
	 */
	public void clear() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from " + tableName);
		db.close();
	}

	/**
	 * 判断表是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + tableName, null);
		boolean empty = 0 == cursor.getCount();
		cursor.close();
		db.close();
		return empty;
	}

	/**
	 * @return 用户信息
	 */
	public User select(String username) {
		String conditions = " where username='" + username + "'";
		String sql = "select " + columns + " from " + tableName + conditions;

		SQLiteDatabase db = getWritableDatabase();
		User user = null;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			user = new User(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5),
					cursor.getString(6), cursor.getString(7),
					cursor.getString(8), cursor.getString(9),
					cursor.getString(10), cursor.getString(11),
					cursor.getString(12), cursor.getString(13),
					cursor.getString(14), cursor.getString(15),
					cursor.getString(16), cursor.getString(17),
					cursor.getString(18), cursor.getString(19),
					cursor.getString(20), cursor.getString(21),
					cursor.getString(22), cursor.getString(23),
					cursor.getString(24), cursor.getString(25),
					cursor.getString(26), cursor.getString(27),
					cursor.getString(28), cursor.getString(29),
					cursor.getString(30), cursor.getString(31),
					cursor.getString(32), cursor.getString(33),
					cursor.getString(34), cursor.getString(35)

			);
		}
		cursor.close();
		db.close();
		return user;
	}
}
