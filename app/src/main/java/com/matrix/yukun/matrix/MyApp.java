package com.matrix.yukun.matrix;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lecloud.sdk.config.LeCloudPlayerConfig;
import com.lecloud.sdk.listener.OnInitCmfListener;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

/**
 * Created by yukun on 17-1-24.
 */
public class MyApp extends Application {
    public  static MyApp myApp;
//   private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
        //讯飞人脸识别
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID+"=58833c92");
        Beta.autoCheckUpgrade = false;//设置不自动检查
//        Beta.initDelay = 9 * 1000; //自动监测时间
        Bugly.init(getApplicationContext(), "884e2d9286", false);
        //  内存泄漏监测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        refWatcher = LeakCanary.install(this);
        String processName = getProcessName(this, android.os.Process.myPid());
        //设置域名 LeCloudPlayerConfig.HOST_DEFAULT 代表国内版
        //LeCloudPlayerConfig.HOST_US代表国际版
                //LeCloudPlayerConfig.HOST_GUOGUANG代表国广版
        //默认是国内版LeCloudPlayerConfig.HOST_DEFAULT，国内用户使用默认就可以
        int host = LeCloudPlayerConfig.HOST_DEFAULT;
        /**
         这个
         if
         判断必须有，在自己的进程初始化
         cde
         */
        if (getApplicationInfo().packageName.equals(processName)) {
            try {
                /**
                 LeCloudPlayerConfig.LOG_LOGCAT
                 打印日志到控制台
                 LeCloudPlayerConfig.LOG_FILE
                 日志打印到文件
                 LeCloudPlayerConfig.LOG_NONE
                 不打印日志
                 LeCloudPlayerConfig.LOG_ALL
                 日志打印到控制台和文件
                 */
//                LeCloudPlayerConfig.setLogOutputType(LeCloudPlayerConfig.LOG_LOGCAT);
                LeCloudPlayerConfig.setHostType(host);
                LeCloudPlayerConfig.init(getApplicationContext());
                LeCloudPlayerConfig.setmInitCmfListener(new OnInitCmfListener() {
                    @Override
                    public void onCdeStartSuccess() {
                        /**cde启动成功,可以开始播放如果使用remote版本这个方法会回调的晚一些，因为有个下载过程
                         * 只有回调了该方法，才可以正常播放视频建议用户通过
                         cde初始化的回调进行控制，点击开始播放是否创建播放器
                         */
                    }
                    @Override
                    public void onCdeStartFail() {
                        /**cde启动失败,不能正常播放;如果使用remote版本则可能是remote下载失败;如果使用普通版本,
                         则可能是so文件加载失败导致*/
                    }
                    @Override
                    public void onCmfCoreInitSuccess() {
                        //不包含 cde 的播放框架需要处理
                    }
                    @Override
                    public void onCmfCoreInitFail() {
                        //不包含 cde 的播放框架需要处理
                    }
                    @Override
                    public void onCmfDisconnected() {
                        //cde服务断开, 会导致播放失败, 需要重新执行初始化
                        try {
                            LeCloudPlayerConfig.init(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
