package com.matrix.yukun.matrix.leancloud_module.entity;

import org.litepal.crud.DataSupport;

/**
 * author: kun .
 * date:   On 2019/12/16
 */
public class AddFriendInfo extends DataSupport {
    private String name;
    private String avatar;
    private boolean isAdd;
    private String createTime;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
