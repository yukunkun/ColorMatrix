package com.matrix.yukun.matrix.net_module.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2019/4/23.
 */

public class TrafficBean {
    /**
     * 当前对象实例
     */
    private static TrafficBean instance;
    /**
     * 当前应用的uid
     */
    static int UUID;
    /**
     * 上一次记录网络字节流
     */
    private long preRxBytes = 0;
    /**
     * 上一次记录网络字节流
     */
    private long pretxBytes = 0;
    /**
     *
     */
    private Timer mTimer = null;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 消息处理器
     */
    private Handler handler;
    /**
     * 更新频率
     */
    private final int UPDATE_FREQUENCY = 1;
    private int times = 1;


    /**
     * 构造方法
     *
     * @param context
     * @param handler
     * @param uid
     */
    public TrafficBean(Context context, Handler handler, int uid) {
        this.context = context;
        this.handler = handler;
        this.UUID = uid;
    }

    public TrafficBean(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    /**
     * 获取实例对象
     *
     * @param context
     * @param handler
     * @return
     */
    public static TrafficBean getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new TrafficBean(context, handler);
        }
        return instance;
    }
    /**
     * 获取当前下载流量总和
     *
     * @return
     */
    public static long getNetworkRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    /**
     * 获取当前上传流量总和
     *
     * @return
     */
    public static long getNetworkTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    /**
     * 获取当前网速
     *
     * @return
     */
    public double getNetSpeed() {
        long curRxBytes = getNetworkRxBytes();
        if (preRxBytes == 0)
            preRxBytes = curRxBytes;
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        //int kb = (int) Math.floor(bytes / 1024 + 0.5);
        double kb = (double) bytes / (double) 1024;
        BigDecimal bd = new BigDecimal(kb);
        return kb;//bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
     /* 获取当前网速
     *
     * @return
      */
    public double getNetUploadSpeed() {
        long curTxBytes = getNetworkTxBytes();
        if (pretxBytes == 0)
            pretxBytes = curTxBytes;
        long bytes = curTxBytes - pretxBytes;
        pretxBytes = curTxBytes;
        //int kb = (int) Math.floor(bytes / 1024 + 0.5);
        double kb = (double) bytes / (double) 1024;
        BigDecimal bd = new BigDecimal(kb);
        return kb;//bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 开启流量监控
     */
    public void startCalculateNetSpeed() {
        preRxBytes = getNetworkRxBytes();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (times == UPDATE_FREQUENCY) {
                        Message msg = new Message();
                        msg.what = 1;
                        //msg.arg1 = getNetSpeed();
                        msg.obj = getNetSpeed();
                        handler.sendMessage(msg);
                        Message msg2 = new Message();
                        msg2.what = 2;
                        //msg.arg1 = getNetSpeed();
                        msg2.obj = getNetUploadSpeed();
                        handler.sendMessage(msg2);
                        times = 1;
                    } else {
                        times++;
                    }
                }
            }, 1000, 1000);
        }
    }
    /**
     * 停止网速监听计算
     */
    public void stopCalculateNetSpeed() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    /**
     * 停止网速监听计算
     */
    public double getTotalNet() {
        return TrafficStats.getMobileTxBytes()+TrafficStats.getMobileRxBytes();
    }

    /**
     * 获取当前应用uid
     *
     * @return
     */
    public int getUid() {
        try {
            PackageManager pm = context.getPackageManager();
            //修改
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return ai.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
