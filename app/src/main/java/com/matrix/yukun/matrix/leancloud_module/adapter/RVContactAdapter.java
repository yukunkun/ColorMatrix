package com.matrix.yukun.matrix.leancloud_module.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVContactAdapter extends BaseQuickAdapter<ContactInfo,BaseViewHolder> {

    public RVContactAdapter(int layoutResId, @Nullable List<ContactInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactInfo item) {

    }
}
