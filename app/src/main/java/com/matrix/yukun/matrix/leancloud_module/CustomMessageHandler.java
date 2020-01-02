package com.matrix.yukun.matrix.leancloud_module;

import com.matrix.yukun.matrix.util.log.LogUtil;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMMessage;
import cn.leancloud.im.v2.AVIMMessageHandler;

/**
 * author: kun .
 * date:   On 2020/1/2
 */
public class CustomMessageHandler extends AVIMMessageHandler {
    private String TAG= CustomMessageHandler.class.getName();
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        LogUtil.i(TAG,"onMessage:"+message.toJSONString());
        MessageManager.getInstance().onMessage(message,conversation,client);

    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        LogUtil.i(TAG,"onMessageReceipt:"+message.toJSONString());
        MessageManager.getInstance().onMessageReceipt(message,conversation,client);
    }
}
