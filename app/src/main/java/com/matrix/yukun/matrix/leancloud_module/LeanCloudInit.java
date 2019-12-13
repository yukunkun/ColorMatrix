package com.matrix.yukun.matrix.leancloud_module;

import com.matrix.yukun.matrix.util.log.LogUtil;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMConversation;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.callback.AVIMClientCallback;
import cn.leancloud.im.v2.callback.AVIMClientStatusCallback;
import cn.leancloud.im.v2.callback.AVIMConversationCreatedCallback;

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
//        mAvimClient.getClientStatus(new AVIMClientStatusCallback() {
//            @Override
//            public void done(AVIMClient.AVIMClientStatus client) {
//                LogUtil.i("state "+client.name());
//            }
//        });

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
