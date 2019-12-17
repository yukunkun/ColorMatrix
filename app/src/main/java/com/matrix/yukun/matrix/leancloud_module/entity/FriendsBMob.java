package com.matrix.yukun.matrix.leancloud_module.entity;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * author: kun .
 * date:   On 2019/12/16
 */
public class FriendsBMob extends BmobObject {
    private ArrayList<String> mFriendList;
    private String userId;
    private String avator;
    private String name;
    private String id;

    public ArrayList<String> getFriendList() {
        return mFriendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        mFriendList = friendList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getId() {
        return getObjectId();
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FriendsBMob{" +
                " userId='" + userId + '\'' +
                ", avator='" + avator + '\'' +
                ", name='" + name + '\'' +
                ", id='" + getId() + '\'' +
                ", mFriendList=" + mFriendList +
                '}';
    }
}
