package com.ykk.pluglin_video.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.utils.DensityUtil;
import com.ykk.pluglin_video.utils.ISideBarSelectCallBack;


/**
 * Created by Allen Liu on 2016/5/12.
 */
public class SideBar extends TextView {
    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint textPaint;
    private Paint bigTextPaint;
    private Paint scaleTextPaint;
    private Canvas canvas;
    private int itemH;
    private int w;
    private int h;
    public static int STYLEWAVE=0;
    public static int STYLENOWAVE=1;
    public static int STYLENORMAL=2;
    private int style=0;
    /**
     * 普通情况下字体大小
     */
    float singleTextH;
    /**
     * 缩放离原始的宽度
     */
    private float scaleWidth = dp(100);
    /**
     * 滑动的Y
     */
    private float eventY = 0;
    /**
     * 缩放的倍数
     */
    private int scaleTime = 1;
    /**
     * 缩放个数item，即开口大小
     */
    private int scaleItemCount = 6;
    private ISideBarSelectCallBack callBack;

    public SideBar(Context context) {
        super(context);
        init(null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SideBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    public void setDataResource(String[] data){
        letters=data;
        invalidate();
    }
   public void setOnStrSelectCallBack(ISideBarSelectCallBack callBack){
       this.callBack=callBack;
   }
    /**
     * 设置字体缩放比例
     * @param scale
     */
    public void setScaleTime(int scale){
        scaleTime=scale;
        invalidate();
    }

    /**
     * 设置缩放字体的个数，即开口大小
     * @param scaleItemCount
     */
    public  void setScaleItemCount(int scaleItemCount){
        this.scaleItemCount=scaleItemCount;
        invalidate();
    }

    /**
     * 设置样式
     * @param style
     */
    public void setStyle(int style){
        this.style=style;
    }

    private void init(AttributeSet attrs) {
      //  setPadding(dp(10), 0, dp(10), 0);
        if(attrs!=null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SideBar);
           scaleTime= typedArray.getInteger(R.styleable.SideBar_scaleTime,1);
            scaleItemCount=typedArray.getInteger(R.styleable.SideBar_scaleItemCount,6);
        }
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);
        bigTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigTextPaint.setColor(getCurrentTextColor());
        bigTextPaint.setTextSize(getTextSize()+getTextSize() * (scaleTime+2) );
        bigTextPaint.setTextAlign(Paint.Align.CENTER);
        scaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scaleTextPaint.setColor(getCurrentTextColor());
        scaleTextPaint.setTextSize(getTextSize() * (scaleTime + 1));
        scaleTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private int dp(int px) {
        return DensityUtil.dip2px(getContext(), px);
    }

    //获取到y的范围
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                //保证在文字上才获取y
                if(event.getX()>(w-getPaddingRight()-singleTextH-10)) {
                    eventY = event.getY();
                    invalidate();
                    return true;
                }else{
                    eventY = 0;
                    invalidate();
                    break;
                }
            case MotionEvent.ACTION_CANCEL:
                //只有normal才会回调
                if(style==2){
                    //离开的回调
                    callBack.onSelectEnd();
                }
                eventY = 0;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                //只有normal才会回调
                if(style==2){
                    //离开的回调
                    callBack.onSelectEnd();
                }
                //滑动离开文字
                if(event.getX()>(w-getPaddingRight()-singleTextH-10)) {
                    eventY = 0;
                    invalidate();
                    return true;
                }else
                    break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        DrawView(eventY);
    }

