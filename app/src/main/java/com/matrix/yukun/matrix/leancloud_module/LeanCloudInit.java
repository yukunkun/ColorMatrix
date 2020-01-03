package com.matrix.yukun.matrix.leancloud_module;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.leancloud_module.common.LeanConatant;
import com.matrix.yukun.matrix.leancloud_module.impl.ConversitionListenerImpl;
import com.matrix.yukun.matrix.leancloud_module.impl.LoginListenerImpl;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.AVQuery;
import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMConversationsQuery;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.callback.AVIMClientCallback;
import cn.leancloud.im.v2.callback.AVIMConversationCallback;
import cn.leancloud.im.v2.callback.AVIMConversationCreatedCallback;
import cn.leancloud.im.v2.callback.AVIMConversationQueryCallback;
import cn.leancloud.im.v2.messages.AVIMTextMessage;

/**
 * author: kun .
 * date:   On 2019/12/12
 */
public class LeanCloudInit {

    public static LeanCloudInit mLeanCloudInit = new LeanCloudInit();
    public static AVIMClient mAvimClient;
    private boolean isLogionleanCloud = false;

    public LeanCloudInit() {

    }

    public static LeanCloudInit getInstance() {
        return mLeanCloudInit;
    }

    public void init(String userId, LoginListenerImpl loginListener) {
        mAvimClient = AVIMClient.getInstance(userId);
        mAvimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    if(loginListener!=null){
                        loginListener.login();
                    }
                    LogUtil.i("登录leancloud成功");
                    isLogionleanCloud = true;
                } else {
                    LogUtil.i("登录leancloud成功");
                    if(loginListener!=null){
                        loginListener.error(e);
                    }
                }
            }
        });
    }

    public void init(String userId) {
        mAvimClient = AVIMClient.getInstance(userId);
        mAvimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    LogUtil.i("登录leancloud成功");
                    isLogionleanCloud = true;
                } else {
                    LogUtil.i("登录leancloud失败:" + e.toString() + " " + userId);
                }
            }
        });
    }

    public void sendSystemMessage(String id,Map map, AVIMTextMessage avimTextMessage) {
        AVIMClient mAvimClientSystem = AVIMClient.getInstance(LeanConatant.SystemAdmin);
        mAvimClientSystem.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    mAvimClientSystem.createConversation(Arrays.asList(id), "系统消息", map, false, true,
                            new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        // 创建成功
                                        conversation.sendMessage(avimTextMessage, new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {

                                                } else {

                                                }
                                            }
                                        });
                                    } else {
                                        LogUtil.i("createConversation失败:" + e.toString());
                                    }
                                    // mAvimClient 重新初始化
                                }
                            });

                } else {
                    LogUtil.i("登录leancloud失败:" + e.toString());
                }
            }
        });
    }


    public void searchRecent(ConversitionListenerImpl listener) {
        if(mAvimClient!=null){
            AVIMConversationsQuery query = mAvimClient.getConversationsQuery();
            /* 设置查询选项，指定返回对话的最后一条消息 */
            query.setWithLastMessagesRefreshed(true);
//        query.whereContainsIn("m", Arrays.asList(MyApp.getUserInfo().getObjectId()));
//        query.whereEqualTo("objectId","5df3375a90aef5aa8428effd");
            query.setQueryPolicy(AVQuery.CachePolicy.IGNORE_CACHE);
            query.findInBackground(new AVIMConversationQueryCallback() {
                @Override
                public void done(List<AVIMConversation> convs, AVIMException e) {
                    if (e == null) {
                        listener.conversitionData(convs);
                        // 获取符合查询条件的 Conversation 列表
                    } else {
                        LogUtil.i(e.toString());
                        listener.error(e);
                    }
                }
            });
        }

    }

    public void sendSystemAdd(String id, String userInfo,String toUserName) {

        Map<String, Object> map = new HashMap();
        map.put("from", LeanConatant.SystemMessage);
        map.put("to", id);
        map.put("fromUserName", "系统消息");
        map.put("toUserName", toUserName);
        map.put("avator", AppConstant.APP_ICON_URl);
        AVIMTextMessage avimTextMessage = new AVIMTextMessage();
        avimTextMessage.setText(userInfo);
        sendSystemMessage(id,map, avimTextMessage);
    }

    public boolean isLogionleanCloud() {
        return isLogionleanCloud;
    }

    public void setLogionleanCloud(boolean b) {
        isLogionleanCloud=b;
    }

    public void logout() {
//        if (mAvimClient != null) {
//            mAvimClient.close(new AVIMClientCallback() {
//                @Override
//                public void done(AVIMClient client, AVIMException e) {
//                    if (e == null) {
//                        mAvimClient = null;
//                        isLogionleanCloud = false;
//                    }
//                }
//            });
//        }
    }
}
