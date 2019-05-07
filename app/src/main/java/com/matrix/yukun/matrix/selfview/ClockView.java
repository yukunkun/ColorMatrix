package com.matrix.yukun.matrix.selfview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.matrix.yukun.matrix.R;

/**
 * author: kun .
 * date:   On 2019/4/16
 */
public class ClockView extends View {
    private static final int DEFAULT_COLOR_LOWER = Color.parseColor("#1d953f");
    private static final int DEFAULT_COLOR_MIDDLE = Color.parseColor("#228fbd");
    private static final int DEFAULT_COLOR_HIGH = Color.RED;
    private static final int DEAFAULT_COLOR_TITLE = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE_DIAL = 11;
    private static final int DEFAULT_STROKE_WIDTH = 8;
    private static final int DEFAULT_RADIUS_DIAL = 128;
    private static final int DEAFAULT_TITLE_SIZE = 16;
    private static final int DEFAULT_VALUE_SIZE = 20;
    private static final int DEFAULT_ANIM_PLAY_TIME = 2000;

    private int colorDialLower;
    private int colorDialMiddle;
    private int colorDialHigh;
    private int textSizeDial;
    private int strokeWidthDial;
    private String titleDial;
    private int titleDialSize;
    private int titleDialColor;
    private int valueTextSize;
    private int animPlayTime;

    private int radiusDial;
    private int mRealRadius;
    private float currentValue;

    private Paint arcPaint;
    private RectF mRect;
    private Paint pointerPaint;
    private Paint.FontMetrics fontMetrics;
    private Paint titlePaint;
    private Path pointerPath;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        colorDialLower = attributes.getColor(R.styleable.ClockView_color_dial_lower, DEFAULT_COLOR_LOWER);
        colorDialMiddle = attributes.getColor(R.styleable.ClockView_color_dial_middle, DEFAULT_COLOR_MIDDLE);
        colorDialHigh = attributes.getColor(R.styleable.ClockView_color_dial_high, DEFAULT_COLOR_HIGH);
        textSizeDial = (int) attributes.getDimension(R.styleable.ClockView_text_size_dial, sp2px(DEFAULT_TEXT_SIZE_DIAL));
        strokeWidthDial = (int) attributes.getDimension(R.styleable.ClockView_stroke_width_dial, dp2px(DEFAULT_STROKE_WIDTH));
        radiusDial = (int) attributes.getDimension(R.styleable.ClockView_radius_circle_dial, dp2px(DEFAULT_RADIUS_DIAL));
        titleDial = attributes.getString(R.styleable.ClockView_text_title_dial);
        titleDialSize = (int) attributes.getDimension(R.styleable.ClockView_text_title_size, dp2px(DEAFAULT_TITLE_SIZE));
        titleDialColor = attributes.getColor(R.styleable.ClockView_text_title_color, DEAFAULT_COLOR_TITLE);
        valueTextSize = (int) attributes.getDimension(R.styleable.ClockView_text_size_value, dp2px(DEFAULT_VALUE_SIZE));
        animPlayTime = attributes.getInt(R.styleable.ClockView_animator_play_time, DEFAULT_ANIM_PLAY_TIME);
    }

    private void initPaint(){
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(strokeWidthDial);

        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setTextSize(textSizeDial);
        pointerPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = pointerPaint.getFontMetrics();

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setFakeBoldText(true);

        pointerPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int mWidth, mHeight;
        if (widthMode == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }else {
            mWidth = getPaddingLeft() + radiusDial * 2 + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST){
                mWidth = Math.min(mWidth, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize;
        }else {
            mHeight = getPaddingTop() + radiusDial * 2 + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST){
                mHeight = Math.min(mHeight, heightSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight);

        radiusDial = Math.min((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()),
                (getMeasuredHeight() - getPaddingTop() - getPaddingBottom())) / 2;
        mRealRadius = radiusDial - strokeWidthDial / 2;
        mRect = new RectF(-mRealRadius, -mRealRadius, mRealRadius, mRealRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
        drawPointerLine(canvas);
        drawTitleDial(canvas);
        drawPointer(canvas);
    }

    private void drawArc(Canvas canvas){
        canvas.translate(getPaddingLeft() + radiusDial, getPaddingTop() + radiusDial);
        arcPaint.setColor(colorDialLower);
        canvas.drawArc(mRect, 135, 54, false, arcPaint);
        arcPaint.setColor(colorDialMiddle);
        canvas.drawArc(mRect, 189, 162, false, arcPaint);
        arcPaint.setColor(colorDialHigh);
        canvas.drawArc(mRect, 351, 54, false, arcPaint);
    }

    private void drawPointerLine(Canvas canvas){
        canvas.rotate(135);
        for (int i=0; i<101; i++){     //一共需要绘制101个表针

            if (i <= 20){
                pointerPaint.setColor(colorDialLower);
            }else if (i<= 80){
                pointerPaint.setColor(colorDialMiddle);
            }else {
                pointerPaint.setColor(colorDialHigh);
            }

            if (i % 10 == 0){     //长表针
                pointerPaint.setStrokeWidth(6);
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(15), 0, pointerPaint);

                drawPointerText(canvas, i);
            }else {    //短表针
                pointerPaint.setStrokeWidth(3);
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(5), 0, pointerPaint);
            }
            canvas.rotate(2.7f);
        }
    }

    private void drawPointerText(Canvas canvas, int i){
        canvas.save();
        int currentCenterX = (int) (radiusDial - strokeWidthDial - dp2px(21) - pointerPaint.measureText(String.valueOf(i)) / 2);
        canvas.translate(currentCenterX, 0);
        canvas.rotate(360 - 135 - 2.7f * i);        //坐标系总旋转角度为360度

        int textBaseLine = (int) (0 + (fontMetrics.bottom - fontMetrics.top) /2 - fontMetrics.bottom);
        canvas.drawText(String.valueOf(i*10)+"M", 0, textBaseLine, pointerPaint);
        canvas.restore();
    }

    private void drawTitleDial(Canvas canvas){
        titlePaint.setColor(titleDialColor);
        titlePaint.setTextSize(titleDialSize);
        canvas.rotate( -47.7f);       //恢复坐标系为起始中心位置
        canvas.drawText(titleDial, 0, -radiusDial / 3, titlePaint);

        if (currentValue <= 20){
            titlePaint.setColor(colorDialLower);
        }else if (currentValue <= 80){
            titlePaint.setColor(colorDialMiddle);
        }else {
            titlePaint.setColor(colorDialHigh);
        }
        titlePaint.setTextSize(valueTextSize);
        canvas.drawText(String.format("%.2f",currentValue*10) + "M", 0, radiusDial * 2/3, titlePaint);
    }

    private void drawPointer(Canvas canvas){
        int currentDegree = (int) (currentValue * 2.7 + 135);
        canvas.rotate(currentDegree);

        pointerPath.moveTo(radiusDial - strokeWidthDial - dp2px(12), 0);
        pointerPath.lineTo(0, -dp2px(5));
        pointerPath.lineTo(-12, 0);
        pointerPath.lineTo(0, dp2px(5));
        pointerPath.close();
        canvas.drawPath(pointerPath,titlePaint);
    }

    public void setCompleteDegree(float degree){

        ValueAnimator animator = ValueAnimator.ofFloat(0, degree);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float)(Math.round((float) animation.getAnimatedValue() * 100)) / 100;
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(animPlayTime);
        animator.start();
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
