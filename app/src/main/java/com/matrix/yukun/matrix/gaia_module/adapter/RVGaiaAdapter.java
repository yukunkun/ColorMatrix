package com.matrix.yukun.matrix.gaia_module.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVGaiaAdapter extends BaseQuickAdapter<GaiaIndexBean,BaseViewHolder> {


    public RVGaiaAdapter(int layoutResId, @Nullable List<GaiaIndexBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GaiaIndexBean item) {

    }
}
