package com.ykk.pluglin_video.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * yuklun
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
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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

    /**
     * @return screen height
     */
    public String getDuration (long duration){
        int totalSeconds = (int) (duration / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }

    }
    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity 当前activity
     * @return
     */
    public  Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getWidth( activity);
        int height = getHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }
    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity 当前activity
     * @return
     */
    public  Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getWidth(activity);
        int height = getHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取系统最大音量
     *
     * @param context
     * @return
     */
    public static int getMaxVolume(Context context){
        AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        int maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int streamVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(streamVolume),0);
        return maxVolume;
    }
    /**
     * 获取系统最大音量
     *
     * @param context
     * @return
     */
    public static int getCurrentVolume(Context context){
        AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        int streamVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return streamVolume;
    }

    /**
     * 设置当前音量
     *
     * @param context
     * @param currentVolume 要设置的当前音量
     */
    public static void setCurrentVolume(Context context,int currentVolume){
        AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(currentVolume),0);
    }

    /**
     * 改变屏幕亮度
     * @param activity 页面
     * @param value 0到1.0
     */
    private void changeBrightness(Activity activity,float value) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = value;
        activity.getWindow().setAttributes(layoutParams);
    }

    /**
     *  dp 转 px
     * @param context
     * @param dp dp值
     * @return
     */
    public static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     *  px 转 dp
     * @param context
     * @param px px值
     * @return
     */
    public static int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     *  隐藏软件盘,显示则隐藏,否则显示
     * @param context
     */
    public static void hindOrShowInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软件盘
     * @param activity
     */
    public static void hindInput(Activity activity){
        ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * 获取虚拟键盘高度
     * @param context
     * @return
     */
    public int getBottomKeyboardHeight(Activity context) {
        int screenHeight = getAccurateScreenDpi(context)[1];
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightDifference = screenHeight - dm.heightPixels;
        return heightDifference;
    }

    /**
     * 获取精确的屏幕大小
     * @param context
     */
    public int[] getAccurateScreenDpi(Activity context) {
        int[] screenWH = new int[2];
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWH;
    }

    public void dimissBottonBar(Activity activity) {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
//       设置屏幕始终在前面，不然点击鼠标，重新出现虚拟按键
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                            // bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }


}
