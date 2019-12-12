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
public class ClockTimePaper extends WallpaperService {

    private Paint mPaint = new Paint();
    private Paint mHelperPaint = new Paint();
    private float mWidth = -1f;
    private float mHeight;
    private float mHourR;
    private float mMinuteR;
    private float mSecondR;
    private float mHourDeg;
    private float mMinuteDeg;
    private float mSecondDeg;
    private  int mCurrentColor;
    private  int mOriginColor;
    private ValueAnimator mAnimator;
    private List<String> mList=new ArrayList<>();
    private Canvas mLockCanvas;
    private Thread mThread;
    private SurfaceHolder mSurfaceHolder;
    private String[] strings=new String[]{"日","一","二","三","四","五","六","七","八","九","十"};
    String[] times=new String[]{"子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"};
    private List<String> mListTime=new ArrayList<>();
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
            mHeight=ScreenUtil.screenHeight;
            mHourR = mWidth * 0.138f;
            mMinuteR = mWidth * 0.36f;
            mSecondR = mWidth * 0.36f;
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
            mListTime=Arrays.asList(times);
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
                mLockCanvas.save();
                mLockCanvas.translate(mWidth / 2, mHeight / 2);//原点移动到中心
                //绘制各元件
                drawCenterInfo(mLockCanvas);
                drawHour(mLockCanvas, mHourDeg);
                drawMinute(mLockCanvas, mMinuteDeg);
                drawSecond(mLockCanvas, mSecondDeg);
                mLockCanvas.restore();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawCenterInfo(Canvas canvas) {
        //绘制数字时间
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        String minuteStr ;
        if (minute < 10){
            minuteStr="0"+minute;
        }else {
            minuteStr=minute+"";
        }
        mPaint.setColor(mCurrentColor);
        mPaint.setTextSize(mHourR * 0.46f);
        mPaint.setAlpha(255);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(hour+":"+minuteStr, 0f, getBottomedY(), mPaint);
        //绘制月份、星期
        int mon = Calendar.getInstance().get(Calendar.MONTH)+1;
        String monthStr ;
        if (mon < 10){
            monthStr="0"+mon;
        }else {
            monthStr=mon+"";
        }
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
        mPaint.setTextSize(mHourR * 0.24f);
        mPaint.setAlpha(255);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(monthStr+"."+day+" 星期"+mList.get(dayOfWeek), 0f, getToppedY(), mPaint);
    }

    private void drawHour(Canvas canvas, float degrees) {
        mPaint.setTextSize(mHourR * 0.30f);
        //处理整体旋转
        canvas.save();
        canvas.rotate(degrees);
        for (int i = 0; i < 12; i++) {
            canvas.save();
            //从x轴开始旋转，每15°绘制一下「几点」，24次就画完了「时圈」
            float iDeg = 360 / 12f * i;
            canvas.rotate(iDeg);
            //这里处理当前时间点的透明度，因为degrees控制整体逆时针旋转
            //iDeg控制绘制时顺时针，所以两者和为0时，刚好在x正半轴上，也就是起始绘制位置。
            if(iDeg + degrees == 0f){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha((255));
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6 * 255));
            }
            mPaint.setTextAlign( Paint.Align.LEFT);
            canvas.drawText(mListTime.get(i)+"时", mHourR, getCenteredY(), mPaint);
            canvas.restore();
        }
        canvas.restore();
    }

    private void drawMinute(Canvas canvas, float minuteDeg) {
        mPaint.setTextSize( mHourR * 0.22f);
        //处理整体旋转
        canvas.save();
        canvas.rotate(minuteDeg);
        for (int i = 0; i < 60; i++) {
            canvas.save();
            float iDeg = 360 / 60f * i;
            canvas.rotate(iDeg);
            if(iDeg + minuteDeg == 0f){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(255*0.6f));
            }
            mPaint.setTextAlign(Paint.Align.RIGHT);
            if (i < 60) {
                canvas.drawText(toBigCH(i+1)+"分", mMinuteR, getCenteredY(), mPaint);
            }
            canvas.restore();
        }
        canvas.restore();
    }

    private void drawSecond(Canvas canvas, float secondDeg) {
        mPaint.setTextSize(mHourR * 0.22f);
        //处理整体旋转
        canvas.save();
        canvas.rotate(secondDeg);
        for (int i = 0; i < 60; i++) {
            canvas.save();
            float iDeg = 360 / 60f * i;
            canvas.rotate(iDeg);
            if(iDeg + secondDeg == 0f){
                mPaint.setColor(mCurrentColor);
                mPaint.setAlpha(255);
            }else {
                mPaint.setColor(mOriginColor);
                mPaint.setAlpha((int)(0.6f * 255));
            }
            mPaint.setTextAlign(Paint.Align.LEFT);
            if (i < 59) {
                canvas.drawText(toBigCH(i+1)+"秒", mSecondR+5, getCenteredY(), mPaint);
            }
            canvas.restore();
        }
        canvas.restore();
    }

    /**
     * 扩展获取绘制文字时在x轴上 垂直居中的y坐标
     */
    private float getCenteredY() {
        return mPaint.getFontSpacing() / 2 - mPaint.getFontMetrics().bottom;
    }

    /**
     * 扩展获取绘制文字时在x轴上 垂直居中的y坐标
     */
    private float getToppedY() {
        return -mPaint.getFontMetrics().ascent;
    }

    /**
     * 扩展获取绘制文字时在x轴上 贴紧x轴的上边缘的y坐标
     */
    private float getBottomedY(){
        return -mPaint.getFontMetrics().bottom;
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
