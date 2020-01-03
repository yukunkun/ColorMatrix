package com.matrix.yukun.matrix.leancloud_module.impl;

import com.matrix.yukun.matrix.leancloud_module.MessageManager;
import com.matrix.yukun.matrix.util.log.LogUtil;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMMessage;

/**
 * author: kun .
 * date:   On 2020/1/3
 */
public interface UpdateMessageListener {

    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client);

    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client);
}
