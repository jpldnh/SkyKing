package com.skingapp.commen.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sking.lib.imageload.SKImageTask;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRUtil;
import com.sking.lib.utils.SKScreenUtil;
import com.sking.lib.utils.SKSharedPreferencesUtil;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseActivity;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.bases.BaseNetWorker;
import com.skingapp.commen.models.SysInitInfo;
import com.skingapp.commen.models.User;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * 启动页
 */
public class StartActivity extends BaseActivity {
    private ImageView imageView;
    private SysInitInfo sysInitInfo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SKScreenUtil.setFullCreen(this,true);//设置全屏
        setSwipEnable(false);//设置本页面关闭滑动返回
        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);
        sysInitInfo = getApplicationContext().getSysInitInfo();
        user = getApplicationContext().getUser();
        init();
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        imageView = (ImageView) findViewById(R.id.imageview);
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStatusBarAndNevagitBar() {
        //重写为了不使用默认效果
//        SKScreenUtil.setStatusBarTransparent(this,true);
//        SKScreenUtil.setNevigationBarTransparent(this);
    }

    private void init() {
        setStartImage();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.skr_animal_logo);
        animation.setAnimationListener(new StartAnimationListener());
        imageView.startAnimation(animation);
    }

    private void setStartImage() {
        String startImage = null;
        if (sysInitInfo != null)
            startImage = sysInitInfo.getStart_img();
        if (!isNull(startImage)) {
            URL url;
            try {
                url = new URL(startImage);
                imageWorker.loadImage(new ImageTask(imageView, url, mContext));
            } catch (MalformedURLException e) {
                imageView.setImageResource(R.mipmap.start);
            }
        }
    }

    @Override
    protected void networkRequestBefore(SKRNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void networkRequestAfter(SKRNetTask netTask) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void networkRequestSuccess(SKRNetTask netTask, SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case INIT:
                @SuppressWarnings("unchecked")
                SKRArrayBaseResult<SysInitInfo> sResult = (SKRArrayBaseResult<SysInitInfo>) baseResult;
                sysInitInfo = sResult.getObjects().get(0);
                setStartImage();
                getApplicationContext().setSysInitInfo(sysInitInfo);

			/* 验证服务器密钥 2017.08.27 update*/
//			getApplicationContext().verifyApplicationKey(mContext);
        	/* 验证服务器密钥end */

                checkLogin();
                break;
            case CLIENT_LOGIN:
                toMain();
            case THIRD_SAVE:
                break;
            default:
                break;
        }

    }

    private void checkLogin() {
        if (isAutoLogin()) {
            String username = SKSharedPreferencesUtil.get(this, "username");
            String password = SKSharedPreferencesUtil.get(this, "password");
            if (!isNull(username) && !isNull(password)) {// 如果是本项目用户名密码登录
                BaseNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(username, password);
            } else if (SKRUtil.isThirdSave(mContext)) {// 如果是第三方登录
                BaseNetWorker netWorker = getNetWorker();
                netWorker.thirdSave();
            } else {
                toLogin();
            }
        } else {
            toLogin();
        }

    }

    // 检查是否自动登录
    private boolean isAutoLogin() {
        String autoLogin = SKSharedPreferencesUtil.get(mContext, "autoLogin");
        boolean no = "no".equals(autoLogin);
        return !no;
    }

    @Override
    protected void networkRequestParseFailed(SKRNetTask netTask,
                                             SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case INIT:
                getInitFailed();
                break;
            case CLIENT_LOGIN:
                toMain();
            case THIRD_SAVE:
                toMain();
                break;
            default:
                break;
        }
    }

    private void getInitFailed() {
        if (sysInitInfo != null) {
            checkLogin();
        } else {
            showToastLong("获取系统初始化信息失败啦\n请检查网络连接重试");
        }
    }

    @Override
    protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case INIT:
                getInitFailed();
                break;
            case CLIENT_LOGIN:
                toMain();
            case THIRD_SAVE:
                // toLogin();
                toMain();
                break;
            default:
                break;
        }
    }

    private void toMain() {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }


    private void toLogin() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
    }

    private class StartAnimationListener implements AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            BaseNetWorker netWorker = getNetWorker();
            netWorker.init();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }
    }

    private class ImageTask extends SKImageTask {

        public ImageTask(ImageView imageView, URL url, Object context) {
            super(imageView, url, context);
        }

        @Override
        public void beforeload() {
            imageView.setImageResource(R.mipmap.start);
        }

        @Override
        public void failed() {
            log_w("Get image " + path + " failed!!!");
            imageView.setImageResource(R.mipmap.start);
        }
    }

}
