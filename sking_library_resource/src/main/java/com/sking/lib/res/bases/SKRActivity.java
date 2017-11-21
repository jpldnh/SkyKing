package com.sking.lib.res.bases;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.sking.lib.base.SKActivity;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.res.R;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRDialogUtil;
import com.sking.lib.res.utils.SKRProgressDialogUtil;
import com.sking.lib.res.utils.SKRTextDialogUtil;
import com.sking.lib.res.views.SKRFrameLayout;
import com.sking.lib.utils.SKScreenUtil;
import com.sking.lib.utils.SKToastUtil;

import static com.sking.lib.res.config.SKRConfig.IS_OPEN_UP_SWIP;


/**
 * 本项目中对XtomActivity的一些拓展
 * <p>
 * 1.本项目中发送网络请求不建议使用{@link # getDataFromServer(SKNetTask)} 建议使用如下
 * <p>
 * <pre>
 * SKRNetWorker netWorker = {@link #getNetWorker()};
 * netWorker.login();
 * </pre>
 * <p>
 * </p>
 */
public abstract class SKRActivity extends SKActivity {
    private SKRNetWorker netWorker;
    private SKRTextDialogUtil textDialog;
    private SKRDialogUtil buttonDialog;
    private SKRProgressDialogUtil progressDialog;
    private SKRFrameLayout swipFrameLayout;
    private boolean PAGE_SWIP_ENABLED = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarAndNevagitBar();
        if (IS_OPEN_UP_SWIP&&PAGE_SWIP_ENABLED)//swip关闭
            swipFrameLayout = new SKRFrameLayout(mContext);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (swipFrameLayout != null)//swip关闭
            swipFrameLayout.replaceLayer(this);
    }

    @Override
    protected boolean onKeyBack() {
        finish();
        return true;
    }

    @Override
    protected boolean onKeyMenu() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onDestroy() {
        if (netWorker != null) {
            netWorker.cancelTasks();
            netWorker.setOnTaskExecuteListener(null);
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        cancelTextDialog();
        if (progressDialog != null)
            progressDialog.cancelImmediately();
        if (swipFrameLayout != null)//swip关闭
        {
            if (!swipFrameLayout.isSwipeFinished()) {
                swipFrameLayout.cancelPotentialAnimation();
                overridePendingTransition(0, R.anim.skr_animal_right_out);
            }
        }
        super.finish();
    }

    /**
     * 关闭Activity
     *
     * @param enterAnim 进入Activity的动画,若没有传0即可
     * @param exitAnim  退出Activity的动画,若没有传0即可
     */
    public void finish(int enterAnim, int exitAnim) {
        finish();
        overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * @param enterAnim 进入Activity的动画,若没有传0即可
     * @param exitAnim  退出Activity的动画,若没有传0即可
     */
    public void startActivityForResult(Intent intent, int requestCode,
                                       int enterAnim, int exitAnim) {
        startActivityForResult(intent, requestCode);
        if (getParent() != null)
            getParent().overridePendingTransition(enterAnim, exitAnim);
        else
            overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * @param enterAnim 进入Activity的动画,若没有传0即可
     * @param exitAnim  退出Activity的动画,若没有传0即可
     */
    public void startActivity(Intent intent, int enterAnim, int exitAnim) {
        startActivity(intent);
        if (getParent() != null)
            getParent().overridePendingTransition(enterAnim, exitAnim);
        else
            overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * @time 2017/9/29  17:15
     * @author Im_jingwei
     * @desc 设置状态栏和导航栏，若要修改重写次方法
     */
    public void setStatusBarAndNevagitBar() {
//        SKScreenUtil.setStatusBarTransparent(this,true);//透明化状态栏
        SKScreenUtil.setStatusBar(this, skGetColor(R.color.colorPrimary));//设置状态栏颜色
        SKScreenUtil.setNevigationBar(this, skGetColor(R.color.colorPrimary));
    }

    /**
     * @param
     * @time 2017/11/15  15:52
     * @author Im_jingwei
     * @desc 设置侧滑关闭
     */
    public void setSwipEnable(boolean bo) {
        PAGE_SWIP_ENABLED = bo;
    }

    /**
     * @param
     * @time 2017/11/15  15:52
     * @author Im_jingwei
     * @desc 设置任意位置侧滑关闭
     */
    public void setSwipEnableAnyWhere(boolean bo) {
        if(swipFrameLayout!=null)
            swipFrameLayout.setSwipeAnyWhere(bo);
    }

    /**
     * 显示提示弹窗
     *
     * @param text 弹窗提示语
     */
    public void showTextDialog(String text) {
        if (textDialog == null)
            textDialog = new SKRTextDialogUtil(this);
        textDialog.setText(text);
        textDialog.show();
    }

    /**
     * 显示提示弹窗
     *
     * @param text 弹窗提示语id
     */
    public void showTextDialog(int text) {
        if (textDialog == null)
            textDialog = new SKRTextDialogUtil(this);
        textDialog.setText(text);
        textDialog.show();
    }

    /**
     * 取消提示弹窗
     */
    public void cancelTextDialog() {
        if (textDialog != null)
            textDialog.cancel();
    }

    /**
     * 有确定按钮提示弹窗
     *
     * @param text 弹窗提示语id
     */
    public void showConfirmButtonDialog(int text) {
        if (buttonDialog == null)
            buttonDialog = new SKRDialogUtil(this);
        buttonDialog.setText(text);
        buttonDialog.show();
    }

    /**
     * 有确定按钮提示弹窗
     *
     * @param text 弹窗提示语id
     */
    public void showConfirmButtonDialog(String text) {
        if (buttonDialog == null)
            buttonDialog = new SKRDialogUtil(this);
        buttonDialog.setText(text);
        buttonDialog.show();
    }

    /**
     * 有确定,取消按钮提示弹窗
     *
     * @param text 弹窗提示语id
     */
    public void showButtonDialog(int text) {
        if (buttonDialog == null)
            buttonDialog = new SKRDialogUtil(this);
        buttonDialog.setText(text);
        buttonDialog.showExpand();
    }

    /**
     * 有确定,取消按钮提示弹窗
     *
     * @param text 弹窗提示语id
     */
    public void showButtonDialog(String text) {
        if (buttonDialog == null)
            buttonDialog = new SKRDialogUtil(this);
        buttonDialog.setText(text);
        buttonDialog.showExpand();
    }

    /**
     * 显示交互弹窗(默认不可以点击弹窗外侧取消)
     *
     * @param text 弹窗提示语
     */
    public void showProgressDialog(String text) {
        if (progressDialog == null)
            progressDialog = new SKRProgressDialogUtil(this);
        progressDialog.setText(text);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 显示交互弹窗
     *
     * @param text       弹窗提示语id
     * @param cancelable 是否可以点击弹窗外侧取消
     */
    public void showProgressDialog(String text, boolean cancelable) {
        if (progressDialog == null)
            progressDialog = new SKRProgressDialogUtil(this);
        progressDialog.setText(text);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    /**
     * 显示交互弹窗(默认不可以点击弹窗外侧取消)
     *
     * @param text 弹窗提示语
     */
    public void showProgressDialog(int text) {
        if (progressDialog == null)
            progressDialog = new SKRProgressDialogUtil(this);
        progressDialog.setText(text);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 显示交互弹窗
     *
     * @param text       弹窗提示语
     * @param cancelable 是否可以点击弹窗外侧取消
     */
    public void showProgressDialog(int text, boolean cancelable) {
        if (progressDialog == null)
            progressDialog = new SKRProgressDialogUtil(this);
        progressDialog.setText(text);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    /**
     * 取消交互弹窗(同时setCancelable(false))
     */
    public void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.setCancelable(false);
            progressDialog.cancel();
        }
    }

    /**
     * 取消button提示弹窗
     */
    public void cancelButtonDialog() {
        if (buttonDialog != null)
            buttonDialog.cancel();
    }

    /**
     * 获取text dialog
     */
    public SKRTextDialogUtil getTextDialog() {
        if (textDialog == null)
            textDialog = new SKRTextDialogUtil(this);
        return textDialog;
    }

    /**
     * 获取button dialog
     */
    public SKRDialogUtil getButtontDialog() {
        if (buttonDialog == null)
            buttonDialog = new SKRDialogUtil(this);
        return buttonDialog;
    }

    /**
     * 获取progress dialog
     */
    public SKRProgressDialogUtil getProgressDialog() {
        if (progressDialog == null)
            progressDialog = new SKRProgressDialogUtil(this);
        return progressDialog;
    }

    /**
     * 土司信息
     */
    public void showToastLong(int text) {
        SKToastUtil.showLongToast(getApplicationContext(), text);
    }

    /**
     * 土司信息
     */
    public void showToastLong(String text) {
        SKToastUtil.showLongToast(getApplicationContext(), text);
    }

    /**
     * 土司信息
     */
    public void showToastShort(int text) {
        SKToastUtil.showShortToast(getApplicationContext(), text);
    }

    /**
     * 土司信息
     */
    public void showToastShort(String text) {
        SKToastUtil.showShortToast(getApplicationContext(), text);
    }

    /**
     * 设置view显示
     */
    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 设置view隐藏
     */
    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    /**
     * 获取颜色
     */
    public int skGetColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return getResources().getColor(color, null);
        else
            return getResources().getColor(color);
    }

    /**
     * 获取网络请求工具类
     */
    public SKRNetWorker getNetWorker() {
        if (netWorker == null) {
            netWorker = initNetWorker();
            netWorker
                    .setOnTaskExecuteListener(new SKNetTaskExecuteListener(this));
        }
        return netWorker;
    }

    /**
     * 初始化NetWorker
     */
    protected abstract SKRNetWorker initNetWorker();

    /**
     * 返回数据前的操作，如显示进度条
     *
     * @param netTask
     */
    protected abstract void networkRequestBefore(SKRNetTask netTask);

    /**
     * 返回数据后的操作，如关闭进度条
     *
     * @param netTask
     */
    protected abstract void networkRequestAfter(SKRNetTask netTask);

    /**
     * 服务器处理成功
     *
     * @param netTask
     * @param baseResult
     */
    protected abstract void networkRequestSuccess(SKRNetTask netTask,
                                                  SKRBaseResult baseResult);

    /**
     * 服务器处理或数据解析失败
     *
     * @param netTask
     * @param baseResult
     */
    protected abstract void networkRequestParseFailed(SKRNetTask netTask,
                                                      SKRBaseResult baseResult);

    /**
     * 获取数据失败
     *
     * @param netTask
     * @param failedType 失败原因
     *                   <p>
     *                   See {@link SKRNetWorker#FAILED_DATAPARSE
     *                   SKNetWorker.FAILED_DATAPARSE},
     *                   {@link SKRNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
     *                   {@link SKRNetWorker#FAILED_NONETWORK
     *                   XtomNetWorker.FAILED_NONETWORK}
     *                   </p>
     */
    protected abstract void networkRequestExecuteFailed(SKRNetTask netTask,
                                                        int failedType);

    /**
     * 自动登录失败
     *
     * @param netWorker
     * @param netTask
     * @param failedType 如果failedType为0表示服务器处理失败,其余参照
     *                   {@link SKRNetWorker#FAILED_DATAPARSE
     *                   SKNetWorker.FAILED_DATAPARSE},
     *                   {@link SKRNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
     *                   {@link SKRNetWorker#FAILED_NONETWORK
     *                   XtomNetWorker.FAILED_NONETWORK}
     * @param baseResult 执行结果(仅当failedType为0时有值,其余为null)
     * @return true表示拦截该任务执行流程,
     * 不会继续调用callBackForServerFailed或者callBackForGetDataFailed方法;
     * false反之
     */
    public abstract boolean onAutoLoginFailed(SKRNetWorker netWorker,
                                              SKRNetTask netTask, int failedType, SKRBaseResult baseResult);

    private class SKNetTaskExecuteListener extends SKRNetTaskExecuteListener {

        public SKNetTaskExecuteListener(Context context) {
            super(context);
        }

        @Override
        public void onPreExecute(SKRNetWorker netWorker, SKRNetTask netTask) {
            networkRequestBefore(netTask);
        }

        @Override
        public void onPostExecute(SKRNetWorker netWorker, SKRNetTask netTask) {
            networkRequestAfter(netTask);
        }

        @Override
        public void onServerSuccess(SKRNetWorker netWorker,
                                    SKRNetTask netTask, SKRBaseResult baseResult) {
            networkRequestSuccess(netTask, baseResult);
        }

        @Override
        public void onServerFailed(SKRNetWorker netWorker,
                                   SKRNetTask netTask, SKRBaseResult baseResult) {
            networkRequestParseFailed(netTask, baseResult);
        }

        @Override
        public void onExecuteFailed(SKRNetWorker netWorker,
                                    SKRNetTask netTask, int failedType) {
            networkRequestExecuteFailed(netTask, failedType);
        }

        @Override
        public boolean onAutoLoginFailed(SKRNetWorker netWorker,
                                         SKRNetTask netTask, int failedType, SKRBaseResult baseResult) {
            return SKRActivity.this.onAutoLoginFailed(netWorker, netTask,
                    failedType, baseResult);
        }
    }


    @Override
    protected void networkRequestBefore(SKNetTask netTask) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void networkRequestAfter(SKNetTask netTask) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void networkRequestSuccess(SKNetTask netTask, Object result) {
        // TODO Auto-generated method stub
    }

    @Override
    @Deprecated
    public void getDataFromServer(SKNetTask netTask) {
        log_e("本项目中不建议使用此方法");
    }

}
