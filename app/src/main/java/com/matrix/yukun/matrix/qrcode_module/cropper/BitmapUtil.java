package com.matrix.yukun.matrix.qrcode_module.cropper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.FileInputStream;

public class BitmapUtil {

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * ���ر���ͼƬ
     *
     * @return
     */
    public static Bitmap getBitmapFromSDCard(String path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            if (fis != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                return BitmapFactory.decodeStream(fis, null, options);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static Bitmap temp;

    // ͼƬ����
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
}
