package com.matrix.yukun.matrix.leancloud_module;

import cn.leancloud.im.v2.AVIMClient;
import cn.leancloud.im.v2.AVIMException;
import cn.leancloud.im.v2.callback.AVIMClientCallback;

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
                    isLogionleanCloud=true;
                }
            }
        });
    }

    public boolean isLogionleanCloud() {
        return isLogionleanCloud;
    }

    public void setAvimClient(AVIMClient avimClient) {
        mAvimClient = avimClient;
    }

    public void logout(){
        if(mAvimClient!=null){
            mAvimClient.close(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient client, AVIMException e) {
                    if(e!=null){
                        mAvimClient=null;
                        isLogionleanCloud=false;
                    }
                }
            });
        }
    }
}
