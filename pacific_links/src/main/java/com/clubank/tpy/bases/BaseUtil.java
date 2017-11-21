package com.clubank.tpy.bases;

import android.content.Context;

import com.sking.lib.base.SKActivityManager;
import com.sking.lib.utils.SKTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 工具类
 */
public class BaseUtil {
	
	
	/**
	 * 退出软件
	 * 
	 * @param context
	 */
	public static void exit(Context context) {
		SKActivityManager.finishAll();
	}

	/**
	 * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
	 * 
	 * @param time
	 *            时间字符串
	 * @return String
	 */
	public static String transTimeChat(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			String current = SKTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			String dian24 = SKTimeUtil.TransTime(current, "yyyy-MM-dd")
					+ " 24:00:00";
			String dian00 = SKTimeUtil.TransTime(current, "yyyy-MM-dd")
					+ " 00:00:00";
			Date now = null;
			Date date = null;
			Date d24 = null;
			Date d00 = null;
			try {
				now = sdf.parse(current); // 将当前时间转化为日期
				date = sdf.parse(time); // 将传入的时间参数转化为日期
				d24 = sdf.parse(dian24);
				d00 = sdf.parse(dian00);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
			long min = diff / (60 * 1000);
			if (min <= 5)
				return "刚刚";
			if (min < 60)
				return min + "分钟前";

			if (now.getTime() <= d24.getTime()
					&& date.getTime() >= d00.getTime())
				return "今天" + SKTimeUtil.TransTime(time, "HH:mm");

			int sendYear = Integer
					.valueOf(SKTimeUtil.TransTime(time, "yyyy"));
			int nowYear = Integer.valueOf(SKTimeUtil.TransTime(current,
					"yyyy"));
			if (sendYear < nowYear)
				return SKTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
			else
				return SKTimeUtil.TransTime(time, "MM-dd HH:mm");
		} catch (Exception e) {
			return null;
		}
	}
	

	
}
