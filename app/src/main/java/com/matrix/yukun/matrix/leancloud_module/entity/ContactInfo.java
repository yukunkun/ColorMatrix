package com.matrix.yukun.matrix.leancloud_module.entity;

import java.io.Serializable;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/12/12
 */
public class ContactInfo implements Serializable {
    private String conversationId;
    private List<String> members; // 成员
    private String creator;     // 创建者
    boolean isTransient; // 是否为临时对话
    private String lastMessage;
    private String lastTime;
    private String fromAvator;
    private String toAvator;
    private String from;
    private String to;
    private String fromUserName;
    private String toUserName;
    private boolean isSyncLastMessage = false;
    /**
     * 未读消息数量
     */
    int unreadMessagesCount = 0;
    boolean unreadMessagesMentioned = false;
    /**
     * 对方最后收到消息的时间，此处仅针对双人会话有效
     */
    long lastDeliveredAt;
    /**
     * 对方最后读消息的时间，此处仅针对双人会话有效
     */
    long lastReadAt;
    /**
     * 是否是临时对话
     */
    boolean isTemporary = false;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getFromAvator() {
        return fromAvator;
    }

    public void setFromAvator(String fromAvator) {
        this.fromAvator = fromAvator;
    }

    public String getToAvator() {
        return toAvator;
    }

    public void setToAvator(String toAvator) {
        this.toAvator = toAvator;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean aTransient) {
        isTransient = aTransient;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isSyncLastMessage() {
        return isSyncLastMessage;
    }

    public void setSyncLastMessage(boolean syncLastMessage) {
        isSyncLastMessage = syncLastMessage;
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public boolean isUnreadMessagesMentioned() {
        return unreadMessagesMentioned;
    }

    public void setUnreadMessagesMentioned(boolean unreadMessagesMentioned) {
        this.unreadMessagesMentioned = unreadMessagesMentioned;
    }

    public long getLastDeliveredAt() {
        return lastDeliveredAt;
    }

    public void setLastDeliveredAt(long lastDeliveredAt) {
        this.lastDeliveredAt = lastDeliveredAt;
    }

    public long getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(long lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public void setTemporary(boolean temporary) {
        isTemporary = temporary;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "conversationId='" + conversationId +
                ", from='" + from +
                ", to='" + to +
                ", fromUserName='" + fromUserName +
                ", toUserName='" + toUserName +
                ", fromAvator='" + fromAvator +
                ", toAvator=" + toAvator +
                ", members=" + members +
                ", creator='" + creator +
                ", isTransient=" + isTransient +
                ", lastMessage=" + lastMessage +
                ", lastTime='" + lastTime +
                ", isSyncLastMessage=" + isSyncLastMessage +
                ", unreadMessagesCount=" + unreadMessagesCount +
                ", unreadMessagesMentioned=" + unreadMessagesMentioned +
                ", lastDeliveredAt=" + lastDeliveredAt +
                ", lastReadAt=" + lastReadAt +
                ", isTemporary=" + isTemporary +
                '}';
    }
}