    //更具y来实现绘制，即点击在文字上
    private void DrawView(float y) {
        int currentSelectIndex = -1;
        //有触摸才绘制大文字
        if (y != 0) {
            for (int i = 0; i < letters.length; i++) {
                //当前的高度
                float currentItemY = itemH * i;
                //下一个的高度
                float nextItemY = itemH * (i + 1);
                //判断位置在点中的字母间
                if (y >= currentItemY && y < nextItemY) {

                    currentSelectIndex = i;
                    if(callBack!=null){
                        callBack.onSelectStr(currentSelectIndex,letters[i]);
                    }
                    //画大的字母
                    Paint.FontMetrics fontMetrics = bigTextPaint.getFontMetrics();
                    //文字绘制，有基线的区别，获取到文字的高度
                    float bigTextSize = fontMetrics.descent - fontMetrics.ascent;
                    //判断类型
                    if(style==0||style==1){
                        //绘制字母，大文字
                        canvas.drawText(letters[i], w - getPaddingRight() - scaleWidth - bigTextSize, singleTextH + itemH * i, bigTextPaint);
                    }
                    //２才会回调
                    if(style==2){
                        //选中的回调
                        callBack.onSelectStart();
                    }
                }
            }
        }
        //其他的绘制
        drawLetters(y, currentSelectIndex);
    }

    private void drawLetters(float y, int index) {
        //第一次进来没有缩放情况，默认画原图
        if (index == -1) {
            w = getMeasuredWidth();
            h = getMeasuredHeight();
            //每一个字母的高度
            itemH = h / letters.length;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            //文字绘制，有基线的区别，获取到文字的高度
            singleTextH = fontMetrics.descent - fontMetrics.ascent;
            //绘制字母，每个item
            for (int i = 0; i < letters.length; i++) {
                canvas.drawText(letters[i], w - getPaddingRight(), singleTextH + itemH * i, textPaint);
            }
            //触摸的时候画缩放图
        } else {
            //遍历所有字母
            for (int i = 0; i < letters.length; i++) {
                //要画的字母的起始Y坐标
                float currentItemToDrawY = singleTextH + itemH * i;
                float centerItemToDrawY;
                if (index < i)
                    centerItemToDrawY = singleTextH + itemH * (index + scaleItemCount);
                else
                    centerItemToDrawY = singleTextH + itemH * (index - scaleItemCount);
                //最麻烦的计算,距离当前点中的字母的距离远，则比例越小，距离x越小 (delta在字母移动范围内为0－1)
                float delta = 1 - Math.abs((y - currentItemToDrawY) / (centerItemToDrawY - currentItemToDrawY));
//                Log.i("size", letters[i] + "--->" + delta + "");
                float maxRightX = w - getPaddingRight();
                //如果大于0，表明在y坐标上方
                scaleTextPaint.setTextSize(getTextSize() + getTextSize() * delta);
//                Log.i("scaleTextPaint_size",getTextSize() + getTextSize() * delta+"");
                float drawX = maxRightX - scaleWidth * delta;
                //超出边界直接花在边界上
               if (style==0){//波浪形状
                    if (drawX > maxRightX) {
                        //画边上的字母
                        canvas.drawText(letters[i], maxRightX, singleTextH + itemH * i, textPaint);
                    }else {
                        //画弧线字母
                        canvas.drawText(letters[i], drawX, singleTextH + itemH * i, scaleTextPaint);
                    }
                    //没有波浪
                }else {
                    canvas.drawText(letters[i], maxRightX, singleTextH + itemH * i, textPaint);
                }
                //抛物线实现，没有动画效果，太生硬了
//                    canvas.save();
//                    canvas.translate(w-getPaddingRight(),0);
//                    double y1 = singleTextH + itemH * (index - scaleItemCount);
//                    double y2 = singleTextH + itemH * (index + scaleItemCount);
//                    double topY = y;
//                    double topX = -scaleWidth;
//                    double p = topX / ((topY - y1) * (topY - y2));
//                    for (int j = 1; j <= scaleItemCount; j++) {
//                        double currentY=singleTextH + itemH * i;
//                        canvas.drawText(letters[i], (float) (p * (currentY - y1) * (currentY - y2)), singleTextH + itemH * i, scaleTextPaint);
//                    }
//                    canvas.restore();
//                     }
            }
        }
    }
}
