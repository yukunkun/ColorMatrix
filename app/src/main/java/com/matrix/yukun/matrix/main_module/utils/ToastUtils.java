package com.matrix.yukun.matrix.main_module.utils;

import android.widget.Toast;

import com.matrix.yukun.matrix.MyApp;

/**
 * Created by yukun on 18-1-4.
 */

public class ToastUtils {
    public static void showToast(String txt){
        Toast.makeText(MyApp.myApp, txt, Toast.LENGTH_SHORT).show();
    }
}
