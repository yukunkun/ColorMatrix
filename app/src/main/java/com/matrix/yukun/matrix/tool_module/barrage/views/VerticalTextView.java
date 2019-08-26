package com.matrix.yukun.matrix.tool_module.barrage.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;

/**
 * author: kun .
 * date:   On 2018/11/19
 */
public class VerticalTextView extends View{
    private Paint mPaint;
    private int width=300;
    private int color= Color.BLUE;
    private String text="发射爱心❤弹幕";
    private Rect mRect;
    Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private int vTextViewLength;
    private int movePos;
    private int speed=20;
    private int textWidth=20;
    Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if(movePos<-vTextViewLength){
                        movePos=mScreenHeight;
                    }else {
                        movePos=movePos-speed;
                    }
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(1,40);
                    break;
            }
        }
    };

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setFont(Typeface font) {
        this.font = font;
        invalidate();
    }

    public void startAnimation(){
        mHandler.sendEmptyMessage(1);
    }

    public void stopAnimation(){
        if(mHandler!=null){
            Log.i("------>","stop");
            mHandler.removeMessages(1);
        }
    }


    public void setWidth(int width) {
        this.width = width*10;
        invalidate();
    }

    public int getvTextViewLength() {
        return vTextViewLength;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public VerticalTextView(Context context) {
        super(context);
        init(context,null,0);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext=context;
        mScreenWidth = ScreenUtils.instance().getWidth(mContext);
        mScreenHeight = ScreenUtils.instance().getHeight(mContext);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mRect = new Rect();
        mPaint.setTextSize(width);
        mPaint.getTextBounds(text, movePos, text.length(), mRect);
        vTextViewLength=mRect.width();
        movePos=mScreenHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(textWidth);
        mPaint.setColor(color);
        mPaint.setTextSize(width);
        mPaint.setTypeface(font);
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int h = rect.height();
        vTextViewLength=rect.width();
        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
//        float baseline=/*distance+*/h;
        Matrix matrix = new Matrix();
        matrix.setRotate(90,0,0);
        canvas.setMatrix(matrix);
        canvas.drawText(text, movePos, -mScreenWidth/2+h/3, mPaint);
    }

}
