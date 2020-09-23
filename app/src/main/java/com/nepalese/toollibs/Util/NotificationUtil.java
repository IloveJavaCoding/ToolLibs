package com.nepalese.toollibs.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.nepalese.toollibs.R;

/**
 * @author nepalese on 2020/9/23 15:52
 * @usage
 */
public class NotificationUtil {
    public static void sendNotification(Context context, String channelId, Intent intent, String title, String msg, int id){
        //1. get notification service
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification channel version>26   android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            //channel.setSound();
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(1);

            manager.createNotificationChannel(channel);
        }

        //3. set intent(active when click the notification)
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setContentTitle(title) //title
                .setContentText(msg) //message
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//intent
                .setAutoCancel(true)//cancel the notification when click head
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)  //hang the notification
                //set a picture
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapUtil.getBitmapFromRes(context,R.drawable.img_bg))
                        .bigLargeIcon(null))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.img_bg))

                .build();

        //.setCategory(Notification.CATEGORY_MESSAGE) //version>21
        //.setVisibility(Notification.VISIBILITY_PUBLIC)// can be show in the Locker page >21

        /**
         * 1.VISIBILITY_PRIVATE : 显示基本信息，如通知的图标，但隐藏通知的全部内容；
         * 2.VISIBILITY_PUBLIC : 显示通知的全部内容；
         * 3.VISIBILITY_SECRET : 不显示任何内容，包括图标。
         */

        //5. send notification
        manager.notify(id,notification);
    }

    public static void sendPictureNotification(Context context, String channelId, Intent intent, String title, String msg, int id, int resId){
        //1. get notification service
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification channel version>26   android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        //3. set intent(active when click the notification)
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setContentTitle(title) //title
                .setContentText(msg) //message
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//intent
                .setAutoCancel(true)//cancel the notification when click head
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)  //hang the notification
                //set a picture
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapUtil.getBitmapFromRes(context,resId))
                        .bigLargeIcon(null))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), resId))
                .build();

        //5. send notification
        manager.notify(id,notification);
    }

    public static void sendSelfViewNotification(Context context, String channelId, Intent intent, int id, RemoteViews small, RemoteViews big){
        //1. get notification service
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification channel version>26   android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        //3. set intent(active when click the notification)
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//intent
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomContentView(small)
                .setCustomBigContentView(big)
                .build();

        //5. send notification
        manager.notify(id,notification);
    }

    public static void showMusicNotification(Context context, String channelId, Intent intent, int id, RemoteViews small, RemoteViews big) {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setCustomContentView(small)
                .setCustomBigContentView(big)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        // Send the notification.
        //startForeground(id, notification);
    }

}
