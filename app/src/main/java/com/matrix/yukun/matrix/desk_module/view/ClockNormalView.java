package com.matrix.yukun.matrix.desk_module.view;

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
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.properties.Delegates;

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

    private ValueAnimator mAnimator;
    private List<String> mList=new ArrayList<>();

    private Calendar mCalendar=Calendar.getInstance();

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
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = (float)widthMeasureSpec;
        mHeight = (float)heightMeasureSpec;
        mHourR = mWidth * 0.143f;
        mMinuteR = mWidth * 0.35f;
        mSecondR = mWidth * 0.35f;
        LogUtil.i("mWidth:"+mWidth+" mHeight:"+mHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 绘制
     */
    private void doInvalidate() {
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        int second = mCalendar.get(Calendar.SECOND);
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

//                if (this@TextClockView.mBlock != null) {
//                    this@TextClockView.mBlock?.invoke()
//
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

        canvas.drawColor(Color.WHITE);//填充背景
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);//原点移动到中心
        //绘制各元件
        drawCenterInfo(canvas);
        drawHour(canvas, mHourDeg);
        drawMinute(canvas, mMinuteDeg);
        drawSecond(canvas, mSecondDeg);
        //辅助线
        canvas.drawLine(0f, 0f, mWidth, 0f, mHelperPaint);
        canvas.restore();
    }

    private void drawCenterInfo(Canvas canvas) {
       //绘制数字时间
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        String minuteStr ;
        if (minute < 10){
            minuteStr="0";
        }else {
            minuteStr=minute+"";
        }
        mPaint.setTextSize(mHourR * 0.4f);
        mPaint.setColor(Color.BLUE);
        mPaint.setAlpha(120);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(hour+":"+minuteStr, 0f, getBottomedY(), mPaint);

        //绘制月份、星期
        int mon = mCalendar.get(Calendar.MONTH);
        String monthStr ;
        if (mon < 10){
            monthStr="0";
        }else {
            monthStr=mon+"";
        }
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = ((mCalendar.get(Calendar.DAY_OF_WEEK)) - 1);
        mPaint.setTextSize(mHourR * 0.16f);
        mPaint.setAlpha(120);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(monthStr+"."+day+" 星期"+dayOfWeek, 0f, getToppedY(), mPaint);
    }

    private void drawHour(Canvas canvas, float hourDeg) {


    }

    private void drawMinute(Canvas canvas, float minuteDeg) {

    }

    private void drawSecond(Canvas canvas, float secondDeg) {

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

}
