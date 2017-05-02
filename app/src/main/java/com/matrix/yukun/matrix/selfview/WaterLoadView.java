package com.matrix.yukun.matrix.selfview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.matrix.yukun.matrix.R;


/**
 * Created by yukun on 17-5-2.
 */
public class WaterLoadView extends View {
    private Path path=new Path();;
    private Paint mPaint=new Paint();
    private int mWidth;
    private int mHeight;
    private float mRadioFirst;
    private int mRadioBig=2;
    private int topWidth;
    private float mValue;
    private float mCircleValue;
    private int mCircleColor;
    private int mCircleSmallRadio;
    private int mCircleTop;
    private int mLoadTime;

    public WaterLoadView(Context context) {
        super(context);
        init(context,null,0);
    }

    public WaterLoadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public WaterLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaterLoadView, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.WaterLoadView_circleColor:
                    mCircleColor = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.WaterLoadView_circleSmallRadio:
                    mCircleSmallRadio = a.getInt(attr, 15);
                    break;
                case R.styleable.WaterLoadView_circleTop:
                    mCircleTop = a.getInt(attr, 70);
                    break;
                case R.styleable.WaterLoadView_loadTime:
                    mLoadTime = a.getInt(attr, 1500);
                    break;
            }
        }
        mRadioFirst=dip2px(context,mCircleSmallRadio);
        topWidth=(int) dip2px(context,mCircleTop);
        setAnim();
        setCircleAnim();
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mCircleColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth/2,(float) (topWidth-4*mRadioFirst*mValue),mRadioFirst,mPaint);
        canvas.drawCircle(mWidth/2,(float) (topWidth-7*mRadioFirst*mValue),mRadioFirst*0.85f,mPaint);
        canvas.drawCircle(mWidth/2,(float) (topWidth-10*mRadioFirst*mValue),mRadioFirst*0.7f,mPaint);
        path.reset();
        path.moveTo(mWidth/2-mRadioFirst-mRadioFirst*mCircleValue,topWidth);
        path.quadTo(mWidth/2,(topWidth-mRadioFirst+mRadioFirst-mRadioFirst*6*mCircleValue),mWidth/2+mRadioFirst+mRadioFirst*mCircleValue,topWidth);
        path.close();
        canvas.drawPath(path,mPaint);
        canvas.drawCircle(mWidth/2,topWidth,mRadioFirst+mRadioFirst*mCircleValue,mPaint);
    }

    private void setAnim() {
        ValueAnimator valueAnimator= ValueAnimator.ofFloat(1,0,1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(mLoadTime);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }
    private void setCircleAnim() {
        ValueAnimator valueAnimator= ValueAnimator.ofFloat(0.5f,1f,0.5f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCircleValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(mLoadTime);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    public  float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }
}
