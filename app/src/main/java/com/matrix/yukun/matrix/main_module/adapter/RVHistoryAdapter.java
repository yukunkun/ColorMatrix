package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.VideoDetailPlayActivity;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;
import com.matrix.yukun.matrix.main_module.entity.HistoryPlay;
import com.matrix.yukun.matrix.util.DataUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/1/17
 */
public class RVHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext ;
    List<HistoryPlay> historyPlayList;
    public RVHistoryAdapter(Context context, List<HistoryPlay> historyPlayList) {
        mContext = context;
        this.historyPlayList = historyPlayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.history_play_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            final HistoryPlay historyPlay = historyPlayList.get(position);
            ((MyHolder) holder).mTvTitle.setText(historyPlay.getTitle());
            ((MyHolder) holder).mTvDes.setText(historyPlay.getDescription());
            ((MyHolder) holder).mTvTime.setText("发布："+ DataUtils.getDataTime(historyPlay.getData()));
            Glide.with(mContext).load(historyPlay.getCover()).into(((MyHolder) holder).mIvCover);
            ((MyHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSupport.deleteAll(HistoryPlay.class, "cover = ?", historyPlay.getCover());
                    historyPlayList.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EyesInfo mEyesInfo=new EyesInfo();
                    EyesInfo.DataBean data = new EyesInfo.DataBean();
                    data.setPlayUrl(historyPlay.getPlay_url());
                    data.setSlogan(historyPlay.getName());
                    data.setDuration(historyPlay.getDuration());
                    data.setDescription(historyPlay.getDescription());
                    data.setTitle(historyPlay.getTitle());

                    EyesInfo.DataBean.CoverBean cover = new EyesInfo.DataBean.CoverBean();
                    EyesInfo.DataBean.AuthorBean authorBean = new EyesInfo.DataBean.AuthorBean();
                    cover.setDetail(historyPlay.getCover());
                    data.setCover(cover);
                    data.setDate(historyPlay.getData());
                    authorBean.setIcon(historyPlay.getHeader());
                    data.setAuthor(authorBean);
                    mEyesInfo.setData(data);
                    Intent intent = new Intent(mContext, VideoDetailPlayActivity.class);
                    intent.putExtra("eyesInfo",mEyesInfo);
                    intent.putExtra("next_url",historyPlay.getNextUrl());
                    intent.putExtra("type",0);//小视频界面要用
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,((MyHolder) holder).mImageView,"shareView").toBundle());
                    }else {
                        mContext.startActivity(intent);
                        ((Activity)mContext).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return historyPlayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView mTvTitle,mTvDes,mTvTime;
        ImageView mImageView,mIvCover;

        public MyHolder(View itemView) {
            super(itemView);
            mTvTitle=itemView.findViewById(R.id.tv_title);
            mTvDes=itemView.findViewById(R.id.tv_des);
            mTvTime=itemView.findViewById(R.id.tv_time);
            mImageView=itemView.findViewById(R.id.iv_delete);
            mIvCover=itemView.findViewById(R.id.iv_cover);

        }
    }
}
