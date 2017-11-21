package com.skingapp.commen.bases;

import com.sking.lib.config.SKConfig;
import com.sking.lib.res.bases.SKRApplication;
import com.sking.lib.res.config.SKRConfig;
import com.sking.lib.utils.SKLogger;
import com.sking.lib.utils.SKSharedPreferencesUtil;
import com.skingapp.commen.config.BaseConfig;
import com.skingapp.commen.db.SysInfoDBHelper;
import com.skingapp.commen.db.UserDBHelper;
import com.skingapp.commen.models.SysInitInfo;
import com.skingapp.commen.models.User;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;


/**
 * 该项目自定义Application
 */
public class BaseApplication extends SKRApplication {
    private static final String TAG = BaseApplication.class.getSimpleName();
    private SysInitInfo sysInitInfo;// 系统初始化信息
    private User user;
    private static BaseApplication application;

    public static BaseApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        application = this;
        SKConfig.DEBUG = BaseConfig.DEBUG;
        SKRConfig.UMENG_ENABLE = BaseConfig.UMENG_ENABLE;
        String iow = SKSharedPreferencesUtil.get(this, "imageload_onlywifi");
        SKConfig.IMAGELOAD_ONLYWIFI = "true".equals(iow);
        SKConfig.DIGITAL_CHECK = true;
        SKConfig.DATAKEY = BaseConfig.DATAKEY;
        SKRConfig.APPLICATION_KEY = BaseConfig.APPLICATION_KEY;
        SKRConfig.IS_OPEN_UP_SWIP = BaseConfig.IS_OPEN_UP_SWIP;//是否开始仿照IOS侧滑关闭
        initHotfix();//初始化阿里hotfix
//        SKBaseUtil.initLocalCRT(this,"xx.crt","www.sking.com");//启用本地证书验证，crt或cer证书文件名称，证书中的根域名
        SKLogger.i(TAG, "onCreate");
        super.onCreate();

        /* 友盟相关 */
//        UmengUtils.init(this, new UmPushMessageReceiver());
        /* 友盟相关end */

        /* 极光推送相关 */
//        JPushInterface.setDebugMode(BaseConfig.DEBUG);
//        JPushInterface.init(this);
       /* 极光推送相关 */

       /* 百度推送相关 */
//		FrontiaApplication.initFrontiaApplication(this);
        /* 百度推送相关end */
    }

    @Override
    public void onLowMemory() {
        SKLogger.i(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        SKLogger.i(TAG, "onTerminate");
        super.onTerminate();
    }

    /**
     * @return 当前用户
     */
    public User getUser() {
        if (user == null) {
            UserDBHelper helper = new UserDBHelper(this);
            String username = SKSharedPreferencesUtil.get(this, "username");
            user = helper.select(username);
            helper.close();
        }
        return user;
    }

    /**
     * 设置保存当前用户
     * <p>
     * 当前用户
     */
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            UserDBHelper helper = new UserDBHelper(this);
            helper.insertOrUpdate(user);
            helper.close();
        }
    }

    /**
     * @return 系统初始化信息
     */
    public SysInitInfo getSysInitInfo() {
        if (sysInitInfo == null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            sysInitInfo = helper.select();
            helper.close();
        }
        return sysInitInfo;
    }

    /**
     * 设置保存系统初始化信息
     *
     * @param sysInitInfo 系统初始化信息
     */
    public void setSysInitInfo(SysInitInfo sysInitInfo) {
        this.sysInitInfo = sysInitInfo;
        if (sysInitInfo != null) {
            SysInfoDBHelper helper = new SysInfoDBHelper(this);
            helper.insertOrUpdate(sysInitInfo);
            helper.close();
        }
    }

    /**
     * @param
     * @time 2017/10/12  11:24
     * @author Im_jingwei
     * @desc 初始化hotfix
     */
    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" Code:").append(code)
                                .append(" Info:").append(info)
                                .append(" HandlePatchVersion:").append(handlePatchVersion).toString();
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            SophixManager.getInstance().killProcessSafely();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

}
