package com.matrix.yukun.matrix.leancloud_module.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.LeanCloudInit;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVAddFriendAdapter extends BaseQuickAdapter<UserInfoBMob,BaseViewHolder> {

    Context mContext;
    public RVAddFriendAdapter(Context context,int layoutResId, @Nullable List<UserInfoBMob>  data) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoBMob item) {
        helper.setText(R.id.tv_nickname,item.getName());
        GlideUtil.loadCircleImage(item.getAvator(),helper.getView(R.id.iv_avatar));
        TextView tvAddView = helper.getView(R.id.tv_add);
        if(item.getId().equals(MyApp.getUserInfo().getId())){
            tvAddView.setClickable(false);
            tvAddView.setText("已添加");
            tvAddView.setTextColor(mContext.getResources().getColor(R.color.color_back_more));
            tvAddView.setBackgroundResource(R.drawable.shape_collect_bg_checked);
        }

        tvAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddView.setClickable(false);
                tvAddView.setText("已申请");
                tvAddView.setTextColor(mContext.getResources().getColor(R.color.color_back_more));
                tvAddView.setBackgroundResource(R.drawable.shape_collect_bg_checked);
                LeanCloudInit.getInstance().sendSystemAdd(item.getId(),UserInfoBMob.toJson(MyApp.getUserInfo()));

            }
        });
    }
}
