package com.matrix.yukun.matrix.tool_module.qrcode;

import android.app.Activity;

import java.io.Serializable;

/**
 * 扫描结束监听
 *
 * @autor wwl
 * @date 2017-01-11 10:57
 */

public interface ScanFinishListener extends Serializable {
    /**
     * 扫描结束后的回调
     *
     * @param resultStr 扫描出的字符串
     * @param activity 扫码的activity
     */
    void onScanFinish(String resultStr, Activity activity);
}
