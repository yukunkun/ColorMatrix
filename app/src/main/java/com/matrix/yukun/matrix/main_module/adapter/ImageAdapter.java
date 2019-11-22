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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.activity.TextDetailActivity;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.ImageInfo;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ImageInfo> jokeInfoList;
    boolean isVertical=true;
    private int mWidth;
    ShareCallBack mShareCallBack;

    public void setShareCallBack(ShareCallBack shareCallBack) {
        mShareCallBack = shareCallBack;
    }

    public void setTextViewWidth(boolean isVertical){
        this.isVertical=isVertical;
    }


    public ImageAdapter(Context context, List<ImageInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout_item, null);
        mWidth = ScreenUtils.instance().getHeight(context);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MHolder) {
            final ImageInfo recInfo = jokeInfoList.get(position);
//            ((MHolder) holder).mTvName.setText(recInfo.get);
            ((MHolder) holder).mTvTitle.setText(recInfo.getCopyright());
            GlideUtil.loadImage("http://s.cn.bing.net"+recInfo.getUrl(),((MHolder) holder).mImCover);
            Glide.with(context).load("http://s.cn.bing.net"+recInfo.getUrl()).into(((MHolder) holder).mCiHead);
            ((MHolder) holder).mTvTimes.setText(recInfo.getStartdate());

            if(((MHolder) holder).mImCover.getHeight()> ScreenUtils.dp2Px(context,180)){
                ViewGroup.LayoutParams layoutParams=((MHolder) holder).mImCover.getLayoutParams();
                layoutParams.height= ScreenUtils.dp2Px(context,180);
                ((MHolder) holder).mImCover.setLayoutParams(layoutParams);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ImageDetailActivity.class);
                    intent.putExtra("url","http://s.cn.bing.net"+recInfo.getUrl());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,((MHolder) holder).mImCover,"shareView").toBundle());
                    }else {
                        context.startActivity(intent);
                        ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                    }
                }
            });
            ((MHolder) holder).mImShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShareCallBack.onShareCallback(position);
                }
            });

            //加入收藏
            ((MHolder) holder).mImCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<CollectsInfo> newsList = DataSupport.where("cover = ?", recInfo.getUrl()).find(CollectsInfo.class);
                    if(newsList.size()>0){
                        Toast.makeText(context, "已经添加到收藏了-_-", Toast.LENGTH_SHORT).show();
                        //存储了
                        return;
                    }else {
                        CollectsInfo collectInfo=new CollectsInfo();
                        collectInfo.setHeader("http://s.cn.bing.net"+recInfo.getUrl());
                        collectInfo.setCover("http://s.cn.bing.net"+recInfo.getUrl());
                        collectInfo.setTitle(recInfo.getCopyright());
                        collectInfo.setName(recInfo.getCopyright());
                        collectInfo.setType(2);
                        collectInfo.setPlay_url("http://s.cn.bing.net"+recInfo.getUrl());
                        collectInfo.setGif(false);
                        collectInfo.save();
                        Toast.makeText(context, "添加到收藏成功", Toast.LENGTH_SHORT).show();
                        ((MHolder) holder).mImCollect.setImageResource(R.mipmap.collection_fill);
                    }
                }
            });
        }
    }

    private void shareSend(Context context,String str) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, str);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, ""));
    }

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.ci_head)
        CircleImageView mCiHead;
        @BindView(R2.id.tv_name)
        TextView mTvName;
        @BindView(R2.id.tv_times)
        TextView mTvTimes;
        @BindView(R2.id.im_cover)
        ImageView mImCover;
        @BindView(R2.id.tv_title)
        TextView mTvTitle;
        @BindView(R2.id.tv_play_times)
        TextView mTvPlayTimes;
        @BindView(R2.id.iv_share)
        ImageView mImShare;
        @BindView(R2.id.iv_collect)
        ImageView mImCollect;
        @BindView(R2.id.ci_comment_head)
        CircleImageView mCiCommentHead;
        @BindView(R2.id.tv_comment_name)
        TextView mTvCommentName;
        @BindView(R2.id.tv_comment)
        TextView mTvCommen;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
