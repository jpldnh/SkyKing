package com.sking.lib.base;

import com.sking.lib.utils.SKBaseUtil;
import com.sking.lib.utils.SKLogger;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 相当于Object，集成了log_v(msg)等打印方法以及println(Object)。
 */
public class SKObject {
    /**
     * 打印TAG，类名
     */
    private String TAG;

    public SKObject() {
        TAG = getLogTag();
    }

    /**
     * 获取打印TAG，即类名
     *
     * @return
     */
    private String getLogTag() {
        return getClass().getSimpleName();
    }

    /**
     * 打印v级别信息
     *
     * @param msg
     */
    protected void log_v(String msg) {
        SKLogger.v(TAG, msg);
    }

    /**
     * 打印d级别信息
     *
     * @param msg
     */
    protected void log_d(String msg) {
        SKLogger.d(TAG, msg);
    }

    /**
     * 打印i级别信息
     *
     * @param msg
     */
    protected void log_i(String msg) {
        SKLogger.i(TAG, msg);
    }

    /**
     * 打印w级别信息
     *
     * @param msg
     */
    protected void log_w(String msg) {
        SKLogger.w(TAG, msg);
    }

    /**
     * 打印e级别信息
     *
     * @param msg
     */
    protected void log_e(String msg) {
        SKLogger.e(TAG, msg);
    }

    /**
     * 打印
     *
     * @param msg
     */
    protected void println(Object msg) {
        SKLogger.println(msg);
    }

    /**
     * 解析时，判断是否为空
     *
     * @param jsonObject
     * @param s
     * @return
     * @throws JSONException
     */
    protected String get(JSONObject jsonObject, String s) throws JSONException {
        if (!jsonObject.isNull(s)) {
            return jsonObject.getString(s);
        }
        return null;
    }

    /**
     * 解析时，判断是否为空
     *
     * @param jsonObject
     * @param s
     * @return 若为空返回0
     * @throws JSONException
     */
    protected int getInt(JSONObject jsonObject, String s) throws JSONException {
        if (!jsonObject.isNull(s)) {
            return jsonObject.getInt(s);
        }
        return 0;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true如果该字符串为null或者"",否则false
     */
    protected static boolean isNull(String str) {
        return SKBaseUtil.isNull(str);
    }

    /**
     * 判断字符是否相等
     *
     * @param str
     * @return true相等, 否则false
     */
    protected static boolean isEquals(Object str, Object str2) {
        return SKBaseUtil.isEquals(str, str2);
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isFloat(value);
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isFloat(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是xml结构
     */
    public static boolean isXML(String value) {
        return value.contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    }

}
