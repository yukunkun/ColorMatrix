package com.ykk.pluglin_video.entity;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ChatListInfo {
    private String mChatInfo;
    private int type;
    private Bitmap mBitmap;
    private boolean isShowTime;
    private double msgTime;

    public double getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(double msgTime) {
        this.msgTime = msgTime;
    }

    public void setIsShowTime(boolean isShowTime){
        this.isShowTime=isShowTime;
    }

    public boolean getIsShowTime(){
        return isShowTime;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getChatInfo() {
        return mChatInfo;
    }

    public void setChatInfo(String chatInfo) {
        mChatInfo = chatInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
