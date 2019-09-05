package com.matrix.yukun.matrix.main_module.adapter;

import android.support.annotation.NonNull;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.TouTiaoBean;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/9/4
 */
public class TouTiaoAdapter extends BaseMultiItemQuickAdapter<TouTiaoBean,BaseViewHolder> {

    public TouTiaoAdapter(List<TouTiaoBean> data) {
        super(data);
        addItemType(1, R.layout.item_toutiao_video);
        addItemType(2, R.layout.item_toutiao_image);
        addItemType(3, R.layout.item_toutiao_image_three);
        addItemType(4, R.layout.item_toutiao_video);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TouTiaoBean item) {
        int itemViewType = helper.getItemViewType();
        if(itemViewType==1){
            helper.setText(R.id.tv_name,item.getUser_info().getName());
            helper.setText(R.id.tv_des,item.getTitle());
            helper.setText(R.id.tv_time,"时长·"+ DataUtils.getDuration(item.getVideo_duration()));
            GlideUtil.loadCircleBoardImage(item.getUser_info().getAvatar_url(), helper.getView(R.id.iv_avator));
            GlideUtil.loadPlaceholderImage(item.getVideo_detail_info().getDetail_video_large_image().getUrl(), helper.getView(R.id.iv_cover));
        }
        if(itemViewType==2){
            helper.setText(R.id.tv_des,item.getTitle());
            helper.setText(R.id.tv_play,"评论·"+item.getComment_count()+" 时间："+DataUtils.getTime(item.getPublish_time()*1000,"YYYY年MM月dd日"));
            GlideUtil.loadPlaceholderImage(item.getMiddle_image().getUrl(), helper.getView(R.id.iv_cover));
        }
        if(itemViewType==3){
            helper.setText(R.id.tv_name,item.getUser_info().getName());
            helper.setText(R.id.tv_des,item.getTitle());
            GlideUtil.loadPlaceholderImage(item.getImage_list().get(0).getUrl(), helper.getView(R.id.image_cover_1));
            GlideUtil.loadPlaceholderImage(item.getImage_list().get(1).getUrl(), helper.getView(R.id.image_cover_2));
            GlideUtil.loadPlaceholderImage(item.getImage_list().get(2).getUrl(), helper.getView(R.id.image_cover_3));
            GlideUtil.loadCircleBoardImage(item.getUser_info().getAvatar_url(), helper.getView(R.id.iv_avator));
        }
    }
}
