package com.matrix.yukun.matrix.util.handler;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.util.ActivityManager;
import com.matrix.yukun.matrix.util.DeviceUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * App崩溃处理
 *
 * @author PengZhenjin
 * @date 2016-9-9
 */
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {

    private static AppCrashHandler mInstance = new AppCrashHandler();

    public static final String FILE_NAME = "crash.log";

    private Context mContext;
    private String  logPath;

    private AppCrashHandler() {

    }

    /**
     * 单例
     *
     * @return
     */
    public static AppCrashHandler getInstance() {
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 保存错误信息到SDCard
        this.saveExceptionToSDCard(ex);

        // 上传错误信息到服务器
        this.uploadExceptionToServer();

        // 上传到友盟
//        if(!AppManager.isDebug()){
//            MobclickAgent.reportError(this.mContext, ex);
//        }
//
//        // 打印错误信息到控制台
        LogUtil.e("AppCrashHandler:" + ex.getMessage(), ex);
//        LogUtil.flushLog();

        // 提示信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，应用程序出错啦，即将关闭！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        // 让程序继续运行3秒再退出，保证错误日志的保存
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 退出应用程序
//        ActivityManager.getInstance().AppExit();
    }

    /**
     * 保存错误信息到SDCard
     *
     * @param ex
     */
    private void saveExceptionToSDCard(Throwable ex) {
        try {
            logPath = AppConstant.LOG;
            File logFile = new File(logPath);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
            // 打印发生异常的时间
            pw.println();
            pw.println("错误发生时间：" + new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()));
            pw.println();

            // 打印手机信息
//            pw.println("App版本号：" + AppUtil.getVersionCode(this.mContext));
            pw.println();
            pw.println("Android系统版本号：" + DeviceUtil.getBuildVersion());
            pw.println("Android手机制造商：" + DeviceUtil.getPhoneManufacturer());
            pw.println("Android手机品牌：" + DeviceUtil.getPhoneBrand());
            pw.println("Android手机型号：" + DeviceUtil.getPhoneModel());
            StringBuilder sb = new StringBuilder();
            sb.append("Android手机CPU：");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (String abi : DeviceUtil.getPhoneCPU()) {
                    sb.append(abi);
                    sb.append("，");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            pw.println(sb.toString());
            pw.println();

            // 打印错误信息
            ex.printStackTrace(pw);
            pw.println();

            pw.close();
        } catch (IOException e) {
            LogUtil.e(AppCrashHandler.class.toString(), e);
        }
    }

    /**
     * 上传错误信息到服务器
     */
    private void uploadExceptionToServer() {
        // TODO
    }
}
