package com.matrix.yukun.matrix.leancloud_module;

import com.matrix.yukun.matrix.leancloud_module.impl.UpdateMessageListener;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMMessage;
import cn.leancloud.im.v2.AVIMMessageHandler;

/**
 * author: kun .
 * date:   On 2020/1/2
 */
public class CustomMessageHandler extends AVIMMessageHandler {
    private String TAG = CustomMessageHandler.class.getName();
    private static CustomMessageHandler instance=new CustomMessageHandler();
    private static List<UpdateMessageListener> mMessageListeners = new ArrayList<>();

    public CustomMessageHandler() {

    }

    public static CustomMessageHandler getInstance() {
        return instance;
    }

    public void addUpdateMessageListener(UpdateMessageListener listener) {
        if (!mMessageListeners.contains(listener)) {
            mMessageListeners.add(listener);
        }
    }

    public void removeUpdateMessageListener(UpdateMessageListener listener) {
        if (!mMessageListeners.isEmpty()) {
            mMessageListeners.remove(listener);
        }
    }

    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        LogUtil.i(TAG, "onMessage:" + message.toJSONString());
        MessageManager.getInstance().onMessage(message, conversation, client);
        for (int i = 0; i < mMessageListeners.size(); i++) {
            mMessageListeners.get(i).onMessage(message, conversation, client);
        }
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        LogUtil.i(TAG, "onMessageReceipt:" + message.toJSONString());
        MessageManager.getInstance().onMessageReceipt(message, conversation, client);
        for (int i = 0; i < mMessageListeners.size(); i++) {
            mMessageListeners.get(i).onMessageReceipt(message, conversation, client);
        }
    }
}
