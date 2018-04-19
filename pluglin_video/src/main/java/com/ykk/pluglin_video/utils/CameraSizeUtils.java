package com.ykk.pluglin_video.utils;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yukun on 17-9-28.
 */

public class CameraSizeUtils {
    /**
     * 找到短边比长边大于于所接受的最小比例的最大尺寸
     *
     * @param sizes       支持的尺寸列表
     * @param defaultSize 默认大小
     * @param minRatio    相机图片短边比长边所接受的最小比例
     * @return 返回计算之后的尺寸
     */
    public static Camera.Size findBestPictureSize(List<Camera.Size> sizes, Camera.Size defaultSize, float minRatio) {
        final int MIN_PIXELS = 320 * 480;
        float minRatios=5f;
        int i = 0;
//        sortSizes(sizes);
        Iterator<Camera.Size> it = sizes.iterator();
        while (it.hasNext()) {
            Camera.Size size = it.next();
            Log.i("size1: ", size.width + " " + size.height);
            float abs = Math.abs((float) size.width / size.height - minRatio);
            if (abs < minRatios) {
                minRatios = abs;
                i++;
            }
        }
        return sizes.get(i-1);
//            //移除不满足比例的尺寸
//            if ((float) size.height / size.width <= minRatio) {
//                it.remove();
//                continue;
//            }
//            //移除太小的尺寸
//            if (size.width * size.height < MIN_PIXELS) {
//                it.remove();
//            }
//        }
//
//        // 返回符合条件中最大尺寸的一个
//        if (!sizes.isEmpty()) {
////            Log.i("size: ", sizes.get(sizes.size()-1).width+" "+sizes.get(sizes.size()-1).height);
////            return sizes.get(0);
//            return sizes.get(sizes.size()-1);
//        }
        // 没得选，默认吧
//        return defaultSize;
    }
    /**
     * @param sizes
     * @param defaultSize
     * @param pictureSize 图片的大小
     * @param minRatio preview短边比长边所接受的最小比例
     * @return
     */
    public static Camera.Size findBestPreviewSize(List<Camera.Size> sizes, Camera.Size defaultSize,
                                                  Camera.Size pictureSize, float minRatio) {
        final int pictureWidth = pictureSize.width;
        final int pictureHeight = pictureSize.height;
        boolean isBestSize = (pictureHeight / (float)pictureWidth) > minRatio;
//        sortSizes(sizes);

//        Iterator<Camera.Size> it = sizes.iterator();
//        while (it.hasNext()) {
//            Camera.Size size = it.next();
//            if ((float) size.height / size.width <= minRatio) {
//                it.remove();
//                continue;
//            }
//
//            // 找到同样的比例，直接返回
//            if (isBestSize && size.width * pictureHeight == size.height * pictureWidth) {
//                return size;
//            }
//        }

        // 未找到同样的比例的，返回尺寸最大的
        if (!sizes.isEmpty()) {
//            return sizes.get(0);
            return sizes.get(sizes.size()-1);
        }

        Log.i("size1OPer: ", sizes.get(sizes.size()-1).width+" "+sizes.get(sizes.size()-1).height);
        // 没得选，默认吧
        return defaultSize;
    }


    public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            Log.i("size: ", size.width+" "+size.height);
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        Log.i("sizeOPer: ", optimalSize.width+" "+optimalSize.height);
        return optimalSize;
    }


    public static byte[] compressBitmap(Bitmap bitmap, float size) {
        if (bitmap == null) {
            return null;//如果图片本身的大小已经小于这个大小了，就没必要进行压缩
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);//如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        int quality = 100;
        while (baos.toByteArray().length / 1024f > size) {
            quality = quality - 4;// 每次都减少4
            baos.reset();// 重置baos即清空baos
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            Log.i("------质量--------", "" + baos.toByteArray().length / 1024f);
        }
        return baos.toByteArray();
    }


    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

}
