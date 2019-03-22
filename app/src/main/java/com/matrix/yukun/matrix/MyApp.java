package com.matrix.yukun.matrix;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.matrix.yukun.matrix.download_module.service.DownLoadService;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import java.util.List;

/**
 * Created by yukun on 17-1-24.
 */
public class MyApp extends MyApplication {
    public  static MyApp myApp;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
        //讯飞人脸识别
//        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID+"=58833c92");
//        Setting.setShowLog(true);
        Beta.autoCheckUpgrade = false;//设置不自动检查
        Bugly.init(getApplicationContext(), "884e2d9286", false);
        //  内存泄漏监测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
        String processName = getProcessName(this, android.os.Process.myPid());
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        //mob
//        MobSDK.init(this);
        //服务
        DownLoadService.start(this);
//        DownLoadService.checkServiceIsHealthy(this);
    }
    //获取当前进程名字
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager)
                cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    public static Application getInstance(){
        if(myApp!=null){
            myApp=new MyApp();
        }
        return myApp;
    }
    //toast
    public static void showToast(String msg){
        Toast.makeText(myApp, msg, Toast.LENGTH_SHORT).show();
    }
    //内存泄漏管理
//    public static RefWatcher getRefWatcher(Context context) {
//        MyApp application = (MyApp) context.getApplicationContext();
//        return application.refWatcher;
//    }
}
