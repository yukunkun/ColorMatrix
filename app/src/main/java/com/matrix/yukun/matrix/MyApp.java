package com.matrix.yukun.matrix;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by yukun on 17-1-24.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        CrashReport.initCrashReport(getApplicationContext(), "884e2d9286", false);
        super.onCreate();
    }
}
