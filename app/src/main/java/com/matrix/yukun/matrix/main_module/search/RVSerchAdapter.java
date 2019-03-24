package com.matrix.yukun.matrix.main_module.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.play.PersonActivity;
import com.matrix.yukun.matrix.video_module.play.VideoDetailPlayActivity;
import com.matrix.yukun.matrix.video_module.video.VideoPlayActivity;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2019/3/23.
 */

public class RVSerchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DBSearchInfo> mDBSearchInfos;

    public RVSerchAdapter(Context context, List<DBSearchInfo> DBSearchInfos) {
        mContext = context;
        mDBSearchInfos = DBSearchInfos;
    }

    public void update(List<DBSearchInfo> mDBSearchInfos){
        this.mDBSearchInfos=mDBSearchInfos;
        LogUtil.i("===========ad",this.mDBSearchInfos.toString());
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(viewType == 1){
            view = LayoutInflater.from(mContext).inflate(R.layout.search_header_layout,null);
           return new HeaderHolder(view);
        }else if(viewType == 2){
            view = LayoutInflater.from(mContext).inflate(R.layout.search_video_layout,null);
            return new VideoHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DBSearchInfo dbSearchInfo = mDBSearchInfos.get(position);
        if(holder instanceof HeaderHolder){
            ((HeaderHolder) holder).mTVTitle.setText(dbSearchInfo.getTitle());
            ((HeaderHolder) holder).mTVDes.setText(dbSearchInfo.getSlogan());
            Glide.with(mContext).load(dbSearchInfo.getAvatar()).into(((HeaderHolder) holder).mCircleImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.start(mContext,DBSearchInfo.countToEyeInfo(dbSearchInfo),dbSearchInfo.getNextUrl());
                }
            });
        }else if(holder instanceof VideoHolder){
            ((VideoHolder) holder).mTvTitle.setText(dbSearchInfo.getTitle());
            ((VideoHolder) holder).mTvDes.setText(dbSearchInfo.getDescription());
            ((VideoHolder) holder).mTvslogan.setText(dbSearchInfo.getSlogan());
            Glide.with(mContext).load(dbSearchInfo.getAvatar()).into(((VideoHolder) holder).mCircleImageView);
            Glide.with(mContext).load(dbSearchInfo.getVideoImage()).into(((VideoHolder) holder).mIvCover);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoDetailPlayActivity.start(mContext,DBSearchInfo.countToEyeInfo(dbSearchInfo),dbSearchInfo.getNextUrl());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mDBSearchInfos.get(position).getSearchType()==1){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return mDBSearchInfos.size();
    }

    class HeaderHolder extends RecyclerView.ViewHolder{
        TextView mTVTitle,mTVDes;
        CircleImageView mCircleImageView;
        public HeaderHolder(View itemView) {
            super(itemView);
            mTVTitle=itemView.findViewById(R.id.tv_title);
            mTVDes=itemView.findViewById(R.id.tv_des);
            mCircleImageView=itemView.findViewById(R.id.cv_avatar);
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        CircleImageView mCircleImageView;
        ImageView mIvCover;
        TextView mTvDes,mTvTitle,mTvslogan;
        public VideoHolder(View itemView) {
            super(itemView);
            mTvTitle=itemView.findViewById(R.id.tv_title);
            mTvDes=itemView.findViewById(R.id.tv_des);
            mTvslogan=itemView.findViewById(R.id.tv_slogan);
            mIvCover=itemView.findViewById(R.id.iv_cover);
            mCircleImageView=itemView.findViewById(R.id.cv_avatar);
        }
    }
}
