package com.matrix.yukun.matrix.video_module.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.entity.VideoCommentBean;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: kun .
 * date:   On 2018/12/28
 */
public class RVCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<VideoCommentBean> mPlayAllBeans;

    public RVCommentAdapter(Context context, List<VideoCommentBean> playAllBeans) {
        mContext = context;
        mPlayAllBeans=playAllBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.comment_item_layout, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHolder){
            final VideoCommentBean videoCommentBean = mPlayAllBeans.get(position);
            Glide.with(mContext).load(videoCommentBean.getUser().getProfile_image()).into(((MyHolder) holder).mCVHeader);
            ((MyHolder) holder).mTvName.setText(videoCommentBean.getUser().getUsername());
            ((MyHolder) holder).mTvLike.setText(videoCommentBean.getLike_count()+"");
            ((MyHolder) holder).mTvComment.setText(videoCommentBean.getContent());
            ((MyHolder) holder).mIvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MyHolder) holder).mIvLike.setImageResource(R.mipmap.icon_liked);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1.5f, 0f, 1.5f, ((MyHolder) holder).mIvLike.getWidth() / 2f, ((MyHolder) holder).mIvLike.getHeight() / 2f);
                    scaleAnimation.setDuration(1000);
                    scaleAnimation.startNow();
                    ((MyHolder) holder).mIvLike.setAnimation(scaleAnimation);
                    ((MyHolder) holder).mTvLike.setText(videoCommentBean.getLike_count()+1+"");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPlayAllBeans.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        CircleImageView mCVHeader;
        TextView mTvComment,mTvLike,mTvName;
        ImageView mIvLike;

        public MyHolder(View itemView) {
            super(itemView);
            mCVHeader=itemView.findViewById(R.id.ci_header);
            mTvComment=itemView.findViewById(R.id.tv_comment);
            mTvName=itemView.findViewById(R.id.tv_name);
            mTvLike=itemView.findViewById(R.id.tv_like);
            mIvLike=itemView.findViewById(R.id.iv_like);
        }
    }

}
