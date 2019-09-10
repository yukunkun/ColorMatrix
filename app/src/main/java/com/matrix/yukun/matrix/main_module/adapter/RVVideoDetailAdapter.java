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

import java.util.List;

/**
 * author: kun .
 * date:   On 2018/12/27
 */
public class RVVideoDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<EyesInfo> mEyesInfos;
    String mNextUrl;
    int mType;
    public RVVideoDetailAdapter(Context context, List<EyesInfo> eyesInfos, String nextUrl, int type) {
        mContext = context;
        mEyesInfos = eyesInfos;
        mNextUrl=nextUrl;
        mType=type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.detail_video_item,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            EyesInfo.DataBean data = mEyesInfos.get(position).getData();
            ((MyHolder) holder).mTvSlogn.setText(data.getAuthor().getName());
            ((MyHolder) holder).mTvTitle.setText(data.getTitle());
            ((MyHolder) holder).mTvTime.setText("时长•"+getDuration(data.getDuration()));
            Glide.with(mContext).load(data.getCover().getDetail()).into(((MyHolder) holder).mIvCover);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VideoDetailPlayActivity.class);
                    intent.putExtra("eyesInfo",mEyesInfos.get(position));
                    intent.putExtra("next_url",mNextUrl);
                    intent.putExtra("type",mType);//小视频界面要用
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,((MyHolder) holder).mIvCover,"shareView").toBundle());
                    }else {
                        mContext.startActivity(intent);
                        ((Activity)mContext).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                    }
                    ((Activity) mContext).finish();
                }
            });
        }
    }

    private String getDuration(int duration) {
        if(duration==0){
            return "未知";
        }
        if(duration<=60){
            return duration+"''";
        }else if(duration<60*60){
            return duration/60+"'"+duration%60+"''";
        }else if(duration<60*60*24){
            return duration/(60*60)+"°"+duration/(60*60)%60+"'"+duration%60+"''";
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mEyesInfos.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView mTvTitle,mTvSlogn,mTvTime;
        ImageView mIvCover;
        public MyHolder(View itemView) {
            super(itemView);
            mTvTitle=itemView.findViewById(R.id.tv_title);
            mTvSlogn=itemView.findViewById(R.id.tv_slogn);
            mTvTime=itemView.findViewById(R.id.tv_time);
            mIvCover=itemView.findViewById(R.id.iv_cover);
        }
    }
}
