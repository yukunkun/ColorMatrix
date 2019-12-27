package com.matrix.yukun.matrix.leancloud_module.entity;

import org.litepal.crud.DataSupport;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author: kun .
 * date:   On 2019/12/26
 */
public class LeanChatMessage extends DataSupport implements MultiItemEntity {
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
