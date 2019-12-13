package com.matrix.yukun.matrix.leancloud_module.adapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

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
        helper.setText(R.id.tv_name, MyApp.getUserInfo().getName().equals(item.getTo())?item.getFrom():item.getTo());
        helper.setText(R.id.tv_context,item.getLastMessage());
        GlideUtil.loadCircleImage(item.getAvator()+"",helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_time, coverToTime(Long.valueOf(item.getLastTime())));
    }

    private String coverToTime(Long aLong) {
        if(System.currentTimeMillis()-aLong<24*60*60*1000){
            return DataUtils.getTime(aLong,"HH:mm");
        }else if(System.currentTimeMillis()-aLong<2*24*60*60*1000){
            return "昨天";
        }else {
            return DataUtils.getTime(aLong,"YY:MM:DD");
        }
    }
}
