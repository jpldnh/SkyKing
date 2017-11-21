package com.sking.lib.res.bases;

import android.content.Context;

import com.sking.lib.net.SKNetWorker;
import com.sking.lib.res.config.SKRConfig;

/**
 * 网络请求工具类
 */
public abstract class SKRNetWorker extends SKNetWorker {

    public SKRNetWorker(Context mContext) {
        super(mContext);
    }

    /**
     * token失效时自动登录方法
     */
    public abstract void clientLogin();

    /**
     * token失效时自动登录方法(第三方登录)
     *
     * @return 如果当前用户是第三方登录的请返回true否则将自动调用{@link #clientLogin()}
     */
    public abstract boolean thirdSave();

    /**
     * 验证程序许可密钥
     */
    public abstract void verifyAppkey();

    /**
     * 获取密钥
     * 2017.08.25 update
     */
    protected String getAppkey() {
        return SKRConfig.APPLICATION_KEY;
    }

}
