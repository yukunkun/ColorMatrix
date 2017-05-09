package com.matrix.yukun.matrix.util;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yukun on 17-5-9.
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

//        sortSizes(sizes);

        Iterator<Camera.Size> it = sizes.iterator();
        while (it.hasNext()) {
            Camera.Size size = it.next();
            //移除不满足比例的尺寸
            if ((float) size.height / size.width <= minRatio) {
                it.remove();
                continue;
            }
            //移除太小的尺寸
            if (size.width * size.height < MIN_PIXELS) {
                it.remove();
            }
        }

        // 返回符合条件中最大尺寸的一个
        if (!sizes.isEmpty()) {
            return sizes.get(0);
        }
        // 没得选，默认吧
        return defaultSize;
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

        Iterator<Camera.Size> it = sizes.iterator();
        while (it.hasNext()) {
            Camera.Size size = it.next();
            if ((float) size.height / size.width <= minRatio) {
                it.remove();
                continue;
            }

            // 找到同样的比例，直接返回
            if (isBestSize && size.width * pictureHeight == size.height * pictureWidth) {
                return size;
            }
        }

        // 未找到同样的比例的，返回尺寸最大的
        if (!sizes.isEmpty()) {
            return sizes.get(0);
        }

        // 没得选，默认吧
        return defaultSize;
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

}
