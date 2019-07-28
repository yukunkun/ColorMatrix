package com.matrix.yukun.matrix;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.matrix.yukun.matrix.download_module.service.DownLoadService;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by yukun on 17-1-24.
 */
public class MyApp extends MyApplication {
    public  static MyApp myApp;
    public static RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
        Beta.autoCheckUpgrade = false;//设置不自动检查
        Bugly.init(getApplicationContext(), "884e2d9286", false);
        String processName = getProcessName(this, android.os.Process.myPid());
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        //服务
        DownLoadService.start(this);
        //  内存泄漏监测
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // 判断是否和 LeakCanary 初始化同一进程
            return;
        }
        refWatcher = LeakCanary.install(this);//获取一个 Watcher
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

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        MyApp leakApplication = (MyApp) context.getApplicationContext();
        return leakApplication.refWatcher;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
}
