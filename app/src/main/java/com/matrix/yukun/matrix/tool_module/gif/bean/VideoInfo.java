package com.matrix.yukun.matrix.tool_module.gif.bean;

import android.graphics.Bitmap;

/**
 * author: kun .
 * date:   On 2019/2/18
 */
public class VideoInfo {
    private Bitmap mBitmap;
    private long timeMs;
    private boolean isChooseStart;
    private boolean isChooseEnd;
    private boolean isChooseCenter;
    public VideoInfo( int timeMs,Bitmap bitmap) {
        mBitmap = bitmap;
        this.timeMs = timeMs;
    }


    public boolean isChooseCenter() {
        return isChooseCenter;
    }

    public void setChooseCenter(boolean chooseCenter) {
        isChooseCenter = chooseCenter;
    }

    public boolean isChooseStart() {
        return isChooseStart;
    }

    public void setChooseStart(boolean chooseStart) {
        isChooseStart = chooseStart;
    }

    public boolean isChooseEnd() {
        return isChooseEnd;
    }

    public void setChooseEnd(boolean chooseEnd) {
        isChooseEnd = chooseEnd;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }
}
