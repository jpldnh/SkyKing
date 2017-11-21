package com.sking.lib.res.bases;

import com.sking.lib.base.SKApplication;
import com.sking.lib.config.SKConfig;
import com.sking.lib.res.config.SKRConfig;
import com.sking.lib.res.utils.SKRUtil;
import com.sking.lib.utils.SKLogger;


/**
 * 该项目自定义Application
 */
public abstract class SKRApplication extends SKApplication {

    private static final String TAG = "SKRApplication";

    private static SKRApplication application;

    public static SKRApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        application = this;
        SKConfig.TIMEOUT_CONNECT_HTTP = SKRConfig.TIMEOUT_HTTP;
        SKConfig.TRYTIMES_HTTP = SKRConfig.TRYTIMES_HTTP;
        SKRUtil.clearPerMission();
        super.onCreate();
//        /* 初始化UNcaughtException */
//        if(!SKConfig.DEBUG)
//            SKRUncaughtException.getInstance().init(application);
//		/* 初始化UNcaughtException end */
    }

    @Override
    public void onLowMemory() {
        SKLogger.w(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        SKLogger.w(TAG, "onTerminate");
        super.onTerminate();
    }

}
