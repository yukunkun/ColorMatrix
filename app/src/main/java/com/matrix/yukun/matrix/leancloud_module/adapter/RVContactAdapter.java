package com.matrix.yukun.matrix.leancloud_module.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
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

    }
}
