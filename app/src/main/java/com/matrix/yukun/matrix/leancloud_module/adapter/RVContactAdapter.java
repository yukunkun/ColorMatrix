package com.matrix.yukun.matrix.leancloud_module.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.common.LeanConatant;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.List;

import androidx.annotation.Nullable;

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
        if(item!=null&& !TextUtils.isEmpty(item.getFrom())&&(item.getFrom().equals(LeanConatant.SystemMessage)|| item.getTo().equals(LeanConatant.SystemMessage))){
            helper.setText(R.id.tv_name, item.getFrom());
            helper.setText(R.id.tv_context,mContext.getString(R.string.system_message));
            GlideUtil.loadCircleImage(R.mipmap.icon_system_msg,helper.getView(R.id.iv_avatar));
            helper.setText(R.id.tv_time, coverToTime(Long.valueOf(item.getLastTime())));
        }else {
            helper.setText(R.id.tv_name,(!item.getFrom().equals(MyApp.getUserInfo().getId()))?item.getFromUserName():item.getToUserName());
            helper.setText(R.id.tv_context,item.getLastMessage());
            GlideUtil.loadCircleImage((!item.getFrom().equals(MyApp.getUserInfo().getId()))?item.getFromAvator():item.getToAvator(),helper.getView(R.id.iv_avatar));
            helper.setText(R.id.tv_time, coverToTime(Long.valueOf(item.getLastTime())));
        }
        TextView tvAccount=helper.getView(R.id.tv_account);
        if(item.getUnreadMessagesCount()>0){
            tvAccount.setVisibility(View.VISIBLE);
            tvAccount.setText(item.getUnreadMessagesCount()+"");
        }else {
            tvAccount.setVisibility(View.GONE);
        }
    }

    private String coverToTime(Long aLong) {
        if(System.currentTimeMillis()-aLong<24*60*60*1000){
            return DataUtils.getTime(aLong,"HH:mm");
        }else if(System.currentTimeMillis()-aLong<2*24*60*60*1000){
            return "昨天";
        }else {
            return DataUtils.getTime(aLong,"MM-dd");
        }
    }
}
