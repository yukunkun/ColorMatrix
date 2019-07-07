package com.matrix.yukun.matrix.gaia_module.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.net.Api;

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
        helper.setText(R.id.work_name,item.getName());
        helper.setText(R.id.work_grade,item.getGrade()+"");
        ImageView cover=(ImageView) helper.getView(R.id.cover);
        if(item.getCover()!=null&&!item.getCover().isEmpty()&&!"null".equals(item.getCover())){
            Glide.with(mContext).load(Api.COVER_PREFIX+item.getCover()).placeholder(R.mipmap.bg_header_nav).into(cover);
        }else if(!TextUtils.isEmpty(item.getScreenshot())){
            if(item.getFlag()==1){
                Glide.with(mContext).load(Api.COVER_PREFIX+item.getScreenshot()+"_18.png").placeholder(R.mipmap.bg_header_nav).into(cover);
            }else if(item.getFlag()==0){
                Glide.with(mContext).load(Api.COVER_PREFIX+item.getScreenshot().replace(".","_18.")).placeholder(R.mipmap.bg_header_nav).into(cover);
            }
        }
        if(item.getHave4k()==1){
            helper.getView(R.id.is_4k).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.is_4k).setVisibility(View.GONE);
        }
        helper.getView(R.id.is_official).setVisibility(View.VISIBLE);
    }
}
