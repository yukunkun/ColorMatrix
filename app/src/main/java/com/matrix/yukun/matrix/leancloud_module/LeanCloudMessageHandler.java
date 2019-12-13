package com.matrix.yukun.matrix.leancloud_module;

import java.util.List;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMConversationEventHandler;
/**
 * author: kun .
 * date:   On 2019/12/12
 */
public class LeanCloudMessageHandler extends AVIMConversationEventHandler {

    @Override
    public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {

    }

    @Override
    public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {

    }

    @Override
    public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {

    }

    @Override
    public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {

    }

    @Override
    public void onUnreadMessagesCountUpdated(AVIMClient client, AVIMConversation conversation) {

    }
}
