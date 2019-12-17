package com.matrix.yukun.matrix.leancloud_module;

import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.List;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMConversationEventHandler;
import cn.leancloud.im.v2.AVIMMessage;

/**
 * author: kun .
 * date:   On 2019/12/12
 */
public class LeanCloudMessageHandler extends AVIMConversationEventHandler {

    @Override
    public void onMessageUpdated(AVIMClient client, AVIMConversation conversation, AVIMMessage message) {
        LogUtil.i("onMessageUpdated -->");
    }

    @Override
    public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {
        LogUtil.i("onMemberLeft -->");
    }

    @Override
    public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {
        LogUtil.i("onMemberJoined -->");
    }

    @Override
    public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {
        LogUtil.i("onKicked -->");
    }

    @Override
    public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {
        LogUtil.i("onInvited -->");
    }

    @Override
    public void onUnreadMessagesCountUpdated(AVIMClient client, AVIMConversation conversation) {
        LogUtil.i("onUnreadMessagesCountUpdated -->",conversation.getUnreadMessagesCount()+"");
    }
}
