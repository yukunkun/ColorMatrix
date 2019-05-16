package com.matrix.yukun.matrix.selfview.voice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.matrix.yukun.matrix.R;

/**
 * 自定义动画按钮
 *
 * @author Wangxx
 * @date 2017/2/8
 */
public class AuditionButton extends AppCompatImageView {

    /**
     * 中心点的X坐标
     */
    private int    mCenterX;
    /**
     * 中心点的Y坐标
     */
    private int    mCenterY;
    /**
     * 画笔
     */
    private Paint  mPaint;
    /**
     * 半径
     */
    private double mRadius;

    /**
     * 圆和图片的间距
     */
    private float padding = 20;

    /**
     * 扩大值
     */
    private float expandValue = 0f;

    /**
     * 缩放系数
     */
    private double zoom = 1.5f;

    //宽度
    private int width;

    public AuditionButton(Context context) {
        this(context, null);
    }

    public AuditionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.C3));
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        post(new Runnable() {
            @Override
            public void run() {
                int dw = getDrawable().getBounds().width();
                int dh = getDrawable().getBounds().height();
                //图片宽高应该相等,这里随便用什么
                mRadius = (dw / 2) + padding;
                width = getWidth();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        canvas.drawCircle(mCenterX, mCenterY, (float) mRadius + expandValue, mPaint);
        super.onDraw(canvas);
    }

    /**
     * 设置当前选中状态
     *
     * @param isSelected
     */
    public void setSelected(boolean isSelected) {
        if (isSelected) {
            mPaint.setStyle(Paint.Style.FILL);
        }
        else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        invalidate();
    }

    public void setDistance(double distance) {
        if (distance > 1.0f) {
            expandValue = 0;
            invalidate();
            return;
        }
        double ratio = (1 - distance);
        if (ratio > zoom) {
            ratio = zoom;
        }
        float temp = (float) (width / 2 - mRadius);
        expandValue = (float) (temp * ratio);
        invalidate();
    }
}
