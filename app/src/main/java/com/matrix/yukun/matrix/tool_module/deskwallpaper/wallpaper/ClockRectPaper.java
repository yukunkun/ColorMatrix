package com.matrix.yukun.matrix.tool_module.deskwallpaper.wallpaper;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import androidx.annotation.RequiresApi;
import android.view.SurfaceHolder;
import android.view.animation.LinearInterpolator;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.service.UIHandler;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/8/19
 */
public class ClockRectPaper extends WallpaperService {

    private Paint mPaint = new Paint();
    private Paint mHelperPaint = new Paint();
    private float mWidth = -1f;
    private float mHourR;
    private float mHourDeg;
    private float mMinuteDeg;
    private float mSecondDeg;
    private  int mCurrentColor;
    private  int mOriginColor;
    private ValueAnimator mAnimator;
    private Canvas mLockCanvas;
    private float mDistance=4;
    private Thread mThread;
    private SurfaceHolder mSurfaceHolder;
    private String[] strings=new String[]{"日","一","二","三","四","五","六","七","八","九","十"};
    private List<String> mList=new ArrayList<>();
    public static boolean isStart=false;

    @Override
    public Engine onCreateEngine() {
        return new ClockEngine();
    }

    public class ClockEngine extends Engine{
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            mSurfaceHolder = surfaceHolder;
            LogUtil.i("---------","onCreate");
            init();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            isStart=false;
            LogUtil.i("---------","onDestroy");
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            startAnimation();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            LogUtil.i("---------","surfaceDestroyed");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            LogUtil.i("---------",visible+"");
            if(visible){
                startAnimation();
            }else {
                isStart=false;
            }
        }
        private void init() {
            //处理动画，声明全局的处理器
            mAnimator = ValueAnimator.ofFloat(6f, 0f);//由6降到1
            mAnimator.setDuration(150);
            mAnimator.setInterpolator(new LinearInterpolator());//插值器设为线性
            mWidth= ScreenUtil.screenWidth;
            mHourR = mWidth * 0.138f;
            initPaint();
            doInvalidate();
        }

