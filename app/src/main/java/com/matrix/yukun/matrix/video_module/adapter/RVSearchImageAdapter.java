package com.matrix.yukun.matrix.video_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.entity.HistoryInfo;
import com.matrix.yukun.matrix.video_module.entity.SearchImageInfo;
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yukun on 17-11-20.
 */

public class RVSearchImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SearchImageInfo> jokeInfoList;

    public RVSearchImageAdapter(Context context, List<SearchImageInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MHolder) {
            final SearchImageInfo searchImageInfo = jokeInfoList.get(position);
            if(!TextUtils.isEmpty(searchImageInfo.getImage_url())){
                ViewGroup.LayoutParams layoutParams = ((MHolder) holder).mIvCover.getLayoutParams();
                float v = (searchImageInfo.getImage_height() / (float)searchImageInfo.getImage_width());
                layoutParams.height=(int)(ScreenUtils.instance().getWidth(context)/3.0*v);
                ((MHolder) holder).mIvCover.setLayoutParams(layoutParams);
                Glide.with(context).load(searchImageInfo.getImage_url()).into(((MHolder) holder).mIvCover);
                ((MHolder) holder).mTvDes.setText(searchImageInfo.getAbs());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDetailActivity.start(context,searchImageInfo.getImage_url(),searchImageInfo.getImage_url().endsWith("gif"));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_des)
        TextView mTvDes;
        @BindView(R2.id.iv_cover)
        ImageView mIvCover;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
