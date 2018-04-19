package com.ykk.pluglin_video.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haozhang.lib.SlantedTextView;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.entity.CollectsInfo;
import com.ykk.pluglin_video.play.ImageDetailActivity;
import com.ykk.pluglin_video.play.TextDetailActivity;
import com.ykk.pluglin_video.video.VideoPlayActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<CollectsInfo> jokeInfoList;



    public CollectAdapter(Context context, List<CollectsInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    public void getInfo(List<CollectsInfo> jokeInfoList){
        this.jokeInfoList=jokeInfoList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.collect_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MHolder) {
            final CollectsInfo recInfo = jokeInfoList.get(position);
            ((MHolder) holder).mTvName.setText(recInfo.getName());
            ((MHolder) holder).mTvTitle.setText(recInfo.getTitle());

            Glide.with(context).load(recInfo.getCover()).into(((MHolder) holder).mImCover);
            Glide.with(context).load(recInfo.getHeader()).into(((MHolder) holder).mCiHead);

            if(recInfo.getType()==1){
                ((MHolder) holder).mStv.setText("视频").setTextSize(5).setTextColor(Color.WHITE).setMode(SlantedTextView.MODE_LEFT);
            }else if(recInfo.getType()==2){
                ((MHolder) holder).mStv.setText("图片").setTextSize(5).setTextColor(Color.WHITE).setMode(SlantedTextView.MODE_LEFT);
            }else if(recInfo.getType()==3){
                ((MHolder) holder).mStv.setText("美文").setTextSize(5).setTextColor(Color.WHITE).setMode(SlantedTextView.MODE_LEFT);
            }
            //跳转
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recInfo.getType()==1){
                        Intent intent=new Intent(context, VideoPlayActivity.class);
                        intent.putExtra("imagepath",recInfo.getPlay_url()+"#"+recInfo.getCover()+"#"+recInfo.getTitle());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,((MHolder) holder).mImCover,"shareView").toBundle());
                        }else {
                            context.startActivity(intent);
                            ((Activity)context).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
                        }
                    }else if(recInfo.getType()==2){
                        Intent intent=new Intent(context, ImageDetailActivity.class);
                        intent.putExtra("url",recInfo.getPlay_url());
                        intent.putExtra("isGif",recInfo.isGif());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,((MHolder) holder).mImCover,"shareView").toBundle());
                        }else {
                            context.startActivity(intent);
                            ((Activity)context).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
                        }
                    }else if(recInfo.getType()==3){
                        Intent intent=new Intent(context, TextDetailActivity.class);
                        intent.putExtra("url",recInfo.getPlay_url());
                        intent.putExtra("title",recInfo.getTitle());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,((MHolder) holder).mImCover,"shareView").toBundle());
                        }else {
                            context.startActivity(intent);
                            ((Activity)context).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
                        }
                    }
                }
            });
            //share
            ((MHolder) holder).mImShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareSend(context,recInfo.getPlay_url());
                }
            });
            //移除
            ((MHolder) holder).mTvPlayTimes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(position,recInfo.getCover());
                }
            });
            //长按删除
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    delete(position,recInfo.getCover());
                    return false;
                }
            });
        }
    }

    private void delete(final int pos, final String cover) {
        new AlertDialog.Builder(context).setTitle("移除").setMessage("确定要移除吗?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DataSupport.deleteAll(CollectsInfo.class, "cover = ?", cover);
                jokeInfoList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, jokeInfoList.size() - pos);
            }
        }).show();
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
        @BindView(R2.id.stv)
        SlantedTextView mStv;

        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
