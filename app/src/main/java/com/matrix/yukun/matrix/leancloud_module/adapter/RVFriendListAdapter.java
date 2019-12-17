package com.matrix.yukun.matrix.leancloud_module.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.LeanCloudInit;
import com.matrix.yukun.matrix.leancloud_module.entity.FriendsBMob;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVFriendListAdapter extends BaseQuickAdapter<UserInfoBMob,BaseViewHolder> {

    Context mContext;
    public RVFriendListAdapter(Context context, int layoutResId, @Nullable List<UserInfoBMob>  data) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoBMob item) {
        if(item!=null){
            LogUtil.i(item.toString());
            helper.setText(R.id.tv_nickname,item.getName()+"");
            GlideUtil.loadCircleImage(item.getAvator(),helper.getView(R.id.iv_avatar));
        }
    }
}
