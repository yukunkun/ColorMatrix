package com.matrix.yukun.matrix.leshilive_module.giftview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by yukun on 17-3-14.
 */
public class StrokeTextView extends TextView {
    Paint m_TextPaint;
    public StrokeTextView(Context context) {
        super(context);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        m_TextPaint=new Paint();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (m_bDrawSideLine) {
            // 描外层
            setTextColorUseReflection(Color.BLUE);
            m_TextPaint.setStrokeWidth(2);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            super.onDraw(canvas);

            // 描内层，恢复原先的画笔
            setTextColorUseReflection(Color.GREEN);
            m_TextPaint.setStrokeWidth(0);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

//        }
        super.onDraw(canvas);
    }
    /**
     * 使用反射的方法进行字体颜色的设置
     * @param color
     */
    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }
}
