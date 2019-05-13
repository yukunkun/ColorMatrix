package com.matrix.yukun.matrix.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.TypedValue;

import java.util.UUID;

/**
 * 设备工具类
 * Created by Guoxin on 2018/3/27.
 */
public class DeviceUtil {

    private static String[] huaweiRongyao = {
        "hwH60",    //荣耀6
        "hwPE",     //荣耀6 plus
        "hwH30",    //3c
        "hwHol",    //3c畅玩版
        "hwG750",   //3x
        "hw7D",      //x1
        "hwChe2",      //x1
    };

    private static String[] SanXing = { "SM-N9200" };
    private static String[] OnepPlus = { "OnePlus6T" };

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            //有些山寨机会写死androidId。
            if (androidId != null && !"9774d56d682e549c".equals(androidId)) {
                androidId = UUID.nameUUIDFromBytes(androidId.getBytes("utf-8")).toString();
            }
            else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    androidId = UUID.randomUUID().toString();
                }
                else {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    String telephonyManagerDeviceId = telephonyManager.getDeviceId();
                    androidId = telephonyManagerDeviceId != null ? UUID.nameUUIDFromBytes(telephonyManagerDeviceId.getBytes("utf-8")).toString() : UUID.randomUUID().toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            androidId = UUID.randomUUID().toString();
        }

        return androidId;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     *
     * @return
     */
    public static String getTelephonyDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机制造商
     *
     * @return
     */
    public static String getPhoneManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机CPU
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String[] getPhoneCPU() {
        return Build.SUPPORTED_ABIS;
    }

    /**
     * 获取手机Android API等级（如：22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android版本（如：4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceInfo() {
        String handSetInfo = "手机型号：" + Build.DEVICE + "\n系统版本：" + Build.VERSION.RELEASE + "\nSDK版本：" + Build.VERSION.SDK_INT;
        return handSetInfo;
    }

    private static String getDeviceModel() {
        return Build.DEVICE;
    }

    public static boolean isHuaWeiRongyao() {
        for (String aHuaweiRongyao : huaweiRongyao) {
            if (aHuaweiRongyao.equals(getDeviceModel())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSanXingNot5() {
        for (String aSanXing : SanXing) {
            if (aSanXing.equals(getPhoneModel())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnePlus() {
        for (String str : OnepPlus) {
            if (getDeviceModel().contains(str) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * dp转px
     *
     * @param context
     *
     * @return
     */

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     *
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     *
     * @return
     */

    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     *
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
