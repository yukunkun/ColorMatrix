package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.List;

/**
 * author: kun .
 * date:   On 2018/12/28
 */
public class RVPlayAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<PlayAllBean> mPlayAllBeans;

    public RVPlayAllAdapter(Context context, List<PlayAllBean> playAllBeans) {
        mContext = context;
        mPlayAllBeans = playAllBeans;
    }

    public void updateData(List<PlayAllBean> mPlayAllBeans){
        this.mPlayAllBeans=mPlayAllBeans;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.play_all_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            final PlayAllBean playAllBean = mPlayAllBeans.get(position);
            GlideUtil.loadImage(playAllBean.getThumbnail(),((MyHolder) holder).mImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(mContext,playAllBean.getThumbnail(),((MyHolder) holder).mImageView,false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPlayAllBeans.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public MyHolder(View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.iv_cover);
            ViewGroup.LayoutParams layoutParams=mImageView.getLayoutParams();
            layoutParams.width= ScreenUtil.getDisplayWidth();
            layoutParams.height= ScreenUtil.getDisplayHeight();
            mImageView.setLayoutParams(layoutParams);
        }
    }
}
