package com.skingapp.commen.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sking.lib.base.SKActivityManager;
import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.interfaces.SKRInterfaces;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRDialogUtil;
import com.sking.lib.res.utils.SKRUtil;
import com.sking.lib.utils.SKSharedPreferencesUtil;
import com.skingapp.commen.R;
import com.skingapp.commen.bases.BaseActivity;
import com.skingapp.commen.bases.BaseApplication;
import com.skingapp.commen.bases.BaseHttpInformation;
import com.skingapp.commen.bases.BaseUpGrade;
import com.skingapp.commen.db.UserDBHelper;
import com.skingapp.commen.models.SysInitInfo;
import com.skingapp.commen.models.User;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {
    private TextView titleText;
    private TextView titleLeft;
    private TextView titleRight;

    private TextView setPassView;
    private TextView aboutUsView;
    private TextView freedBackView;
    private TextView checkUpdateView;
    private Button exitButt;

    private SysInitInfo sysInitInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.top_title_text);
        titleLeft = (TextView) findViewById(R.id.top_title_left);
        titleRight = (TextView) findViewById(R.id.top_title_right);

        setPassView = (TextView) findViewById(R.id.ac_setting_pass);
        aboutUsView = (TextView) findViewById(R.id.ac_setting_about);
        freedBackView = (TextView) findViewById(R.id.ac_setting_advice);
        checkUpdateView = (TextView) findViewById(R.id.ac_setting_update);
        exitButt = (Button) findViewById(R.id.ac_setting_exit);


    }

    @Override
    protected void setListener() {
        titleText.setText("设置");
        titleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight.setVisibility(View.GONE);
        setPassView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PassWordActivity.class);
                startActivity(it);
            }
        });
        aboutUsView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, WebViewActivity.class);
                it.putExtra("TITLE_NAME","关于");
                it.putExtra("URL_PATH","webview/parm/aboutus");
                startActivity(it);
            }
        });
        freedBackView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, AdviceActivity.class);
                startActivity(it);
            }
        });
        checkUpdateView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetWorker().init();
            }
        });
        exitButt.setOnClickListener(new ExitListener());
    }

    @Override
    protected void networkRequestBefore(SKRNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                showProgressDialog("正在检查更新");
                break;
            case CLIENT_LOGINOUT:
                showProgressDialog("正在注销");
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestAfter(SKRNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                cancelProgressDialog();
                break;
            case CLIENT_LOGINOUT:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestSuccess(SKRNetTask netTask,
                                         SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                @SuppressWarnings("unchecked")
                SKRArrayBaseResult<SysInitInfo> sResult = (SKRArrayBaseResult<SysInitInfo>) baseResult;
                sysInitInfo = sResult.getObjects().get(0);
                String sysVersion = sysInitInfo.getAndroid_last_version();
                String version = SKRUtil.getAppVersionForSever(mContext);
                if (SKRUtil.isNeedUpDate(version, sysVersion)) {
                    showUpdateDialog(version, sysVersion);
                } else {
                    String str = "当前客户端版本是" + version + ",服务器最新版本是" + sysVersion + ",已经是最新版本了哦";
                    showToastLong(str);
                }
                break;
            case CLIENT_LOGINOUT:
                cancellationSuccess();
                break;
            default:
                break;
        }
    }

    private SKRDialogUtil updateDialog;

    public void showUpdateDialog(String curr, String server) {
        if (updateDialog == null) {
            updateDialog = new SKRDialogUtil(mContext);
            String str = "当前客户端版本是" + curr + ",服务器最新版本是" + server + ",确定要升级吗？";
            updateDialog.setText(str);
            updateDialog.setLeftButtonText("取消");
            updateDialog.setRightButtonText("升级");
            updateDialog.setButtonOnclickListener(new ButtonListener());
        }
        updateDialog.showExpand();
    }

    @Override
    protected void networkRequestParseFailed(SKRNetTask netTask,
                                             SKRBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                showToastLong(baseResult.getMsg());
                break;
            case CLIENT_LOGINOUT:
                // cancellationSuccess();
                log_i("退出登录失败");
                break;
            default:
                break;
        }
    }

    @Override
    protected void networkRequestExecuteFailed(SKRNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case INIT:
                showToastLong("检查更新失败");
                break;
            case CLIENT_LOGINOUT:
                // cancellationSuccess();
                log_i("退出登录失败");
                break;
            default:
                break;
        }
    }

    private class ButtonListener implements SKRInterfaces.SKROnPnButtonClickLitener {
        private BaseUpGrade upGrade;

        @Override
        public void onPositiveClick() {
            if (upGrade == null)
                upGrade = new BaseUpGrade(mContext);
            upGrade.upGrade(sysInitInfo);
        }

        @Override
        public void onNegetiveClick() {

        }
    }

    private class ExitListener implements OnClickListener {
        private SKRDialogUtil logoutDialog;

        @Override
        public void onClick(View v) {
            showLogoutDialog();
        }

        private void showLogoutDialog() {
            if (logoutDialog == null) {
                logoutDialog = getButtontDialog();
                logoutDialog.setText("您确定要退出登录吗？");
                logoutDialog.setLeftButtonText("取消");
                logoutDialog.setRightButtonText("退出");
                logoutDialog.setButtonOnclickListener(new ExitButtonListener());
            }
            logoutDialog.showExpand();
        }
    }

    private class ExitButtonListener implements SKRInterfaces.SKROnPnButtonClickLitener {


        @Override
        public void onPositiveClick() {
            BaseApplication application = BaseApplication.getInstance();
            User user = application.getUser();
            getNetWorker().clientLoginout(user.getToken());
        }

        @Override
        public void onNegetiveClick() {

        }
    }

    private void cancellationSuccess() {
        //友盟统计
//        UmengUtils.logOut();
//        UmengEventUtils.logoutEvent(mContext,BaseApplication.getInstance().getUser().getId());

        UserDBHelper dbHelper = new UserDBHelper(mContext);
        dbHelper.clear();
        // 清空登录信息
        BaseApplication.getInstance().setUser(null);
        SKSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
        SKSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
//		HemaChat.getInstance(mContext).release();  //释放聊天连接
        SKRUtil.setThirdSave(mContext, false);// 将第三方登录标记置为false
        SKActivityManager.finishAll();
        Intent it = new Intent(mContext, LoginActivity.class);
        startActivity(it);
    }

}
