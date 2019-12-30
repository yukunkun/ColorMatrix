package com.matrix.yukun.matrix.leancloud_module;

import android.os.Environment;

import com.google.protobuf.Extension;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.chat_module.entity.ChatType;
import com.matrix.yukun.matrix.leancloud_module.common.LeanConatant;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.AVFile;
import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.callback.AVIMConversationCallback;
import cn.leancloud.im.v2.callback.AVIMConversationCreatedCallback;
import cn.leancloud.im.v2.messages.AVIMImageMessage;
import cn.leancloud.im.v2.messages.AVIMTextMessage;

/**
 * author: kun .
 * date:   On 2019/12/30
 */
public class MessageManager {

    private static MessageManager manager = new MessageManager();
    private AVIMClient mAvimClient;
    private AVIMConversation mConversation;

    public MessageManager() {
        mAvimClient = AVIMClient.getInstance(MyApp.getUserInfo().getId());
    }

    public static MessageManager getInstance() {
        return manager;
    }

    /**
     * 单聊
     *
     * @param toId
     */
    public void createConversion(String toId) {
        mAvimClient.createConversation(Arrays.asList(toId), "", getAttr(toId), false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVIMException e) {
                        if (e == null) {
                            mConversation = conversation;
                        } else {
                            ToastUtils.showToast("创建会话失败");
                        }
                    }
                }
        );
    }

    /**
     * 群会话
     *
     * @param members
     * @param toId
     */
    public void createConversion(List<String> members, String toId) {
        mAvimClient.createConversation(members, "", getAttr(toId), false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation conversation, AVIMException e) {
                        if (e == null) {
                            mConversation = conversation;
                        } else {
                            ToastUtils.showToast("创建会话失败");
                        }
                    }
                }
        );
    }

    public void sendTxtMessage(String txt) {
        AVIMTextMessage avimTextMessage = new AVIMTextMessage();
        avimTextMessage.setText(txt);
        mConversation.sendMessage(avimTextMessage, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {

                } else {
                    ToastUtils.showToast("发送消息失败");
                }
            }
        });
    }

    public void sendImageMessage(String imagePath) {
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath("San_Francisco.png", Environment.getExternalStorageDirectory() + "/San_Francisco.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AVIMImageMessage avimImageMessage = new AVIMImageMessage(file);
        mConversation.sendMessage(avimImageMessage, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {

                } else {
                    ToastUtils.showToast("发送消息失败");
                }
            }
        });
    }

    public void sendImageMessage(String imageName, String ImageUrl) {
        AVFile file = new AVFile(imageName, ImageUrl, null);
        AVIMImageMessage avimImageMessage = new AVIMImageMessage(file);
        mConversation.sendMessage(avimImageMessage, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    // 发送成功
                } else {
                    ToastUtils.showToast("发送消息失败");
                }
            }
        });
    }

    public Map<String, Object> getAttr(String toId) {
        UserInfoBMob userInfo = MyApp.getUserInfo();
        Map<String, Object> map = new HashMap();
        map.put("avator", userInfo.getAvator());
        map.put("from", userInfo.getObjectId());
        map.put("to", toId);
        map.put("userId", userInfo.getObjectId());
        LogUtil.i("getAttr:",map.toString()+" "+mAvimClient.getClientId());
        return map;
    }

    public LeanChatMessage wrapperTo(AVIMTextMessage avimTextMessage) {
        LeanChatMessage leanChatMessage = new LeanChatMessage();
        leanChatMessage.setType(ChatType.TEXT.getIndex());

        return leanChatMessage;
    }

    public LeanChatMessage wrapperTo(AVIMImageMessage avimImageMessage) {
        LeanChatMessage leanChatMessage = new LeanChatMessage();
        leanChatMessage.setType(ChatType.IMAGE.getIndex());

        return leanChatMessage;
    }
}
