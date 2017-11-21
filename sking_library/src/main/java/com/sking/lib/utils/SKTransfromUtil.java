package com.sking.lib.utils;

import android.content.Context;

import com.sking.lib.exception.SKDataParseException;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class SKTransfromUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dipTopx(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodip(Context mContext, float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * DecimalFormat转换最简便,保留几位小数
     */
    public static String getDecimalFormatString(double f) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (f == 0)
            return "0.00";
        else
            return df.format(f);
    }

    /**
     * DecimalFormat转换最简便,保留几位小数
     * "#.00"
     */
    public static String getDecimalFormatString(double f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        if (f == 0)
            return "0.00";
        else
            return df.format(f);
    }

    /**
     * 封装本地json
     * param success 是否成功
     * param msg 提示信息
     * param infor 数据
     * param 201 数据解析错误
     */
    public static JSONObject getDefaultJson(boolean success, String msg, String infor) throws SKDataParseException {
        try {
            if (success)
                return new JSONObject("{\"success\":" + success + ",\"msg\":" + msg + ",\"infor\": \"" + infor + "\"}");
            else
                return new JSONObject("{\"success\":" + success + ",\"msg\":" + msg + ",\"error_code\":201 }");
        } catch (Exception e) {
            throw new SKDataParseException(e);
        }
    }
}
