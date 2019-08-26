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
import com.matrix.yukun.matrix.util.DataUtils;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class RVRecommendAdapter extends BaseQuickAdapter<GaiaIndexBean, BaseViewHolder> {
    private int type;

    public RVRecommendAdapter(int layoutResId, @Nullable List<GaiaIndexBean> data, int type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, GaiaIndexBean item) {
        helper.setText(R.id.player_rec_title, item.getName());
        if (item.getTime() != null&&type==0) {
            helper.setText(R.id.player_rec_duration, DataUtils.secToTime(item.getDuration()) + "");
            helper.setText(R.id.player_rec_publish_time, DataUtils.getGaiaTime(item.getTime().getTime()));
        }
        if(item.getCreateTime()!=null&&type==1) {
            helper.setText(R.id.player_rec_publish_time, DataUtils.getGaiaTime(item.getCreateTime().getTime()));
            helper.setText(R.id.player_rec_duration, "");
        }
        ImageView cover = helper.getView(R.id.player_rec_cover);
        if (item.getCover() != null && !item.getCover().isEmpty() && !"null".equals(item.getCover())) {
            Glide.with(mContext).load(Api.COVER_PREFIX + item.getCover()).placeholder(R.mipmap.bg_header_nav).into(cover);
        } else if (!TextUtils.isEmpty(item.getScreenshot())) {
            if(type==0){
                if (item.getFlag() == 1) {
                    Glide.with(mContext).load(Api.COVER_PREFIX + item.getScreenshot() + "_18.png").placeholder(R.mipmap.bg_header_nav).into(cover);
                } else if (item.getFlag() == 0) {
                    Glide.with(mContext).load(Api.COVER_PREFIX + item.getScreenshot().replace(".", "_18.")).placeholder(R.mipmap.bg_header_nav).into(cover);
                }
            }else {
                Glide.with(mContext).load(Api.COVER_PREFIX + item.getScreenshot() + "_18.png").placeholder(R.mipmap.bg_header_nav).into(cover);
            }
        }
        if (item.getHave4k() == 1) {
            helper.getView(R.id.player_rec_is4k).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.player_rec_is4k).setVisibility(View.GONE);
        }
    }
}
