package com.matrix.yukun.matrix.leancloud_module.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVAddFriendAdapter extends BaseQuickAdapter<UserInfoBMob,BaseViewHolder> {

    public RVAddFriendAdapter(int layoutResId, @Nullable List<UserInfoBMob>  data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoBMob item) {

    }
}
