package com.matrix.yukun.matrix.selfview.floatingview;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 悬浮窗管理器
 *
 * @author PengZhenjin
 * @date 2017-6-5
 */
public class FloatingViewManager implements View.OnTouchListener {

    private static final String TAG = "FloatingViewManager";

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * WindowManager
     */
    private  static WindowManager mWindowManager;

    /**
     * FloatingView
     */
    private static  FloatingView mFloatingView;

    private static FloatingViewManager instance;

    private boolean isMove;

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    private FloatingViewManager(Context context) {
        this.mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static FloatingViewManager getInstance(Context context) {

        if (instance == null) {
            return new FloatingViewManager(context);
        }
        else {
            instance.removeFloatingView();
            instance.setmContext(context);
            return instance;
        }
    }


    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * 添加悬浮窗
     *
     * @param view    悬浮窗视图组件
     * @param configs 悬浮窗的配置信息
     */
    public void addFloatingView(View view, Configs configs) {
        removeFloatingView();
        // 创建悬浮窗
        mFloatingView = new FloatingView(this.mContext, configs.floatingViewX, configs.floatingViewY);
        mFloatingView.setOnTouchListener(this);
        mFloatingView.setOverMargin(configs.overMargin);
        mFloatingView.setMoveDirection(configs.moveDirection);
        mFloatingView.setAnimateInitialMove(configs.animateInitialMove);

        // 设置悬浮窗的大小
        FrameLayout.LayoutParams targetParams = new FrameLayout.LayoutParams(configs.floatingViewWidth, configs.floatingViewHeight);
        view.setLayoutParams(targetParams);
        mFloatingView.addView(view);

        WindowManager.LayoutParams lp = mFloatingView.getWindowLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            lp.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        // 添加悬浮窗
        this.mWindowManager.addView(mFloatingView, lp);
    }


    /**
     * 移除悬浮窗
     */
    public static void removeFloatingView() {
        if (mFloatingView !=null&& mWindowManager !=null) {
            try {
                mFloatingView.removeAllViews();
                mWindowManager.removeViewImmediate(mFloatingView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mFloatingView = null;
        }
    }


    /**
     * 悬浮窗的配置信息
     */
    public static class Configs {
        /**
         * 悬浮窗的x坐标
         */
        public int floatingViewX;

        /**
         * 悬浮窗的y坐标
         */
        public int floatingViewY;

        /**
         * 悬浮窗的宽度（单位：px）
         */
        public int floatingViewWidth;

        /**
         * 悬浮窗的高度（单位：px）
         */
        public int floatingViewHeight;

        /**
         * 悬浮窗边缘的外边距
         */
        public int overMargin;

        /**
         * 悬浮窗移动方向
         */
        @FloatingView.MoveDirection
        public int moveDirection;

        /**
         * 悬浮窗移动时是否带动画
         */
        public boolean animateInitialMove;

        public Configs() {
            this.floatingViewX = FloatingView.DEFAULT_X;
            this.floatingViewY = FloatingView.DEFAULT_Y;
            this.floatingViewWidth = FloatingView.DEFAULT_WIDTH;
            this.floatingViewHeight = FloatingView.DEFAULT_HEIGHT;
            this.overMargin = 0;
            this.moveDirection = FloatingView.MOVE_DIRECTION_DEFAULT;
            this.animateInitialMove = true;
        }
    }

    public static boolean isMove(){
        return mFloatingView.isMove();
    }

}
