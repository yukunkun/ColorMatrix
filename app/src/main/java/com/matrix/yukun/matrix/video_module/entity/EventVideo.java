package com.matrix.yukun.matrix.video_module.entity;

/**
 * Created by yukun on 18-1-5.
 */

public class EventVideo {
    public EyesInfo mEyesInfo;
    public int type;
    public String mNextUrl;

    public EventVideo(EyesInfo eyesInfo, int type, String nextUrl) {
        mEyesInfo = eyesInfo;
        this.type = type;
        mNextUrl = nextUrl;
    }
    public EventVideo() {

    }
}
