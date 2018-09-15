package com.ykk.pluglin_video.adapter;

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
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.entity.CollectsInfo;
import com.ykk.pluglin_video.entity.ImageInfo;
import com.ykk.pluglin_video.entity.RecInfo;
import com.ykk.pluglin_video.play.ImageDetailActivity;
import com.ykk.pluglin_video.utils.ScreenUtils;

import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.text.SimpleDateFormat;
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

    public void setTextViewWidth( boolean isVertical){
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
            ((MHolder) holder).mTvName.setText(recInfo.getType());
            ((MHolder) holder).mTvTitle.setText(recInfo.getType());
//            ((MHolder) holder).mTvPlayTimes.setText(recInfo.getComment()+"次");
//            ((MHolder) holder).mTvCommentName.setText(recInfo.getTop_commentsName());
//            if(recInfo.getTop_commentsContent()==null){
//                ((MHolder) holder).mTvCommen.setVisibility(View.GONE);
//            }else {
//                ((MHolder) holder).mTvCommen.setVisibility(View.VISIBLE);
//                ((MHolder) holder).mTvCommen.setText(recInfo.getTop_commentsContent());
//            }
            Glide.with(context).load(recInfo.getUrl()).error(R.drawable.head_4).into(((MHolder) holder).mImCover);
            Glide.with(context).load(recInfo.getUrl()).placeholder(R.mipmap.tool_icon).into(((MHolder) holder).mCiCommentHead);
            Glide.with(context).load(recInfo.getUrl()).into(((MHolder) holder).mCiHead);
            ((MHolder) holder).mTvTimes.setText(recInfo.getPublishedAt());

            if(((MHolder) holder).mImCover.getHeight()>ScreenUtils.dp2Px(context,180)){
                ViewGroup.LayoutParams layoutParams=((MHolder) holder).mImCover.getLayoutParams();
                layoutParams.height=ScreenUtils.dp2Px(context,180);
                ((MHolder) holder).mImCover.setLayoutParams(layoutParams);
            }
//            if("".equals(recInfo.getImage())){
//                ((MHolder) holder).mImCover.setVisibility(View.GONE);
//            }else {
//                ((MHolder) holder).mImCover.setVisibility(View.VISIBLE);
//            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ImageDetailActivity.class);
                    intent.putExtra("url",recInfo.getUrl());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,((MHolder) holder).mImCover,"shareView").toBundle());
                    }else {
                        context.startActivity(intent);
                        ((Activity)context).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
                    }
                }
            });
            ((MHolder) holder).mImShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareSend(context,recInfo.getUrl());
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
                        collectInfo.setHeader(recInfo.getUrl());
                        collectInfo.setCover(recInfo.getUrl());
                        collectInfo.setTitle(recInfo.getType());
                        collectInfo.setName(recInfo.getType());
                        collectInfo.setType(2);
                        collectInfo.setPlay_url(recInfo.getUrl());
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
