package com.ykk.pluglin_video.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ykk.pluglin_video.utils.MyType;


/**
 * Created by yukun on 17-6-9.
 */

public class BallView extends View {
    private Paint mPaint;
    private Paint mPaintText;
    private int mWidth;
    private int mHeight;
    private int radio=10;
    private float initX=40;
    private float initY=40;
    public BallView(Context context) {
        super(context);
        init(context,null,0);
    }

    public BallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public BallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint=new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dip2px(context,5));
        mPaintText=new Paint();
        mPaintText.setColor(Color.GREEN);
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setTextSize(dip2px(context,12));
        radio=dip2px(context,radio);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width=0;
        int height=0 ;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        mWidth = width;
        mHeight = height;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        canvas.drawCircle(initX,initY,radio,mPaint);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void startAnimation(final View view){
        PointF pointFFirst = new PointF(mWidth/4,mHeight/4);
        PointF pointFSecond = new PointF(mWidth/4*3,mHeight/4*3);
        PointF pointFStart = new PointF(0,mHeight/2);
        PointF pointFEnd = new PointF(mWidth,mHeight/2);

        ValueAnimator animator = ValueAnimator.ofObject(new MyType(pointFFirst, pointFSecond), pointFStart, pointFEnd);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF value = (PointF) animation.getAnimatedValue();
                view.setX(value.x);
                view.setY(value.y);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        set.play(animator);
        set.start();

    }
}