        private void initPaint() {
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(3);
            mPaint.setColor(MyApp.myApp.getResources().getColor(R.color.color_2c2c2c));
            mHelperPaint.setAntiAlias(true);
            mHelperPaint.setStyle(Paint.Style.FILL);
            mHelperPaint.setStrokeWidth(2);
            mHelperPaint.setColor(MyApp.myApp.getResources().getColor(R.color.bg_blue));
            mList= Arrays.asList(strings);
            mCurrentColor= MyApp.myApp.getResources().getColor(R.color.colorAccent);
            mOriginColor= MyApp.myApp.getResources().getColor(R.color.color_ececec);
        }
    }
    /**
     * 绘制
     */
    public  void doInvalidate() {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
            int hour = Calendar.getInstance().get(Calendar.HOUR);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int second = Calendar.getInstance().get(Calendar.SECOND);
            mHourDeg = -360 / 12f * (hour/2);
            mMinuteDeg = -360 / 60f * (minute - 1);
            mSecondDeg = -360 / 60f * (second - 1);
            //记录当前角度，然后让秒圈线性的旋转6°
            float hd = mHourDeg;
            float md = mMinuteDeg;
            float sd = mSecondDeg;
            //处理动画
            mAnimator.removeAllUpdateListeners();//需要移除先前的监听
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float av = (float) animation.getAnimatedValue();
                    if (minute == 0 && second == 0) {
                        mHourDeg = hd + av * 5;//时圈旋转角度是分秒的5倍，线性的旋转30°
                    }
                    if (second == 0) {
                        mMinuteDeg = md + av;//线性的旋转6°
                    }
                    mSecondDeg = sd + av;//线性的旋转6°
                    drawClock();
                }
            });
            mAnimator.start();
            LogUtil.i("---------","drawClock");
        }
    }


    private void startAnimation() {
        mThread = new Thread(new MyRunnable());
        mThread.start();
    }

    class MyRunnable implements Runnable{
        @Override
        public void run() {
            isStart=true;
            while (isStart){
                try {
                    Thread.sleep(1000);
                    LogUtil.i("----------","sleep");
                    UIHandler.run(new Runnable() {
                        @Override
                        public void run() {
                            doInvalidate();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawClock() {
        if(mSurfaceHolder!=null&&mSurfaceHolder.getSurface()!=null&&mSurfaceHolder.getSurface().isValid()){
            synchronized (this){
                mLockCanvas=mSurfaceHolder.lockCanvas();
                onDraw();
                try{
                    mSurfaceHolder.unlockCanvasAndPost(mLockCanvas);
//                    LogUtil.i("---------","mSurfaceHolder");
                }
                catch (Exception e){
                    LogUtil.i(e.toString());
                }
            }
        }
    }

    private void onDraw() {
        //获取到Canvas
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            if(mLockCanvas!=null){
                mLockCanvas.drawColor(Color.BLACK);//填充背景
                mLockCanvas.translate(0,50);
                mLockCanvas.save();
                //绘制各元件
                drawMonthInfo(mLockCanvas);
                drawDayInfo(mLockCanvas);
                drawWeekInfo(mLockCanvas);
                drawHour(mLockCanvas);
                drawMinute(mLockCanvas);
                drawSecond(mLockCanvas);
                mLockCanvas.restore();
            }
        }
    }
    /**
     * month
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawMonthInfo(Canvas canvas) {
        mPaint.setTextSize(mHourR * 0.24f);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        for (int i = 0; i < 12; i++) {
            if(i+1==month){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6f*255));
            }
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(toBigCH(i+1)+"月",(mWidth/12/2)*(i*2+1), (getTextdY()+getToppedY()-mDistance)*2, mPaint);
        }
    }

    /**
     * day
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawDayInfo(Canvas canvas) {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1;
        for (int i = 0; i < 31; i++) {
            if(i==day){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6f*255));
            }
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(toBigCH(i+1)+"号",(mWidth/7/2)*(i%7*2+1), (getTextdY()+getToppedY()-mDistance)*(i/7+3), mPaint);
        }
    }

    /**
     * week
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawWeekInfo(Canvas canvas) {
        int day = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
        for (int i = 0; i < 7; i++) {
            if(i==day){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6f*255));
            }
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("星期"+mList.get(i),(mWidth/7/2)*(i%7*2+1), (getTextdY()+getToppedY()-mDistance)*(i/7+8), mPaint);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawHour(Canvas canvas) {
        int hour = Calendar.getInstance().get(Calendar.HOUR)-1;
        for (int i = 0; i < 12; i++) {
            if(hour==i){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6f*255));
            }
            mPaint.setTextAlign(Paint.Align.CENTER);
//            if(i<=20){
            canvas.drawText(toBigCH(i+1)+"点", mWidth/12/2*(i%12*2+1), (getTextdY()+getToppedY()-mDistance)*(i/12+9), mPaint);
//            }else {
//                canvas.drawText(mList.get(2)+toBigCH(i-20+1)+"点", mWidth/12/2*(i%12*2+1), (getTextdY()+getToppedY()-mDistance)*(i/12+8), mPaint);
//            }
        }
    }

    /**
     * Minute
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawMinute(Canvas canvas) {
        int hour = Calendar.getInstance().get(Calendar.MINUTE)-1;
        for (int i = 0; i < 59; i++) {
            if(hour==i){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6f*255));
            }
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(toBigCH(i+1)+"分", mWidth/7/2*(i%7*2+1), (getTextdY()+getToppedY()-mDistance)*(i/7+10), mPaint);
        }
    }

    /**
     * Second
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawSecond(Canvas canvas) {
        int hour = Calendar.getInstance().get(Calendar.SECOND);
        for (int i = 3; i < 62; i++) {
            if(hour==i-3){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6f*255));
            }
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(toBigCH(i+1-3)+"秒", mWidth/7/2*((i%7*2)+1), (getTextdY()+getToppedY()-mDistance)*(i/7+18), mPaint);
        }
    }

    /**
     * 扩展获取绘制文字时在x轴上 垂直居中的y坐标
     */
    private float getToppedY() {
        return -mPaint.getFontMetrics().ascent;
    }

    private float getTextdY(){
        return -(mPaint.getFontMetrics().leading+mPaint.getFontMetrics().descent+mPaint.getFontMetrics().ascent);
    }

    public String toBigCH(int i){
        if(i<=10){
            return mList.get(i);
        }else if(i<=20){
            if(i==20){
                return mList.get(2)+mList.get(10);
            }else {
                return mList.get(10)+mList.get(i-10);
            }
        }else if(i<=30){
            if(i==30){
                return mList.get(3)+mList.get(10);
            }else {
                return mList.get(2)+mList.get(10)+mList.get(i-20);
            }
        }else  if(i<=40){
            if(i==40){
                return mList.get(4)+mList.get(10);
            }else {
                return mList.get(3)+mList.get(10)+mList.get(i-30);
            }
        }else  if(i<=50){
            if(i==50){
                return mList.get(5)+mList.get(10);
            }else {
                return mList.get(4)+mList.get(10)+mList.get(i-40);
            }
        }else  if(i<60){
            return mList.get(5)+mList.get(10)+mList.get(i-50);
        }else {
            return "";
        }
    }
}
