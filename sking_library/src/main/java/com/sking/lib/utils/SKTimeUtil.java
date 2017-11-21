package com.sking.lib.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间相关工具类
 */
public class SKTimeUtil {

    /**
     * 获取系统当前时间
     * @return String
     */
    public static String getCurrentTime() {
        return String.valueOf(System.currentTimeMillis());
    }

	/**
	 * 获取系统当前时间
	 * @param format 时间格式yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String getCurrentTime(String format) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(format,
				Locale.getDefault());
		return dateFormat.format(date);
	}

	/**
	 * 转换时间显示形式
	 * 
	 * @param time
	 *            时间字符串yyyy-MM-dd HH:mm:ss
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String TransTime(String time, String format) {
		if ("0000-00-00 00:00:00".equals(time))
			return "0000-00-00 00:00:00";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		try {
			Date date1 = sdf.parse(time);
			SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());// "yyyy年MM月dd HH:mm"
			return dateFormat.format(date1);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 转换时间显示形式
	 *
	 * @param time
	 *            时间字符串yyyy-MM-dd HH:mm:ss
	 * @param context
	 *            context
	 * @return String
	 */
	public String getAfterTime(Context context,String time) {
		String timeStr = null;
        /* 先转成毫秒并求差 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long m =  new Date().getTime() - sdf.parse(time).getTime();
			int afterTime = (int) (m / (1000 * 60));
			if ((int) (m / (1000 * 60 * 60 * 24 * 30)) > 1) {
				timeStr = (int) m / (1000 * 60 * 60 * 24 * 30) + "月前";
			} else if ((int) (m / (1000 * 60 * 60)) > 24) {
				timeStr = (int) m / (1000 * 60 * 60 * 24) + "天前";
			} else if ((int) (m / (1000 * 60 * 60)) > 1) {
				timeStr = (int) m / (1000 * 60 * 60) + "小时前";
			} else if (afterTime < 60) {
				timeStr = afterTime + "分钟前";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStr;
	}
}
