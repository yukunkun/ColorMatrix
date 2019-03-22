package com.matrix.yukun.matrix.selfview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.matrix.yukun.matrix.R;

/**
 * Created by yukun on 18-11-12.
 */
public class BMoveView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Paint mPaintLine;
    private RectF mRectF;
    private int mBoardWidth=50;
    private int firstPos;  //第一次点击位置
    private int mRoationx=0;
    private int mRadio=5;
    private int position=0;//点击到的button位置
    private int mLineEndLength;
    private int mLineLength;
    private int mCircleColor;
    private int mLineColor;
    private int mLineDuration;
    private int mLineWidth;
    private int mCircleDuration;
    private int mCircleCenterColor;
    private int mCirclemRadio;
    private int mButonCount;

    public BMoveView(Context context) {
        super(context);
        init(context,null,0);
    }

    public BMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public BMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BMoveView, defStyleAttr, 0);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.BMoveView_circleColor:
                    mCircleColor = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.BMoveView_lineColor:
                    mLineColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.BMoveView_circleCenterColor:
                    mCircleCenterColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.BMoveView_lineDuration:
                    mLineDuration = a.getInt(attr,500);
                    break;
                case R.styleable.BMoveView_lineWidth:
                    mLineWidth = a.getInt(attr, 5);
                    break;
                case R.styleable.BMoveView_circleDuration:
                    mCircleDuration = a.getInt(attr,500);
                    break;
                case R.styleable.BMoveView_circleRadio:
                    mCirclemRadio = a.getInt(attr,500);
                    break;
                case R.styleable.BMoveView_buttonCount:
                    mButonCount = a.getInt(attr,3);
                    break;
            }
        }
        a.recycle();
        mBoardWidth=dip2px(context,mCirclemRadio);
        mRadio=dip2px(context,mLineWidth);
        mPaint=new Paint();
        mPaintLine = new Paint();
    }

    /**
     * 初始化第一次的位置
     * @param firstPos
     * @param lastPos
     */
    public void setTwoPos(int firstPos,int lastPos) {
        this.firstPos = firstPos;
        this.position=lastPos;
        this.mRoationx = 0;
        //动画的方法 （lastPos-firstPos）两次相减得到需要移动的距离
        leftToRigth(lastPos - firstPos);
    }
    /**
     * button个数
     * @param butonCount
     */

    public void setButonCount(int butonCount) {
        mButonCount = butonCount;
    }

    /**
     *
     * @param startLineLastPosition 正为向右,负为想左,如果是1.则跨度为一,如果是2,则跨度为2;
     */
    private void leftToRigth(int startLineLastPosition) {
        startAnim();
        startLineAnim(startLineLastPosition);
        startLineEndAnim(startLineLastPosition);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画弧度
        mPaint.setColor(mCircleColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mRadio);
        mPaint.setStyle(Paint.Style.STROKE);//只有边
        //画圆弧的矩形位置
        mRectF=new RectF(mWidth/(mButonCount*2)-mBoardWidth+position*mWidth/mButonCount,mHeight/2-mBoardWidth,mWidth/(mButonCount*2)+mBoardWidth+position*mWidth/mButonCount,mHeight/2+mBoardWidth);
        canvas.drawArc(mRectF,90,mRoationx,false,mPaint);
        //画圆覆盖
        mPaintLine.setColor(Color.BLUE);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.FILL);
        //可以画内圆圈的颜色
//        canvas.drawArc(mRectF,90,mRoationx,true,mPaintLine);
        //画线条
        mPaintLine.setColor(mLineColor);
        mPaintLine.setStrokeWidth(mRadio);
        //起始和结束不同,每次动画结束位置是相同的,控制起始点和结束点
        canvas.drawLine(mWidth/(mButonCount*2)+firstPos*mWidth/mButonCount+mLineEndLength,mHeight/2+mBoardWidth,mWidth/(mButonCount*2)+firstPos*mWidth/mButonCount+mLineLength,mHeight/2+mBoardWidth, mPaintLine);
    }

    //圆圈的动画
    public void startAnim(){
        ValueAnimator animator = ValueAnimator.ofInt(0,360);
        animator.setDuration(mCircleDuration);
        animator.setStartDelay(mCircleDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRoationx = (int)animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    //线条开始的动画
    private void startLineAnim(int startLineLastPosition){
        ValueAnimator animator = ValueAnimator.ofInt(0,(mWidth/mButonCount)*startLineLastPosition);
        animator.setDuration(mLineDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineLength = (int)animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    //线条结束的动画
    private void startLineEndAnim(int startLineLastPosition){
        ValueAnimator animator = ValueAnimator.ofInt(0,(mWidth/mButonCount)*startLineLastPosition);
        animator.setDuration(mCircleDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineEndLength = (int)animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
