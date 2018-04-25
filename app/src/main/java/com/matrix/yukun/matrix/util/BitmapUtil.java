package com.matrix.yukun.matrix.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by yukun on 17-1-25.
 */
public class BitmapUtil {
    //返回宽高
    public static Bitmap mTempBit(Bitmap bitmap){
        return  Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_4444);
    }

    //旋转
    public static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha*90);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
        if (newBM.equals(origin)) {
            return newBM;
        }
        return newBM;
    }
    //色度的调节
    public static Bitmap handleColorBmp(Bitmap mTempBmp,Bitmap mBitSeek,int progress,int rotate){
        Bitmap bitmap;
        // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(mTempBmp); // 得到画笔对象
        Paint paint = new Paint(); // 新建paint
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setRotate(rotate,progress-100);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));// 设置颜色变换效果
        canvas.drawBitmap(mBitSeek, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
        // 返回新的位图，也即调色处理后的图片
        return mTempBmp;
    }
    //亮度的调节
    public static Bitmap handleColorMatrixBmp(Bitmap mTempBmp,Bitmap mBitSeek,int progress){
        // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(mTempBmp); // 得到画笔对象
        Paint paint = new Paint(); // 新建paint
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理
        ColorMatrix mSaturationMatrix = new ColorMatrix();
        mSaturationMatrix.setSaturation(progress);
        paint.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));// 设置颜色变换效果
        canvas.drawBitmap(mBitSeek, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
        return mTempBmp;
    }
    //从新绘制
    public static Bitmap handleColorRotateBmp(ColorMatrix colorMatrix,Bitmap mOriginBmp,Bitmap bitmaps){
        // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(bitmaps); // 得到画笔对象
        Paint paint = new Paint(); // 新建paint
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        // 返回新的位图，也即调色处理后的图片
        canvas.drawBitmap(mOriginBmp, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
        return bitmaps;
    }

    public static ColorMatrix matrixTrans(int position){
        ArrayList<ColorMatrix> colorMatrices = MatrixArray.getColorMatrices();
        return colorMatrices.get(position);
    }

    public static Bitmap bigBitmap(Bitmap b,float x,float y) {
        int w=b.getWidth();
        int h=b.getHeight();
        float sx=(float)x/w;//要强制转换，不转换我的在这总是死掉。
        float sy=(float)y/h;
        Matrix matrix = new Matrix();
        matrix.postScale(x, y); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }

}
