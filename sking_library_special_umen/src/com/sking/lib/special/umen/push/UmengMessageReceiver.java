package com.sking.lib.special.umen.push;

import android.app.Notification;
import android.content.Context;

import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


/**
 * Created by Im_jingwei on 2017/9/18.
 */

public abstract class UmengMessageReceiver extends UmengMessageHandler {

    /**
     * 自定义消息的回调方法
     * */
    @Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
        UTrack.getInstance(context).trackMsgClick(msg);
    }

    /**
     * 自定义通知栏样式的回调方法
     * */
    @Override
    public Notification getNotification(Context context, UMessage msg) {
        //默认为0，若填写的builder_id并不存在，也使用默认。
        return super.getNotification(context, msg);
    }

    /**
     * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
     * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     * */
    public UmengNotificationClickHandler notificationClick= new UmengNotificationClickHandler() {
        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
            notificationONclickLisntener(context,msg);
        }
    };

    /**
     * 通知栏点击事件
     * */
    protected abstract void notificationONclickLisntener(Context context, UMessage msg);

}
