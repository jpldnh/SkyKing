package com.sking.lib.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * 获取硬件标识码
 */
public class SKDeviceUtil {
    private static final String PREFS_DEVICE_ID = "device_id";
    private static UUID uuid;

    public static String getDeviceUuid(Context context) {
        if (uuid == null) {
            String id = SKSharedPreferencesUtil.get(context, PREFS_DEVICE_ID);
            if (id != null) {
                // Use the ids previously computed and stored in the
                // prefs file
                uuid = UUID.fromString(id);
            } else {
                final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                // Use the Android ID unless it's broken, in which case
                // fallback on deviceId,
                // unless it's not available, then fallback on a random
                // number which we store
                // to a prefs file
                try {
                    if (!"9774d56d682e549c".equals(androidId)) {
                        uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                    } else {
                        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                        uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                // Write the value out to the prefs file
                SKSharedPreferencesUtil.save(context, PREFS_DEVICE_ID, uuid.toString());
            }
        }
        return uuid.toString();
    }

    /**
     * 获取手机IMEI串号
     * GSM是 IMEI，CDMA为MEID.
     *
     * @param context
     */
    public static String getPhoneImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm == null ? "" : tm.getDeviceId();
    }

    /**
     * 获取手机IMSI
     *
     * @param context
     */
    public static String getPhoneSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm == null ? "" : tm.getSubscriberId();
    }

    //获取sim卡iccid
    public static String getIccid(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm == null ? "" : tm.getSimSerialNumber();
    }

    /**
     * 获取手机号
     *
     * @param context
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm == null ? "" : tm.getLine1Number();
    }

    /**
     * 判断是否是有网络
     * @return
     * @author jw
     * @date 2017.08.29
     */
    public static boolean hasNetWork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();// 获取可用的网络服务
        return info != null && info.isAvailable();
    }

    /**
     *  @time 2017/11/8  11:35
     *  @author Im_jingwei
     *  @desc 返回当前网络环境
     */
    public static String getNetype(Context context) {
        String netType = "";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null)
            return netType;
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
                netType = "数据net网络";
            else
                netType = "数据wap网络";
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "wifi网络";
        } else if (nType == ConnectivityManager.TYPE_VPN) {
            netType = "vpn网络";
        } else {
            netType = "未知网络环境";
        }
        return netType;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    /**
     * 获取当前手机系统版本号
     */
    public static String getAndroidSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机信息
     *
     * @param context
     */
    public static String getDeviceInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuffer sb = new StringBuffer();
        sb.append("DeviceInfo: ");
        try {
            sb.append("\nAppVersion: " + SKBaseUtil.getAppVersionName(context));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sb.append("\nDeviceModel: " + getPhoneModel());
        sb.append("\nDeviceBrand: " + getPhoneBrand());
        sb.append("\nAndroidVersion: " + getAndroidSystemVersion());
        sb.append("\nNetworkState: " + hasNetWork(context));
        sb.append("\nNetworkInfo: " + getNetype(context));
        sb.append("\nNetworkOperator: " + tm.getNetworkOperator());//移动运营商编号
        sb.append("\nNetworkOperatorName: " + tm.getNetworkOperatorName());//移动运营商名称
        sb.append("\nLine1Number: " + tm.getLine1Number());
        sb.append("\nSimCountryIso: " + tm.getSimCountryIso());
        sb.append("\nSimOperator: " + tm.getSimOperator());
        sb.append("\nSimOperatorName: " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber(ICCID): " + tm.getSimSerialNumber());
        sb.append("\nSimSubscriberId(IMSI): " + tm.getSubscriberId());
        sb.append("\nDeviceIMEI(MEID): " + tm.getDeviceId());
        sb.append("\nDeviceUdid: " + uuid);
        sb.append("\n");
        return sb.toString();
    }

}
