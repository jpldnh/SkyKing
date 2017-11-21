package com.sking.lib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Im_jingwei on 2017/10/20.
 * dec
 * Manifest.permission.RECORD_AUDIO 录制音频
 * Manifest.permission.GET_ACCOUNTS 访问帐户列表
 * Manifest.permission.READ_PHONE_STATE 手机状态
 * Manifest.permission.CALL_PHONE 拨打电话
 * Manifest.permission.CAMERA  使用相机
 * anifest.permission.ACCESS_FINE_LOCATION  访问CellID或WiFi热点来获取粗略的位置
 * Manifest.permission.ACCESS_COARSE_LOCATION 访问CellID或WiFi热点来获取粗略的位置
 * Manifest.permission.READ_EXTERNAL_STORAGE 使用外置存储
 * Manifest.permission.WRITE_EXTERNAL_STORAGE 使用外置存储
 */

public class SKPermissionUtil {

    private static final String TAG = SKPermissionUtil.class.getSimpleName();
    private static String[] requestPermissions;

    /**
     * 获取应用权限 名称列表
     */
    public static String[] getAppPermissionsList(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.requestedPermissions;
    }


    /**
     * @param
     * @time 2017/10/17  16:49
     * @author Im_jingwei
     */
    public static void requesetPermission(final Activity activity, String[] permissionArray) {

        if (requestPermissions != null)
            requestPermissions = null;
        requestPermissions = permissionArray;

        ArrayList<String> refusePermissions = getPermissionList(activity, permissionArray, true);
        ArrayList<String> refuseNoRemindPermissions = getPermissionList(activity, permissionArray, false);

        if (refuseNoRemindPermissions.size() == permissionArray.length) {
            ActivityCompat.requestPermissions(activity, permissionArray, 0x1000);
        } else if (refusePermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity, refusePermissions.toArray(new String[refusePermissions.size()]), 0x1000);
            Log.d(TAG, "requesetPermission: " + refusePermissions.toString());
        } else if (refuseNoRemindPermissions.size() > 0) {
            showRemindDialog(activity, refuseNoRemindPermissions);
        }
    }

    //授权结果
    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < grantResults.length; i++) {
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                    //用户禁止
                    Log.i("permission","用户禁止！");
                } else {
                    //用户选择不再提示，禁止权限
                    Log.i("permission","用户禁止并且不再提示！");
                }
                list.add(permissions[i]);
            } else {
                //获取授权
//                onResume();
                Log.i("permission","已有授权！");
            }
        }

        ArrayList<String> refuseNoRemindPermissions = getPermissionList(activity, requestPermissions, false);
        if (refuseNoRemindPermissions.size() > 0)
            showRemindDialog(activity, refuseNoRemindPermissions);

        for (int per : grantResults)
            if (per == -1) {
                Toast.makeText(activity, getPerName(list)+ "授权失败,可能会导致程序异常退出请允许相关授权！", Toast.LENGTH_LONG).show();
                return;
            }

        Log.i("permission result:", Arrays.toString(permissions));
    }

    /**
     * @param isRemind true 返回未授权的权限, false 表示返回用户选择不再提醒的权限
     * @return
     * @throws
     * @time 2017/10/20  12:02
     * @author Im_jingwei
     * @desc
     */
    private static ArrayList<String> getPermissionList(Activity activity, String[] permissonArray, boolean isRemind) {
        ArrayList<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissonArray.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissonArray[i]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissonArray[i])) {
                    if (isRemind)
                        permissionList.add(permissonArray[i]);
                } else {
                    if (!isRemind)
                        permissionList.add(permissonArray[i]);
                }
            }
        }
        return permissionList;
    }

    /*
    * 显示选择dialog
    * */
    private static void showRemindDialog(final Activity activity, ArrayList<String> refuseNoRemindPermissions) {
        new AlertDialog.Builder(activity)
                .setMessage(getPerName(refuseNoRemindPermissions) + "被禁止用户选择不再提示请手动开启！")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSettingActivity(activity);
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }

    /*
    * 打开设置界面
    */
    private static void openSettingActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Log.d(TAG, "getPackageName(): " + activity.getPackageName());
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    /*
     * 根据集合返回对应名称
     */
    private static String getPerName(ArrayList<String> strArray) {
        StringBuffer strBuffer = new StringBuffer();
        for (String name : strArray) {
            strBuffer.append(getPerName(name));
        }
        return strBuffer.toString();
    }

    /*
     * 根据集合返回对应名称
     */
    private static String getPerName(String perName) {
        String strName = "";
        switch (perName) {
            case "android.permission.RECORD_AUDIO":
                strName = "音频权限 ";
                break;
            case "android.permission.GET_ACCOUNTS":
                strName = "账户权限 ";
                break;
            case "android.permission.READ_PHONE_STATE":
                strName = "读取设备状态权限 ";
                break;
            case "android.permission.CALL_PHONE":
                strName = "通讯录权限 ";
                break;
            case "android.permission.CAMERA":
                strName = "相机权限 ";
                break;
            case "android.permission.ACCESS_FINE_LOCATION":
            case "android.permission.ACCESS_COARSE_LOCATION":
                strName = "地理位置权限 ";
                break;
            case "android.permission.READ_EXTERNAL_STORAGE":
            case "android.permission.WRITE_EXTERNAL_STORAGE":
                strName = "文件操作权限 ";
                break;
        }
        return strName;
    }

}
