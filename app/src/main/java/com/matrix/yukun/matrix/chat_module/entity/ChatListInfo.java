package com.matrix.yukun.matrix.chat_module.entity;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ChatListInfo extends DataSupport {

    private String mChatInfo;
    private int type;
    private int typeSn;
    private Bitmap mBitmap;
    private long msgTime;
    private String msgType;
    private boolean isReceive;
    private long timeStamp;    //时间戳
    private boolean isShowTime;
    private String imagePath;
    private String videoPath;
    private String filePath;
    private boolean audioIsPlay;
    private long duration;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isShowTime() {
        return isShowTime;
    }

    public void setShowTime(boolean showTime) {
        isShowTime = showTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public void setReceive(boolean receive) {
        isReceive = receive;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public int getTypeSn() {
        return typeSn;
    }

    public void setTypeSn(int typeSn) {
        this.typeSn = typeSn;
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

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isAudioIsPlay() {
        return audioIsPlay;
    }

    public void setAudioIsPlay(boolean audioIsPlay) {
        this.audioIsPlay = audioIsPlay;
    }

    @Override
    public String toString() {
        return "ChatListInfo{" +
                "mChatInfo='" + mChatInfo + '\'' +
                ", type=" + type +
                ", typeSn=" + typeSn +
                ", mBitmap=" + mBitmap +
                ", msgTime=" + msgTime +
                ", msgType='" + msgType + '\'' +
                ", isReceive=" + isReceive +
                ", timeStamp=" + timeStamp +
                ", isShowTime=" + isShowTime +
                ", imagePath='" + imagePath + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", filePath='" + filePath + '\'' +
                ", audioIsPlay='" + audioIsPlay + '\'' +
                '}';
    }
}
