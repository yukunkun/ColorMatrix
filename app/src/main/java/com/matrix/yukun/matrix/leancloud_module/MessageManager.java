package com.matrix.yukun.matrix.leancloud_module;

import android.text.TextUtils;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.chat_module.entity.ChatType;
import com.matrix.yukun.matrix.leancloud_module.adapter.LeanChatAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;
import com.matrix.yukun.matrix.leancloud_module.utils.MessageWrapper;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.AVFile;
import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.AVIMMessage;
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
    private String mChatId;

    public MessageManager() {
        mAvimClient = AVIMClient.getInstance(MyApp.getUserInfo().getId());
    }

    public static MessageManager getInstance() {
        return manager;
    }

    public void initData(String chatId) {
        this.mChatId = chatId;
    }

    /**
     * 单聊
     *
     * @param toId
     */
    public void createConversion(String toId, String toUserName, String toAvator) {
        mAvimClient.createConversation(Arrays.asList(toId), "", getAttr(toId, toUserName, toAvator), false, true, new AVIMConversationCreatedCallback() {
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
    public void createConversion(List<String> members, String toId, String toUserName, String toAvator) {
        mAvimClient.createConversation(members, "", getAttr(toId, toUserName, toAvator), false, true, new AVIMConversationCreatedCallback() {
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

    public void sendTxtMessage(String txt, String toId, String toUserName, String toAvator) {
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
        LogUtil.e(Thread.currentThread().getName());
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath(new File(imagePath).getName(), imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AVIMImageMessage avimImageMessage = new AVIMImageMessage(file);
        mConversation.sendMessage(avimImageMessage, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {

                } else {
                    LogUtil.e(e.toString() + " " + Thread.currentThread().getName());
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

    public Map<String, Object> getAttr(String toId, String toUserName, String toAvator) {
        UserInfoBMob userInfo = MyApp.getUserInfo();
        Map<String, Object> map = new HashMap();
        map.put("from", userInfo.getObjectId());
        map.put("to", toId);
        map.put("fromUserName", userInfo.getName());
        map.put("toUserName", toUserName);
        map.put("fromAvator", userInfo.getAvator());
        map.put("toAvator", toAvator);
        LogUtil.i("=======getAttr:", map.toString() + " " + mAvimClient.getClientId());
        return map;
    }

    /**
     * 接收消息
     *
     * @param message
     * @param conversation
     * @param client
     */
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        LeanChatMessage leanChatMessage = MessageWrapper.getInstance().wrapperTo(message);
        leanChatMessage.setMsgFrom((String) conversation.getAttribute("from"));
        leanChatMessage.setMsgTo((String) conversation.getAttribute("to"));
        leanChatMessage.setMsgFromUserName((String) conversation.getAttribute("fromUserName"));
        leanChatMessage.setMsgTo((String) conversation.getAttribute("toUserName"));
        leanChatMessage.setMsgFromAvator((String) conversation.getAttribute("fromAvator"));
        leanChatMessage.setMsgtoAvator((String) conversation.getAttribute("toAvator"));
        //update adapter
        LogUtil.i(leanChatMessage.toString());
        if (isCurrentChat(message.getFrom()) && mOnUpdateListener != null) {
            mOnUpdateListener.onUpdateMessage(leanChatMessage);
        }
    }

    public LeanChatMessage txtToMessage(String txt, String chatId, String chatName, String chatAvator) {
        LeanChatMessage leanChatMessage = new LeanChatMessage();
        leanChatMessage.setContent(txt);
        leanChatMessage.setMsgFrom(MyApp.getUserInfo().getId());
        leanChatMessage.setMsgFromAvator(MyApp.getUserInfo().getAvator());
        leanChatMessage.setMsgFromUserName(MyApp.getUserInfo().getName());
        leanChatMessage.setMsgTo(chatId);
        leanChatMessage.setMsgToAvator(chatAvator);
        leanChatMessage.setMsgToUserName(chatName);
        leanChatMessage.setTimeStamp(System.currentTimeMillis());
        leanChatMessage.setType(ChatType.TEXT.getIndex());
        return leanChatMessage;
    }

    public LeanChatMessage imageToMessage(String path, String chatId, String chatName, String chatAvator) {
        LeanChatMessage leanChatMessage = new LeanChatMessage();
        leanChatMessage.setImagePath(path);
        leanChatMessage.setMsgFrom(MyApp.getUserInfo().getId());
        leanChatMessage.setMsgFromAvator(MyApp.getUserInfo().getAvator());
        leanChatMessage.setMsgFromUserName(MyApp.getUserInfo().getName());
        leanChatMessage.setMsgTo(chatId);
        leanChatMessage.setMsgToAvator(chatAvator);
        leanChatMessage.setMsgToUserName(chatName);
        leanChatMessage.setTimeStamp(System.currentTimeMillis());
        leanChatMessage.setType(ChatType.IMAGE.getIndex());
        return leanChatMessage;
    }

    /**
     * 消息回执
     *
     * @param message
     * @param conversation
     * @param client
     */

    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

    }

    private boolean isCurrentChat(String chatId) {
        if (TextUtils.isEmpty(mChatId) || TextUtils.isEmpty(chatId)) {
            return false;
        }
        return mChatId.equals(chatId);
    }


    public onUpdateListener mOnUpdateListener;

    public void setOnUpdateListener(onUpdateListener onUpdateListener) {
        mOnUpdateListener = onUpdateListener;
    }

    public interface onUpdateListener {
        void onUpdateMessage(LeanChatMessage leanChatMessage);
    }
}
