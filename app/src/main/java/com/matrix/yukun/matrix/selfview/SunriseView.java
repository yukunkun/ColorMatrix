package com.matrix.yukun.matrix.selfview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.matrix.yukun.matrix.R;


/**
 * author: kun .
 * date:   On 2019/10/11
 */
public class SunriseView extends View {

    private DisplayMetrics mDisplayMetrics;
    private Paint mPaint;
    private Paint mPaintText;
    private Paint mPaintRec;
    private int   mWidth;
    private int   mHeight;
    private int   radio;
    private String mHeadText="日出";
    private String mBackText="日落";
    private double progress=0.0;
    private Context mContext;
    private Bitmap mBitmap;
    private int icon ;

    public SunriseView(Context context) {
        super(context);
        init(context,null,0);
    }

    public SunriseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public SunriseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mDisplayMetrics=getResources().getDisplayMetrics();
        mContext=context;
        if(icon!=0){
            mBitmap = BitmapFactory.decodeResource(context.getResources(), icon);
        }
        mPaint=new Paint();
        mPaint.setStrokeWidth(20);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setColor(context.getResources().getColor(R.color.color_f733d6));
        mPaintRec=new Paint();
        mPaintRec.setStrokeWidth(10);
        mPaintRec.setAntiAlias(true);
        mPaintRec.setStyle(Paint.Style.FILL);
        mPaintRec.setColor(context.getResources().getColor(R.color.color_f733d6));
        mPaintText=new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.STROKE);
        mPaintText.setTextSize(40);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setColor(context.getResources().getColor(R.color.color_f733d6));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth= w;
        mHeight=h;
        radio=mHeight/2;
    }

    public void setHeadText(String headText) {
        mHeadText = headText;
    }

    public void setBackText(String backText) {
        mBackText = backText;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), icon);
        invalidate();
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap!=null){
            canvas.translate(mWidth/2,mHeight/2+10);
            canvas.save();
            RectF rectF=new RectF(-mHeight/2,-mHeight/2+20,mHeight/2,mHeight/2+20);
            canvas.drawArc(rectF,-180,180,false,mPaint);
            canvas.drawText(mHeadText,-mHeight/2,60,mPaintText);
            canvas.drawText(mBackText,mHeight/2,60,mPaintText);
            float x = (float)(radio*Math.cos((180-progress*180)*Math.PI/180));
            float y = -(float)(radio*Math.sin((180-progress*180)*Math.PI/180));
            canvas.drawBitmap(mBitmap,x-mBitmap.getWidth()/2,y-mBitmap.getHeight()/2,mPaintRec);
        }
    }

    public void doAnimation(){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0,(float) progress);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                progress=animatedValue;
                invalidate();
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();

    }
}
