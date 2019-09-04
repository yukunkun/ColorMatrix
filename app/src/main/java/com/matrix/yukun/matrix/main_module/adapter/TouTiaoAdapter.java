package com.matrix.yukun.matrix.main_module.adapter;

import android.support.annotation.NonNull;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.TouTiaoBean;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/9/4
 */
public class TouTiaoAdapter extends BaseMultiItemQuickAdapter<TouTiaoBean,BaseViewHolder> {

    public TouTiaoAdapter(List<TouTiaoBean> data) {
        super(data);
        addItemType(1, R.layout.item_toutiao_video);
//        addItemType(0, R.layout.item_toutiao_defaut);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TouTiaoBean item) {
        int itemViewType = helper.getItemViewType();
        if(itemViewType==1){
            helper.setText(R.id.tv_name,item.getUser_info().getName());
            helper.setText(R.id.tv_des,item.getTitle());
            GlideUtil.loadCircleBoardImage(item.getUser_info().getAvatar_url(), helper.getView(R.id.iv_avator));
//            GlideUtil.loadImage(item.getVideo_detail_info().getDetail_video_large_image().getUrl(), helper.getView(R.id.iv_cover));
        }
    }
}
