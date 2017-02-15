package com.matrix.yukun.matrix.util;

import android.content.Context;
import android.view.WindowManager;


/**
 * Created by root on 15-9-6.shul
 *
 * Screen StringUtil to get width height dp egg.
 */
public class ScreenUtils {
    private static ScreenUtils mUtils = new ScreenUtils();
    public static ScreenUtils instance(){
        return mUtils;
    }

    /**
     * @return screen width
     */
    public int getWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return  width;
    }

    /**
     * @return screen height
     */
    public int getHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }

    public static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
