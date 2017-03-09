package com.matrix.yukun.matrix;

import android.app.Application;
import android.widget.Toast;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by yukun on 17-1-24.
 */
public class MyApp extends Application {
    public  static MyApp myApp;
    @Override
    public void onCreate() {
        myApp=this;
//        Beta.autoCheckUpgrade = false;//设置不自动检查
        Bugly.init(getApplicationContext(), "884e2d9286", false);

//        CrashReport.initCrashReport(getApplicationContext(), "884e2d9286", false);
        super.onCreate();
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
}
