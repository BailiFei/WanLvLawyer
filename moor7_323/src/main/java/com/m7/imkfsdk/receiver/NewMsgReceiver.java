package com.m7.imkfsdk.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.ChatActivity;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.utils.MoorUtils;

import java.util.List;

/**
 * 新消息接收器
 */
public class NewMsgReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(IMChatManager.NEW_MSG_ACTION)) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("1", "channel_name", NotificationManager.IMPORTANCE_HIGH);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }

            context.sendBroadcast(new Intent("com.m7.imkfsdk.msgreceiver"));
            //看应用是否在前台
            if (!isAppForground(context)) {
                Intent contentIntent = new Intent(MoorUtils.getApp(), ChatActivity.class);

                contentIntent.putExtra("PeerId", "");
                contentIntent.putExtra("type", "peedId");

                contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                MoorUtils.getApp(),
                                0,
                                contentIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                //新的通知
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MoorUtils.getApp());
                Notification notification = builder.setTicker("您有新的消息")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.kf_ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(resultPendingIntent)
                        .setContentTitle("新消息")
                        .setContentText("您有新的消息")
                        .setChannelId("1")
                        .setAutoCancel(true)
                        .build();
                if (notificationManager != null && notification != null) {
                    notificationManager.notify(1, notification);
                }
            }
        }
    }

    /**
     * 判断聊天界面是否在前台
     * (如果是需要退出聊天页就要弹出，那么需要存一个sp，初始化成功后存为true，然后每次退出设置false)
     * @param mContext
     * @return
     */
    public boolean isAppForground(Context mContext) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (mContext.getPackageName().equals(appProcess.processName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
