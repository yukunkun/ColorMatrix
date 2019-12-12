package com.matrix.yukun.matrix;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.text.TextUtils;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.matrix.yukun.matrix.download_module.service.DownLoadService;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import org.litepal.LitePalApplication;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.leancloud.AVLogger;
import cn.leancloud.core.AVOSCloud;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;

/**
 * Created by yukun on 17-1-24.
 */
public class MyApp extends LitePalApplication{
    public  static MyApp myApp;
    public static RefWatcher refWatcher;
    public static UserInfoBMob userInfo;
    public static boolean isNight;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp=this;
        Beta.autoCheckUpgrade = false;//设置不自动检查
        Bugly.init(getApplicationContext(), "884e2d9286", false);
        Bmob.initialize(this, AppConstant.BMOBAPPID);
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(/*this, */AppConstant.LEANCLOUDID, AppConstant.LEANCLOUDKEY);

        String processName = getProcessName(this, android.os.Process.myPid());
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        HeConfig.init(AppConstant.HE_WEATHER_ID, AppConstant.HE_WEATHER_SDK);
        HeConfig.switchToFreeServerNode();
        updateNight();
        //服务
        DownLoadService.start(this);

        //  内存泄漏监测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // 判断是否和 LeakCanary 初始化同一进程
//            return;
//        }
//        refWatcher = LeakCanary.install(this);//获取一个 Watcher
//        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .cookieJar(cookieJar)
//                //其他配置
//                .build();
//        OkHttpUtils.initClient(okHttpClient);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public static UserInfoBMob getUserInfo() {
        return userInfo;
    }


    public static LatLng getLatLng() {
        String latitude = SPUtils.getInstance().getString("latitude");
        String longitude = SPUtils.getInstance().getString("longitude");
        if (!TextUtils.isEmpty(latitude)){
            LatLng latLng=new LatLng(Double.valueOf(latitude),Double.valueOf(longitude));
            return latLng;
        }else {
            return null;
        }
    }

    public static void setUserInfo(UserInfoBMob user) {
        userInfo=new UserInfoBMob();
        userInfo=user;
        Gson gson=new Gson();
        SPUtils.getInstance().saveString("user",gson.toJson(userInfo));
    }

    public static boolean getNight() {
        return isNight;
    }

    public static void updateNight() {
       isNight=SPUtils.getInstance().getBoolean("isNight");
    }

    public static Application getInstance(){
        if(myApp!=null){
            myApp=new MyApp();
        }
        return myApp;
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
