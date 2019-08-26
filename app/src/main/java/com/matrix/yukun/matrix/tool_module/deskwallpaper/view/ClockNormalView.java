package com.matrix.yukun.matrix.tool_module.deskwallpaper.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/8/15
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class ClockNormalView extends View {

    /**
     * 全局画笔
     */
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

    public ClockNormalView(Context context) {
        super(context);
        init(context,null,0);
    }

    public ClockNormalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public ClockNormalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //处理动画，声明全局的处理器
        mAnimator = ValueAnimator.ofFloat(6f, 0f);//由6降到1
        mAnimator.setDuration(150);
        mAnimator.setInterpolator(new LinearInterpolator());//插值器设为线性
        initPaint();
        doInvalidate();
    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(this.getResources().getColor(R.color.color_2c2c2c));

        mHelperPaint.setAntiAlias(true);
        mHelperPaint.setStyle(Paint.Style.FILL);
        mHelperPaint.setStrokeWidth(2);
        mHelperPaint.setColor(this.getResources().getColor(R.color.bg_blue));
        String[] strings=new String[]{"日","一","二","三","四","五","六","七","八","九","十"};
        mList= Arrays.asList(strings);
        mCurrentColor=this.getResources().getColor(R.color.colorAccent);
        mOriginColor=this.getResources().getColor(R.color.color_ececec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = (float)w;
        mHeight = (float)h;
        mHourR = mWidth * 0.136f;
        mMinuteR = mWidth * 0.36f;
        mSecondR = mWidth * 0.36f;
        LogUtil.i("mWidth:"+mWidth+" mHeight:"+mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        Log.e("YView", "---minimumWidth = " + minimumWidth + "");
        Log.e("YView", "---minimumHeight = " + minimumHeight + "");
        int width = measureWidth(minimumWidth, widthMeasureSpec);
        int height = measureHeight(minimumHeight, heightMeasureSpec);
//        setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 绘制
     */
    public void doInvalidate() {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        mHourDeg = -360 / 12f * (hour - 1);
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
//                LogUtil.i("mSecondDeg "+mSecondDeg+" sd "+sd);
//                if (this@TextClockView.mBlock != null) {
//                    this@TextClockView.mBlock?.invoke()
//                } else {
                    invalidate();
//                }
            }
        });
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);//填充背景
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);//原点移动到中心
        //绘制各元件
        drawCenterInfo(canvas);
        drawHour(canvas, mHourDeg);
        drawMinute(canvas, mMinuteDeg);
        drawSecond(canvas, mSecondDeg);
        //辅助线
//        canvas.drawLine(0f, 0f, mWidth, 0f, mHelperPaint);
        canvas.restore();
    }

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
        mPaint.setTextSize(mHourR * 0.28f);
        //处理整体旋转
        canvas.save();
        canvas.rotate(degrees);
        for (int i = 0; i < 12; i++) {
            canvas.save();
            //从x轴开始旋转，每30°绘制一下「几点」，12次就画完了「时圈」
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
            canvas.drawText(toBigCH(i+1)+"点", mHourR, getCenteredY(), mPaint);
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

    public void initWidthHeight(float width, float height) {
        if (this.mWidth < 0) {
            this.mWidth = width;
            this.mHeight = height;
            mHourR = mWidth * 0.143f;
            mMinuteR = mWidth * 0.35f;
            mSecondR = mWidth * 0.35f;
        }
    }
    private int measureWidth(int defaultWidth, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e("YViewWidth", "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth = /*(int) mPaint.measureText(mText) + */getPaddingLeft() + getPaddingRight();
                Log.e("YViewWidth", "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.e("YViewWidth", "---speMode = EXACTLY");
                defaultWidth = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e("YViewWidth", "---speMode = UNSPECIFIED");
                defaultWidth = Math.max(defaultWidth, specSize);
        }
        return defaultWidth;
    }

    private int measureHeight(int defaultHeight, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e("YViewHeight", "---speSize = " + specSize + "");
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight = (int) (-mPaint.ascent() + mPaint.descent()) + getPaddingTop() + getPaddingBottom();
                Log.e("YViewHeight", "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                Log.e("YViewHeight", "---speSize = EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                Log.e("YViewHeight", "---speSize = UNSPECIFIED");
//        1.基准点是baseline
//        2.ascent：是baseline之上至字符最高处的距离
//        3.descent：是baseline之下至字符最低处的距离
//        4.leading：是上一行字符的descent到下一行的ascent之间的距离,也就是相邻行间的空白距离
//        5.top：是指的是最高字符到baseline的值,即ascent的最大值
//        6.bottom：是指最低字符到baseline的值,即descent的最大值
                break;
        }
        return defaultHeight;

    }
}
