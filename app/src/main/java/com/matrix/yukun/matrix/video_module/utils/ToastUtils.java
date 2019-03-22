package com.matrix.yukun.matrix.video_module.utils;

import android.widget.Toast;

import com.matrix.yukun.matrix.video_module.MyApplication;


/**
 * Created by yukun on 18-1-4.
 */

public class ToastUtils {
    public static void showToast(String txt){
        Toast.makeText(MyApplication.getMyApp(), txt, Toast.LENGTH_SHORT).show();
    }
}
