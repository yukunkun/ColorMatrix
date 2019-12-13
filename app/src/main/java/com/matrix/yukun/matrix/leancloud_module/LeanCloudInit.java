package com.matrix.yukun.matrix.leancloud_module;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.leancloud_module.impl.ConversitionListenerImpl;
import com.matrix.yukun.matrix.util.DataUtils;
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
    private AVIMClient mAvimClient;
    private boolean isLogionleanCloud=false;

    public LeanCloudInit() {

    }

    public static LeanCloudInit getInstance() {
        return mLeanCloudInit;
    }

    public void init(String userId){
        mAvimClient = AVIMClient.getInstance(userId);
        mAvimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if(e==null){
                    LogUtil.i("登录leancloud成功");
                    isLogionleanCloud=true;
                }else {
                    LogUtil.i("登录leancloud失败:"+e.toString() +" "+userId);
                }
            }
        });
    }

    public void searchRecent(ConversitionListenerImpl listener){

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
                    LogUtil.i(convs.size()+" "+convs.toString());
                    listener.conversitionData(convs);
                    // 获取符合查询条件的 Conversation 列表
                }else {
                    LogUtil.i(e.toString());
                    listener.error(e);
                }
            }
        });

//        Map<String ,Object> map=new HashMap();
//        map.put("avator","http://mmbiz.qpic.cn/mmbiz/PwIlO51l7wuFyoFwAXfqPNETWCibjNACIt6ydN7vw8LeIwT7IjyG3eeribmK4rhibecvNKiaT2qeJRIWXLuKYPiaqtQ/0");
//        map.put("from",MyApp.getUserInfo().getName());
//        map.put("to","地方分发到");
//        map.put("userId",MyApp.getUserInfo().getObjectId());
//        mAvimClient.createConversation(Arrays.asList("地方分发到"), "", map, false, true,
//                new AVIMConversationCreatedCallback() {
//                    @Override
//                    public void done(AVIMConversation conversation, AVIMException e) {
//                        if(e == null) {
//                            // 创建成功
//                            LogUtil.i("createConversation成功");
//                            AVIMTextMessage avimTextMessage=new AVIMTextMessage();
//                            avimTextMessage.setText("一个坏蛋:"+ DataUtils.getDataTime(System.currentTimeMillis())+"反对犯得上");
//                            conversation.sendMessage(avimTextMessage, new AVIMConversationCallback() {
//                                @Override
//                                public void done(AVIMException e) {
//                                    if(e==null){
//                                        LogUtil.i("sendMessage成功");
//                                        isLogionleanCloud=true;
//                                    }else {
//                                        LogUtil.i("sendMessage失败:"+e.toString());
//                                    }
//                                }
//                            });
//                        }else {
//                            LogUtil.i("createConversation失败:"+e.toString());
//                        }
//                    }
//                });

    }

    public boolean isLogionleanCloud() {
        return isLogionleanCloud;
    }

    public void logout(){
        if(mAvimClient!=null){
            mAvimClient.close(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient client, AVIMException e) {
                    if(e==null){
                        mAvimClient=null;
                        isLogionleanCloud=false;
                    }
                }
            });
        }
    }
}
