package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.VerticalVideoActivity;
import com.matrix.yukun.matrix.main_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.play_all_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            final PlayAllBean playAllBean = mPlayAllBeans.get(position);
            ((MyHolder) holder).mTVName.setText(playAllBean.getUsername());
            ((MyHolder) holder).mTvDes.setText(playAllBean.getText());
            ((MyHolder) holder).mTvCommentName.setText(TextUtils.isEmpty(playAllBean.getTop_commentsName())?"佚名":playAllBean.getTop_commentsName());
            ((MyHolder) holder).mTvCommentDes.setText(TextUtils.isEmpty(playAllBean.getTop_commentsContent())? "没有吐槽": "吐槽:"+playAllBean.getTop_commentsContent());
            GlideUtil.loadImage(playAllBean.getThumbnail(),((MyHolder) holder).mImageView);
            GlideUtil.loadImage(playAllBean.getHeader(),((MyHolder) holder).mCVHeader);
            GlideUtil.loadImage(playAllBean.getTop_commentsHeader(),((MyHolder) holder).mCVCommentHeader);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<PlayAllBean> playAllBeans=new ArrayList<>();
                    for (int i = position; i < mPlayAllBeans.size(); i++) {
                        playAllBeans.add(mPlayAllBeans.get(i));
                    }
                    Intent intent=new Intent(mContext, VerticalVideoActivity.class);
                    intent.putExtra("playAllBean",playAllBeans);
                    intent.putExtra("type",5);
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
        return mPlayAllBeans.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        CircleImageView mCVHeader,mCVCommentHeader;
        TextView mTVName,mTvCommentName,mTvDes,mTvCommentDes;
        public MyHolder(View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.iv_cover);
            mCVHeader=itemView.findViewById(R.id.cv_header);
            mCVCommentHeader=itemView.findViewById(R.id.cv_comment_header);
            mTVName=itemView.findViewById(R.id.tv_name);
            mTvCommentName=itemView.findViewById(R.id.tv_comment_name);
            mTvCommentDes=itemView.findViewById(R.id.tv_comment_des);
            mTvDes=itemView.findViewById(R.id.tv_des);

        }
    }
}
