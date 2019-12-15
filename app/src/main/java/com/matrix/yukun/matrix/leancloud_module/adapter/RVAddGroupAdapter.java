package com.matrix.yukun.matrix.leancloud_module.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.leancloud_module.entity.SearchGroupInfo;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVAddGroupAdapter extends BaseQuickAdapter<SearchGroupInfo,BaseViewHolder> {

    public RVAddGroupAdapter(int layoutResId, @Nullable List<SearchGroupInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchGroupInfo item) {

    }
}
