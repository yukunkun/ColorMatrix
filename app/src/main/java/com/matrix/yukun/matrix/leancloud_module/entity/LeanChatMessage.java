package com.matrix.yukun.matrix.leancloud_module.entity;

import org.litepal.crud.DataSupport;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author: kun .
 * date:   On 2019/12/26
 */
public class LeanChatMessage extends DataSupport implements MultiItemEntity {
    private String msgId;
    private String content;
    private String imageUrl;
    private String msgFrom;
    private String msgTo;
    private String msgFromUserName;
    private String msgToUserName;
    private String msgFromAvator;
    private String msgToAvator;
    private int type;
    private long timeStamp;
    private boolean isReceived;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgFromUserName() {
        return msgFromUserName;
    }

    public void setMsgFromUserName(String msgFromUserName) {
        this.msgFromUserName = msgFromUserName;
    }

    public String getMsgToUserName() {
        return msgToUserName;
    }

    public void setMsgToUserName(String msgToUserName) {
        this.msgToUserName = msgToUserName;
    }

    public String getMsgFromAvator() {
        return msgFromAvator;
    }

    public void setMsgFromAvator(String msgFromAvator) {
        this.msgFromAvator = msgFromAvator;
    }

    public String getMsgtoAvator() {
        return msgToAvator;
    }

    public void setMsgtoAvator(String msgToAvator) {
        this.msgToAvator = msgToAvator;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getMsgToAvator() {
        return msgToAvator;
    }

    public void setMsgToAvator(String msgToAvator) {
        this.msgToAvator = msgToAvator;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean receiver) {
        isReceived = receiver;
    }

    @Override
    public String toString() {
        return "LeanChatMessage{" +
                "msgId='" + msgId +
                ", content='" + content +
                ", imageUrl='" + imageUrl +
                ", msgFrom='" + msgFrom +
                ", msgTo='" + msgTo +
                ", msgFromUserName='" + msgFromUserName +
                ", msgToUserName='" + msgToUserName +
                ", msgFromAvator='" + msgFromAvator +
                ", msgToAvator='" + msgToAvator +
                ", type=" + type +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
