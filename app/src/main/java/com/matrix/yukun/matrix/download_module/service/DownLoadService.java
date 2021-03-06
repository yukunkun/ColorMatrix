package com.matrix.yukun.matrix.download_module.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class DownLoadService extends Service {
    private static final int FORE_SERVICE_ID = 1;
    static DownLoadService mDownLoadService;
    String TAG=DownLoadService.class.getSimpleName();
    String CHANNEL_ONE_ID = "com.primedu.cn";
    String CHANNEL_ONE_NAME = "matrix";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
        mDownLoadService=this;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        relaseData();
        super.onDestroy();
    }

    public static void start(Context context){
        Intent intent = new Intent(context, DownLoadService.class);
//        ContextCompat.startForegroundService(context, intent);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            //解决android8.0以上无法启动服务的问题
            context.startForegroundService(intent);
        }else {
            context.startService(intent);
        }
    }

    public static void stop(Context context){
        Intent intent = new Intent(context, DownLoadService.class);
        context.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand");
        //适配8.0service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID).build();
            this.startForeground(1, notification);
        }else {
            this.startForeground(FORE_SERVICE_ID, new Notification());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                this.startService(new Intent(getApplication(), InnerService.class));
            }
        }
        initListener();
        return START_STICKY;
    }

    //初始化数值
    private void initListener() {
        DownLoadEngine.getInstance().startDownLoadServiceImpl();
    }

    //释放数值
    private void relaseData() {

    }

    /**
     * 检查服务是否运行，并启动服务
     *
     * @param context
     */
    public static void checkServiceIsHealthy(Context context) {
        if (!isServiceRunning(context)) {
            start(context);
        }
    }

    /**
     * 判断服务是否正在运行
     *
     * @param context
     *
     * @return
     */
    private static boolean isServiceRunning(Context context) {
        boolean ret = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceInfo.service.getPackageName().equals(context.getPackageName()) && DownLoadService.class.getName().equals(serviceInfo.service.getClassName())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /**
     * 内部service，作用：保活CoreService
     *
     * @date 2016-12-13
     */
    public static class InnerService extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            this.startForeground(FORE_SERVICE_ID, new Notification());
            this.stopForeground(true);
            this.stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
