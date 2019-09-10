package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haozhang.lib.SlantedTextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.activity.TextDetailActivity;
import com.matrix.yukun.matrix.main_module.activity.VideoDetailPlayActivity;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

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
    ShareCallBack mShareCallBack;

    public void setShareCallBack(ShareCallBack shareCallBack) {
        mShareCallBack = shareCallBack;
    }

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
            GlideUtil.loadImage(recInfo.getCover(),(((MHolder) holder).mImCover));
            GlideUtil.loadCircleBoardImage(recInfo.getCover(),(((MHolder) holder).mCiHead));

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
                        EyesInfo mEyesInfo=new EyesInfo();
                        EyesInfo.DataBean data = new EyesInfo.DataBean();
                        data.setPlayUrl(recInfo.getPlay_url());
                        data.setDuration(recInfo.getDuration());
                        data.setDescription(recInfo.getDescription());
                        data.setTitle(recInfo.getTitle());

                        EyesInfo.DataBean.CoverBean cover = new EyesInfo.DataBean.CoverBean();
                        EyesInfo.DataBean.AuthorBean authorBean = new EyesInfo.DataBean.AuthorBean();
                        cover.setDetail(recInfo.getCover());
                        data.setCover(cover);
                        data.setDate(recInfo.getData());
                        authorBean.setIcon(recInfo.getHeader());
                        authorBean.setDescription(recInfo.getAuthorDes());
                        authorBean.setName(recInfo.getName());
                        data.setAuthor(authorBean);
                        mEyesInfo.setData(data);
                        Intent intent = new Intent(context, VideoDetailPlayActivity.class);
                        intent.putExtra("eyesInfo",mEyesInfo);
                        intent.putExtra("next_url",recInfo.getNextUrl());
                        intent.putExtra("type",3);//小视频界面要用
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,((MHolder) holder).mImCover,"shareView").toBundle());
                        }else {
                            context.startActivity(intent);
                            ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
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
                            ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
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
                            ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                        }
                    }
                }
            });
            //share
            ((MHolder) holder).mImShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShareCallBack.onShareCallback(position);
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

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.ci_head)
        ImageView mCiHead;
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
