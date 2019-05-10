package com.matrix.yukun.matrix.selfview.voice;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.matrix.yukun.matrix.R;


/**
 * 自定义语音播放
 *
 * @author Wangxx
 * @date 2017/2/8
 */

public class PlayStartView extends View {

    public final int START = 100;
    public final int STOP  = 101;
    private Runnable mLongPressRunnable;
    private Paint    mRingProgressPaint;
    private Paint    mRingPaint;
    //圆环颜色
    public  int      mRingColor;
    // 圆环进度的颜色
    public  int      mRingProgressColor;
    //中间颜色
    public  int      mCenterColor;
    //圆环的宽度
    public  int      mRingWidth;

    //控件宽高
    private int   mWidth;
    private int   mHeight;
    //中间X坐标
    private int   centerX;
    //中间Y坐标
    private int   centerY;
    //进度
    private float progress;
    //中间方法比例
    private float centerScale;
    //半径
    private int   radius;
    //最大时间
    private int   mRingMax;
    //时间间隔
    private long timeSpan = 100;
    //开始时间
    private long startTime;
    //是否播放中
    private boolean isPlay = false;

    public PlayStartView(Context context) {
        this(context, null);
    }

    public PlayStartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayStartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordStartView);
        mRingColor = typedArray.getColor(R.styleable.RecordStartView_mRingColor, Color.parseColor("#f1f2f7"));
        mRingProgressColor = typedArray.getColor(R.styleable.RecordStartView_mRingProgressColor, Color.parseColor("#4393f9"));
        mRingWidth = typedArray.getDimensionPixelOffset(R.styleable.RecordStartView_mRingWidth, 8);
        mRingMax = typedArray.getInt(R.styleable.RecordStartView_mRingMax, 1000);
        typedArray.recycle();

        mRingPaint = new Paint();
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStrokeWidth(mRingWidth);

        mRingProgressPaint = new Paint();
        mRingProgressPaint.setColor(mRingProgressColor);
        mRingProgressPaint.setStyle(Paint.Style.STROKE);
        mRingProgressPaint.setAntiAlias(true);
        mRingProgressPaint.setStrokeWidth(mRingWidth);

        /**
         * 自己实现点击事件
         */
        mLongPressRunnable = new Runnable() {

            @Override
            public void run() {
                startRecord();
            }
        };
    }

    /**
     * 开始播放
     */
    private void startRecord() {
        if (mOnPlayListener != null) {
            mOnPlayListener.onStartPlay();
        }
        isPlay = true;
        startTime = System.currentTimeMillis();
        mHandler.sendEmptyMessage(START);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.AT_MOST) {
            width = dp2px(100);
        }
        else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = dp2px(100);
        }
        else {
            height = heightSize;
        }
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        //获取中心点的位置
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = centerX - mRingWidth / 2;
    }

    /**
     * 开启播放
     */
    public void startPlay() {
        setBackgroundResource(R.mipmap.ic_chat_record_stop);
        postDelayed(mLongPressRunnable, 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRing(canvas);
        drawRingProgress(canvas);
    }

    /**
     * 绘制圆环
     *
     * @param canvas
     */
    private void drawRing(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mRingPaint);
    }

    /**
     * 绘制圆环进度
     *
     * @param canvas
     */
    private void drawRingProgress(Canvas canvas) {
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, -90, 360 * (1.0f * progress / mRingMax), false, mRingProgressPaint);
    }

    /**
     * dp转px
     *
     * @param dp
     *
     * @return
     */
    public int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (radius < (centerX - mRingWidth / 2)) {
                radius++;
            }
            switch (msg.what) {
                case STOP:
                    if (!isPlay) {
                        postInvalidate();
                        setBackgroundResource(R.mipmap.ic_chat_record_start);
                    }
                    break;
                case START:
                    if (progress < mRingMax) {
                        progress = System.currentTimeMillis() - startTime;
                        postInvalidate();
                        if (mOnPlayListener != null) {
                            mOnPlayListener.onPlaying((long) progress);
                        }
                        mHandler.sendEmptyMessageDelayed(START, timeSpan);
                    }
                    else {
                        restore();
                        mHandler.sendEmptyMessageDelayed(STOP, 500);
                    }
                    break;
            }
        }
    };

    /**
     * 复原
     */
    public void restore() {
        setBackgroundResource(R.mipmap.ic_chat_record_start);
        removeCallbacks(mLongPressRunnable);
        progress = 0;
        centerScale = 0f;
        mHandler.removeMessages(START);
        isPlay = false;
        if (mOnPlayListener != null) {
            mOnPlayListener.onStopPlay();
        }
    }

    /**
     * 设置最大时间
     *
     * @param maxTime
     */
    public void setMaxTime(int maxTime) {
        this.mRingMax = maxTime;
    }

    private OnPlayListener mOnPlayListener;

    public interface OnPlayListener {
        void onStartPlay();

        void onPlaying(long progress);

        void onStopPlay();
    }

    public void setOnPlayListener(OnPlayListener mOnPlayListener) {
        this.mOnPlayListener = mOnPlayListener;
    }
}
