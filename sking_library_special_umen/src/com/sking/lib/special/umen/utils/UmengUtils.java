package com.sking.lib.special.umen.utils;

/**
 * Created by Im_jingwei on 2017/9/18.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sking.lib.special.umen.push.UmengMessageReceiver;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.common.UmLog;

/**
 * Created by jw on 2017/3/23.
 * Describe: 统计,推送配置初始化
 */

public class UmengUtils {

    private static final String TAG = UmengUtils.class.getSimpleName();
    private static String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

    /**
     * 在Application中做的初始化
     */
    public static void init(final Context context, UmengMessageReceiver umengMessageReceiver) {

        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setDebugMode(true);

        MobclickAgent.openActivityDurationTrack(false);//统计
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);//统计类型

        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE); //sdk开启通知声音
        mPushAgent.setMessageHandler(umengMessageReceiver); //定义消息接收处理
        mPushAgent.setNotificationClickHandler(umengMessageReceiver.notificationClick);//点击事件
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                UmLog.i(TAG, "ym_device_token: " + deviceToken);
                context.sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }

            @Override
            public void onFailure(String s, String s1) {
                UmLog.e(TAG, "ym_register_failed: " + s + " " +s1);
                context.sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
            }
        });
    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onResume加入
     *
     * @param context
     */
    public static void onActivityResume(Context context) {
        MobclickAgent.onPageStart(context.getClass().getSimpleName());
        MobclickAgent.onResume(context);
        Log.i("UmengAnalytics","ActivityResume_"+context.getClass().getSimpleName());
    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onPause加入
     *
     * @param context
     */
    public static void onActivityPause(Context context) {
        MobclickAgent.onPageEnd(context.getClass().getSimpleName());
        MobclickAgent.onPause(context);
        Log.i("UmengAnalytics","ActivityPause_"+context.getClass().getSimpleName());
    }

    /**
     * 在BaseFragment中的onResume加入
     *
     * @param context
     */
    public static void onFragmentResume(Context context) {
        MobclickAgent.onPageStart(context.getClass().getSimpleName());
        Log.i("UmengAnalytics","FragmentResume_"+context.getClass().getSimpleName());
    }

    /**
     * 在BaseFragment中的onPause加入
     *
     * @param context
     */
    public static void onFragmentPause(Context context) {
        MobclickAgent.onPageEnd(context.getClass().getSimpleName());
        Log.i("UmengAnalytics","FragmentPause_"+context.getClass().getSimpleName());
    }

    /**
     * 在登录成功的地方调用
     *
     * @param userId 用户id
     */
    public static void loginIn(String userId) {
        MobclickAgent.onProfileSignIn(userId);
        Log.i("UmengAnalytics","loginIn_userId="+userId);
    }

    /**
     * 在退出登录的地方调用
     */
    public static void logOut() {
        MobclickAgent.onProfileSignOff();
        Log.i("UmengAnalytics","loginOut");
    }

}
