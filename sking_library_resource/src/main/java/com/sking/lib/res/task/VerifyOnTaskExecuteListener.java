package com.sking.lib.res.task;

import android.content.Context;

import com.sking.lib.res.bases.SKRNetTask;
import com.sking.lib.res.bases.SKRNetTaskExecuteListener;
import com.sking.lib.res.bases.SKRNetWorker;
import com.sking.lib.res.results.SKRBaseResult;
import com.sking.lib.res.utils.SKRUtil;

/**
 * 验证类，验证程序运行密钥
 * Created by Im_jingwei on 2017/8/25.
 */

public class VerifyOnTaskExecuteListener extends SKRNetTaskExecuteListener {

    public VerifyOnTaskExecuteListener(Context context) {
        super(context);
    }

    @Override
    public void onPreExecute(SKRNetWorker netWorker, SKRNetTask netTask) {

    }

    @Override
    public void onPostExecute(SKRNetWorker netWorker, SKRNetTask netTask) {

    }

    @Override
    public void onServerSuccess(SKRNetWorker netWorker, SKRNetTask netTask, SKRBaseResult baseResult) {
        SKRUtil.setPerMission();
    }

    @Override
    public void onServerFailed(SKRNetWorker netWorker, SKRNetTask netTask, SKRBaseResult baseResult) {
        SKRUtil.clearPerMission();
    }

    @Override
    public void onExecuteFailed(SKRNetWorker netWorker, SKRNetTask netTask, int failedType) {
        SKRUtil.clearPerMission();
    }

    @Override
    public boolean onAutoLoginFailed(SKRNetWorker netWorker, SKRNetTask netTask, int failedType, SKRBaseResult baseResult) {
        return false;
    }
}
