package com.matrix.yukun.matrix;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

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
