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
import com.matrix.yukun.matrix.leancloud_module.entity.GroupBMob;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVGroupListAdapter extends BaseQuickAdapter<GroupBMob,BaseViewHolder> {


    Context mContext;

    public RVGroupListAdapter(Context context, int layoutResId, @Nullable List<GroupBMob>  data) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupBMob item) {

    }
}
