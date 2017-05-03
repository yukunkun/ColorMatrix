package com.matrix.yukun.matrix.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.matrix.yukun.matrix.R;


/**
 * Created by yukun on 17-5-2.
 */
public class VoiceLoadView extends View {
    private Paint mPaint=new Paint();
    private int mWidth;
    private int mHeight;
    private int mRadio;
    private int arcCount;
    private int mCurrentCount;
    private int mSplitSize;
    private int mCircleWidth;
    private Rect mRect=new Rect();
    private Bitmap mImage;
    private double mRectImage;
    private int mFirstColor;
    private int mSecondColor;
    private int mImageDevide;

    public void setCurrentCount(int currentCount) {
        mCurrentCount = currentCount;
        invalidate();
    }

    public VoiceLoadView(Context context) {
        super(context);
        init(context,null,0);

    }

    public VoiceLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);

    }

    public VoiceLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VoiceLoadView, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.VoiceLoadView_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.VoiceLoadView_secondColor:
                    mSecondColor = a.getColor(attr, Color.CYAN);
                    break;
                case R.styleable.VoiceLoadView_bg:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.VoiceLoadView_circleWidth:
                    mCircleWidth = dip2px(context,a.getInt(attr,20));
                    break;
                case R.styleable.VoiceLoadView_dotCount:
                    arcCount = a.getInt(attr, 10);// 默认20
                    break;
                case R.styleable.VoiceLoadView_splitSize:
                    mSplitSize = a.getInt(attr,20);
                    break;
                case R.styleable.VoiceLoadView_circleRadio:
                    mRadio = dip2px(context,a.getInt(attr,20));
                    break;
                case R.styleable.VoiceLoadView_imageDevide:
                    mImageDevide = dip2px(context,a.getInt(attr,20));
                    break;
            }
        }
        a.recycle();
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

        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心

        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例*360
         */
        float itemSize = (360 * 1.0f - arcCount * mSplitSize) / arcCount;

        //中间显示矩形
        RectF oval = new RectF(mWidth/2 - mRadio, mHeight/2 - mRadio, mWidth/2 + mRadio, mHeight/2 + mRadio); // 用于定义的圆弧的形状和大小的界限

        mPaint.setColor(mFirstColor); // 设置圆环的颜色
        for (int i = 0; i < arcCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }

        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头

        mPaint.setColor(mSecondColor); // 设置圆环的颜色
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }

        //bitmap
        int relRadius = mRadio - mCircleWidth / 2;// 获得内圆的半径
        /**
         * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
         */
        mRectImage = Math.sqrt(2) * 1.0f / 2 * relRadius/*+mCircleWidth*/-mImageDevide; //这里获得内切矩形的半个边长

        mRect.left=(int)(mWidth/2-mRectImage);
        mRect.top = (int) (mHeight/2 - mRectImage);
        mRect.right = (int) (mRect.left + 2*mRectImage);
        mRect.bottom = (int) (mRect.top + 2*mRectImage);

        // 绘图
        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
