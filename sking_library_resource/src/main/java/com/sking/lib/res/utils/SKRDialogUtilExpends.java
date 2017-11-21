package com.sking.lib.res.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sking.lib.res.R;
import com.sking.lib.res.views.SKRSuccessTickView;
import com.sking.lib.utils.SKBaseUtil;

/**
 * 自定义各种dialog
 * Created by 谁说青春不能错 on 2016/4/26.
 */
public class SKRDialogUtilExpends {

    private Context mContext;
    private Dialog mDialog;
    private SKRAnimalUitl animalUitl = SKRAnimalUitl.getInstance();
    private AnimationDrawable mAnimation;
    private TimeThread timeThread;

    public SKRDialogUtilExpends(Context context) {
        this.mContext = context;
    };

    /**
     * 成功提示diaolog
     **/
    public void showSuccessDialog(String message, boolean isCancle) {
        mDialog = new Dialog(mContext, R.style.SKR_Style_Dialog);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.skr_layout_dialog_progress_success, null);
        FrameLayout mSuccessFrame = (FrameLayout) view.findViewById(R.id.success_frame);
        SKRSuccessTickView mSuccessTick = (SKRSuccessTickView) mSuccessFrame.findViewById(R.id.success_tick);
        View mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        View mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        TextView message_text = (TextView) view.findViewById(R.id.dialog_success_text);
        if (!SKBaseUtil.isNull(message))
            message_text.setText(message);
        animalUitl.viewCenterIn(mContext, view);
        mSuccessTick.startTickAnim();
        mSuccessLeftMask.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.skr_animal_success_mask_left));
        mSuccessRightMask.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.skr_animal_success_mask_right));
        mSuccessRightMask.clearAnimation();
        mSuccessRightMask.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.skr_animal_success_bow_roate));
        mDialog.setCancelable(isCancle);
        mDialog.setContentView(view);
        mDialog.show();
    }

    /**
     * 快跑小人progerss进度条
     **/
    public void showRuanManProgressDialog(String textMessage) {
        mDialog = new Dialog(mContext, R.style.SKR_Style_Dialog);
        startTimeThread();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.skr_layout_dialog_progress_run_man, null);
        ImageView mImageView = (ImageView) view.findViewById(R.id.dialog_run_man_image);
        TextView message_text = (TextView) view.findViewById(R.id.dialog_run_man_text);
        if (!SKBaseUtil.isNull(textMessage))
            message_text.setText(textMessage);
        animalUitl.viewCenterIn(mContext, view);
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
        mDialog.show();
    }

    /**
     * dialog是否正在显示
     */
    public boolean isShow() {
        return mDialog == null ? false : mDialog.isShowing();
    }

    /**
     * 关闭进度条
     */
    public void cancleDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (timeThread != null) {
            timeThread.cancel();
            timeThread = null;
        }
    }

    /**
     * 开启时间进程
     */
    private void startTimeThread() {
        timeThread = new TimeThread(new TimeHandler(mDialog));
        timeThread.start();
    }

    /**
     * 超时判断
     */
    private class TimeThread extends Thread {
        private int curr;
        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            curr = 60;
            while (curr > 0) {
                timeHandler.sendEmptyMessage(curr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                curr--;
            }
            timeHandler.sendEmptyMessage(-1);
        }
    }

    /**
     * handler
     */
    private static class TimeHandler extends Handler {

        private Dialog progressBlueWheel;

        public TimeHandler(Dialog progressBlueWheel) {
            this.progressBlueWheel = progressBlueWheel;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (progressBlueWheel != null) {
                        progressBlueWheel.cancel();
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
