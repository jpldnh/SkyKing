package com.sking.lib.res.bases;

import android.content.Context;
import android.widget.Toast;

import com.sking.lib.base.SKObject;
import com.sking.lib.interfaces.SKNetTaskExecuteListener;
import com.sking.lib.net.SKNetTask;
import com.sking.lib.net.SKNetWorker;
import com.sking.lib.res.R;
import com.sking.lib.res.config.SKRConfig;
import com.sking.lib.res.results.SKRArrayBaseResult;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRUtil;

import java.util.ArrayList;


public abstract class SKRNetTaskExecuteListener extends SKObject implements SKNetTaskExecuteListener {
    public Context mContext;
    private ArrayList<SKRNetTask> failedTasks;// token失效任务队列

    public SKRNetTaskExecuteListener(Context context) {
        mContext = context;
    }

    @Override
    public void onPreExecute(SKNetWorker netWorker, SKNetTask task) {
        onPreExecute((SKRNetWorker) netWorker, (SKRNetTask) task);
    }

    @Override
    public void onPostExecute(SKNetWorker netWorker, SKNetTask task) {
        onPostExecute((SKRNetWorker) netWorker, (SKRNetTask) task);
    }

    @Override
    public void onExecuteFailed(SKNetWorker netWorker, SKNetTask netTask,
                                int failedType) {
        if (SKRConfig.TOAST_NET_ENABLE)
            switch (failedType) {
                case SKNetWorker.FAILED_DATAPARSE:
                    Toast.makeText(mContext, R.string.skr_string_msg_data, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SKNetWorker.FAILED_HTTP:
                    Toast.makeText(mContext, R.string.skr_string_msg_http, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case SKNetWorker.FAILED_NONETWORK:
                    Toast.makeText(mContext, R.string.skr_string_msg_nonet, Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }

        int taskId = netTask.getId();
        if (taskId == SKRConfig.ID_LOGIN || taskId == SKRConfig.ID_THIRDSAVE) {// 登录任务
            if (failedTasks != null && failedTasks.size() > 0) {// token失效的自动登录，所有任务执行失败
                for (SKRNetTask failedTask : failedTasks) {
                    if (!onAutoLoginFailed((SKRNetWorker) netWorker,
                            failedTask, failedType, null))
                        onExecuteFailed((SKRNetWorker) netWorker,
                                (SKRNetTask) netTask, failedType);
                }
                failedTasks.clear();
            } else {
                onExecuteFailed((SKRNetWorker) netWorker,
                        (SKRNetTask) netTask, failedType);
            }
        } else {
            onExecuteFailed((SKRNetWorker) netWorker, (SKRNetTask) netTask,
                    failedType);
        }

    }

    @Override
    public void onExecuteSuccess(SKNetWorker worker, SKNetTask task,
                                 Object result) {
        SKRBaseResult baseResult = (SKRBaseResult) result;
        SKRNetTask netTask = (SKRNetTask) task;
        SKRNetWorker netWorker = (SKRNetWorker) worker;

        if (baseResult.isSuccess()) {// 服务器处理成功
            int taskId = netTask.getId();
            if (taskId == SKRConfig.ID_LOGIN
                    || taskId == SKRConfig.ID_THIRDSAVE) {// 如果为登录接口，保存用户信息
                @SuppressWarnings("unchecked")
                SKRArrayBaseResult<SKRUser> uResult = (SKRArrayBaseResult<SKRUser>) baseResult;
                SKRUser user = uResult.getObjects().get(0);
                String token = user.getToken();
                if (failedTasks != null && failedTasks.size() > 0) {// token失效时的登录，只再次执行失败任务，不做其他操作
                    for (SKRNetTask failedTask : failedTasks) {
                        failedTask.getParams().put("token", token);
                        netWorker.executeTask(failedTask);
                    }
                    failedTasks.clear();
                    checkUpdate(user);
                    return;
                }
            }
            onServerSuccess(netWorker, netTask, baseResult);
        } else {// 服务器处理失败
            if (baseResult.getError_code() == 200) {// token失效自动登录，并重新执行该任务
                if (failedTasks == null)
                    failedTasks = new ArrayList<SKRNetTask>();
                failedTasks.add(netTask);
                if (failedTasks.size() <= 1) {// 确保token失效登录只执行一次
                    if (!netWorker.thirdSave())// 如果不是第三方登录则调用框架自身登录方法
                        netWorker.clientLogin();
                }
            } else {
                int taskId = netTask.getId();
                if (taskId == SKRConfig.ID_LOGIN
                        || taskId == SKRConfig.ID_THIRDSAVE) {// 登录任务
                    if (failedTasks != null && failedTasks.size() > 0) {// token失效的自动登录，所有任务执行失败
                        for (SKRNetTask failedTask : failedTasks) {
                            if (!onAutoLoginFailed((SKRNetWorker) netWorker,
                                    failedTask, 0, baseResult))
                                onServerFailed(netWorker, netTask, baseResult);
                        }
                        failedTasks.clear();
                    } else {
                        onServerFailed(netWorker, netTask, baseResult);
                    }
                } else {
                    onServerFailed(netWorker, netTask, baseResult);
                }
            }

        }
    }

    private void checkUpdate(SKRUser user) {
        String sysVersion = user.getAndroid_last_version();
        String version = SKRUtil.getAppVersionForSever(mContext);
        if (SKRUtil.isNeedUpDate(version, sysVersion)) {
            new SKRUpGrade(user).alert(version, sysVersion);
        }
    }

    /**
     * Runs on the UI thread before the task run.
     *
     * @param netWorker
     * @param netTask
     */
    public abstract void onPreExecute(SKRNetWorker netWorker,
                                      SKRNetTask netTask);

    /**
     * Runs on the UI thread after the task run.
     *
     * @param netWorker
     * @param netTask
     */
    public abstract void onPostExecute(SKRNetWorker netWorker,
                                       SKRNetTask netTask);

    /**
     * 服务器处理成功
     *
     * @param netWorker
     * @param netTask
     * @param baseResult
     */
    public abstract void onServerSuccess(SKRNetWorker netWorker,
                                         SKRNetTask netTask, SKRBaseResult baseResult);

    /**
     * 服务器处理失败
     *
     * @param netWorker
     * @param netTask
     * @param baseResult
     */
    public abstract void onServerFailed(SKRNetWorker netWorker,
                                        SKRNetTask netTask, SKRBaseResult baseResult);

    /**
     * Runs on the UI thread when the task run failed.
     *
     * @param netWorker
     * @param netTask
     * @param failedType the type of cause the task failed.
     *                   <p>
     *                   See {@link SKNetWorker#FAILED_DATAPARSE
     *                   XtomNetWorker.FAILED_DATAPARSE},
     *                   {@link SKNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
     *                   {@link SKNetWorker#FAILED_NONETWORK
     *                   XtomNetWorker.FAILED_NONETWORK}
     *                   </p>
     */
    public abstract void onExecuteFailed(SKRNetWorker netWorker,
                                         SKRNetTask netTask, int failedType);

    /**
     * 自动登录失败
     *
     * @param netWorker
     * @param netTask
     * @param failedType 如果failedType为0表示服务器处理失败,其余参照
     *                   {@link SKNetWorker#FAILED_DATAPARSE
     *                   XtomNetWorker.FAILED_DATAPARSE},
     *                   {@link SKNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
     *                   {@link SKNetWorker#FAILED_NONETWORK
     *                   XtomNetWorker.FAILED_NONETWORK}
     * @param baseResult 执行结果(仅当failedType为0时有值,其余为null)
     * @return true表示拦截该任务执行流程, 不会继续调用onExecuteFailed或者onServerFailed方法; false反之
     */
    public abstract boolean onAutoLoginFailed(SKRNetWorker netWorker,
                                              SKRNetTask netTask, int failedType, SKRBaseResult baseResult);
}
