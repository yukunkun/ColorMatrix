package com.matrix.yukun.matrix.leancloud_module.utils;

import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMMessage;
import cn.leancloud.im.v2.messages.AVIMAudioMessage;
import cn.leancloud.im.v2.messages.AVIMImageMessage;
import cn.leancloud.im.v2.messages.AVIMLocationMessage;
import cn.leancloud.im.v2.messages.AVIMTextMessage;

/**
 * author: kun .
 * date:   On 2019/12/13
 */
public class MessageWrapper {

    public  static MessageWrapper mMessageWrapper = new MessageWrapper();

    public MessageWrapper() {
    }

    public static MessageWrapper getInstance() {
        return mMessageWrapper;
    }

    public List<ContactInfo> covertToContactInfo(List<AVIMConversation> avimConversations) {
        List<ContactInfo> infos = new ArrayList<>();
        for (int i = 0; i < avimConversations.size(); i++) {
            ContactInfo contactInfo = new ContactInfo();
            AVIMConversation avimConversation = avimConversations.get(i);
            contactInfo.setAvator((String) avimConversation.getAttribute("avator"));
            contactInfo.setTo((String) avimConversation.getAttribute("to"));
            contactInfo.setFrom((String) avimConversation.getAttribute("from"));
            contactInfo.setUserId((String) avimConversation.getAttribute("userId"));
            contactInfo.setConversationId(avimConversation.getConversationId());
            contactInfo.setCreator(avimConversation.getCreator());
            contactInfo.setLastReadAt(avimConversation.getLastReadAt());
            contactInfo.setLastDeliveredAt(avimConversation.getLastDeliveredAt());
            contactInfo.setMembers(avimConversation.getMembers());
            contactInfo.setUnreadMessagesCount(avimConversation.getUnreadMessagesCount());
            contactInfo.setLastTime(String.valueOf((avimConversation.getLastMessageAt()!=null?avimConversation.getLastMessageAt().getTime():System.currentTimeMillis())));
            contactInfo.setLastMessage(wrapperLastMessage(avimConversation));
            infos.add(contactInfo);
        }
        return infos;
    }

    private String wrapperLastMessage(AVIMConversation avimConversation) {
        String lastMessageContent;
        AVIMMessage lastMessage = avimConversation.getLastMessage();
        if (lastMessage instanceof AVIMTextMessage) {
            lastMessageContent = ((AVIMTextMessage) lastMessage).getText();
        } else if (lastMessage instanceof AVIMImageMessage) {
            lastMessageContent = "图片";
        } else if (lastMessage instanceof AVIMAudioMessage) {
            lastMessageContent = "语音";
        } else if (lastMessage instanceof AVIMLocationMessage) {
            lastMessageContent = "位置";
        } else {
            lastMessageContent = "未知";
        }
        return lastMessageContent;
    }


}
