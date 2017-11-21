package com.sking.lib.special.umen.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Im_jingwei on 2017/9/19.
 */

public class UmengEventUtils {

    private static final String DEVICEID = "deviceid";
    private static final String IMSI = "imsi";
    private static final String PHONEMODEL = "phone_model";
    private static final String USERID = "userid";
    private static final String PHONE = "phone";

    /**
     * 首次安装
     *
     * @param context
     */
    public static void installEvent(Context context) {
        HashMap<String, String> map = getInstallMap(context);
        MobclickAgent.onEvent(context, "install", map);
        Log.i("UmengAnalyticsEvent","installEvent");
    }

    /**
     * 登录
     *
     * @param ctx
     * @param userId
     * @param phone
     */
    public static void loginEvent(Context ctx, String userId, String phone) {
        HashMap<String, String> map = getInstallMap(ctx);
        map.put(USERID, userId);
        map.put(PHONE, phone);
        MobclickAgent.onEvent(ctx, "login", map);
        Log.i("UmengAnalyticsEvent","loginEvent");
    }

    /**
     * 退出
     *
     * @param ctx
     * @param userId
     */
    public static void logoutEvent(Context ctx, String userId) {
        HashMap<String, String> map = getInstallMap(ctx);
        map.put(USERID, userId);
        MobclickAgent.onEvent(ctx, "logout", map);
        Log.i("UmengAnalyticsEvent","logoutEvent");
    }



    private static HashMap<String, String> getInstallMap(Context ctx) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(PHONEMODEL, android.os.Build.MODEL);
        map.put(DEVICEID, getDeviceId(ctx));
        map.put(IMSI, getImsi(ctx));
        return map;
    }

    private static String getDeviceId(Context context){
        String id = "";
        String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        try {
            id = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static String getImsi(Context context)
    {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }
}
