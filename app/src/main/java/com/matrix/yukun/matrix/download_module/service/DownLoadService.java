package com.matrix.yukun.matrix.download_module.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.matrix.yukun.matrix.leancloud_module.LeanCloudMessageHandler;
import com.qq.e.comm.DownloadService;

//import cn.leancloud.im.v2.AVIMMessageManager;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class DownLoadService extends Service {
    private static final int FORE_SERVICE_ID = 1;
    static DownLoadService mDownLoadService;
    String TAG=DownloadService.class.getSimpleName();

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
        context.startService(intent);
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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            this.startForeground(FORE_SERVICE_ID, new Notification());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                this.startService(new Intent(getApplication(), InnerService.class));
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(FORE_SERVICE_ID, new Notification());
        }
        initListener();
        return START_STICKY;
    }

    //初始化数值
    private void initListener() {
        DownLoadEngine.getInstance().startDownLoadServiceImpl();
//        AVIMMessageManager.setConversationEventHandler(new LeanCloudMessageHandler());
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
            if (serviceInfo.service.getPackageName().equals(context.getPackageName()) && DownloadService.class.getName().equals(serviceInfo.service.getClassName())) {
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
