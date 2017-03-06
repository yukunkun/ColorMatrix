package com.matrix.yukun.matrix.selfview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.ScreenUtils;

/**
 * Created by yukun on 17-1-19.
 */
public class BitmapView extends ImageView {
    private Paint mPaint = new Paint();
    private Bitmap bitmap;// 位图
    private Context context;
    private Rect mDestRect;
    private Rect mSrcRect;
    private Bitmap mOriginBmp;

    public BitmapView(Context context) {
        super(context);
        mOriginBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
    }

    public BitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BitmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int width = 0;
//        int height = 0;
//        mOriginBmp = BitmapFactory.decodeResource(getResources(), R.drawable.hot01);
//
//        /**
//         * 设置宽度
//         */
//        int specMode = MeasureSpec.getMode(widthMeasureSpec);
//        int specSize = MeasureSpec.getSize(widthMeasureSpec);
//        switch (specMode) {
//            case MeasureSpec.EXACTLY:// 明确指定了
//                width = getPaddingLeft() + getPaddingRight() + specSize;
//                break;
//            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
//                width = getPaddingLeft() + getPaddingRight() + bitmap.getWidth();
//                break;
//        }
//
//        /**
//         * 设置高度
//         */
//        specMode = MeasureSpec.getMode(heightMeasureSpec);
//        specSize = MeasureSpec.getSize(heightMeasureSpec);
//        switch (specMode) {
//            case MeasureSpec.EXACTLY:// 明确指定了
//                height = getPaddingTop() + getPaddingBottom() + specSize;
//                break;
//            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
//                height = getPaddingTop() + getPaddingBottom() + bitmap.getHeight();
//                break;
//        }
//
//        setMeasuredDimension(width, height);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap!=null){

            int width = ScreenUtils.instance().getWidth(context);
            int height=320*2;

            if(bitmap.getHeight()/bitmap.getWidth()>width/height){

                mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDestRect = new Rect(0, 0, getMeasuredWidth()-(getMeasuredWidth()/2-bitmap.getWidth()/2), 320*2);

            }else {
                mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDestRect = new Rect(0, 0, width, bitmap.getHeight());
            }

            // 生成色彩矩阵
            ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                    1, 0, 0, 0, 0,
                    0, 1, 0, 0, 0,
                    0, 0, 1, 0, 0,
                    0, 0, 0, 1, 0,
            });
            mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mPaint);
//            canvas.drawBitmap(mOriginBmp,0,0,mPaint);
        }
    }

    public void setBitmap(Bitmap bitmap,Context context){
        this.bitmap=bitmap;
        this.context=context;
        invalidate();
    }
}
