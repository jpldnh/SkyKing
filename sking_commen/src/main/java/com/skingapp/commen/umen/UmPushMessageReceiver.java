package com.skingapp.commen.umen;

import android.app.Notification;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.skingapp.commen.R;
import com.sking.lib.special.umen.push.UmengMessageReceiver;
import com.umeng.message.entity.UMessage;


/**
 * Created by Im_jingwei on 2017/9/18.
 */

public class UmPushMessageReceiver extends UmengMessageReceiver {

    /**
     * 自定义消息的回调方法
     * */
    @Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
//        SKToastUtil.showLongToast(context,msg.custom);
    }

    /**
     * 自定义通知栏样式的回调方法
     * */
//    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Notification getNotification(Context context, UMessage msg) {
        switch (msg.builder_id) {
            case 0:
                Notification.Builder builder = new Notification.Builder(context);
                RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.view_umpush_notification_custom);
                myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                builder.setContent(myNotificationView)
                        .setSmallIcon(getSmallIconId(context, msg))
                        .setTicker(msg.ticker)
                        .setAutoCancel(true);

                return builder.getNotification();
            default:
                //默认为0，若填写的builder_id并不存在，也使用默认。
                return super.getNotification(context, msg);
        }
    }

    @Override
    protected void notificationONclickLisntener(Context context, UMessage msg) {
        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
    }

}
