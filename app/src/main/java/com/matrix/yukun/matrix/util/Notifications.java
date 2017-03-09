package com.matrix.yukun.matrix.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.weather_module.WeatherActivity;

/**
 * Created by yukun on 17-3-8.
 */
public class Notifications {
    public static void start(Context context,String wendu) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //此Builder为android.support.v4.app.NotificationCompat.Builder中的，下同。
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        //系统收到通知时，通知栏上面显示的文字。 mBuilder.setTicker("通知栏上面显示的文字");
        // 显示在通知栏上的小图标
        mBuilder.setSmallIcon(R.mipmap.tool_icon);
        //通知标题
        mBuilder.setContentTitle("Matrix Photo");
        //通知内容
        mBuilder.setContentText(wendu);
        //设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
//        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tool_icon));
        //显示在小图标左侧的数字
//        mBuilder.setNumber(6);
        mBuilder.setCategory(wendu);
        //设置为不可清除模式
        mBuilder.setOngoing(false);
        //显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
        Intent notificationIntent =new Intent(context, WeatherActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
        //添加震动
        Notification notification=mBuilder.build();
//        // notification.defaults=Notifications.DEFAULT_VIBRATE;
//        notification.flags |= Notifications.FLAG_AUTO_CANCEL;
        //将notification传递给manner
        nm.notify(1, notification);
    }
}
