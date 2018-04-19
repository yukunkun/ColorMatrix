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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.entity.CollectsInfo;
import com.ykk.pluglin_video.entity.EyesInfo;
import com.ykk.pluglin_video.video.VideoPlayActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<EyesInfo> eyesInfoList;
    boolean isFist = false;
    boolean isVertical=true;
    public VideoAdapter(Context context, List<EyesInfo> eyesInfoList) {
        this.context = context;
        this.eyesInfoList = eyesInfoList;
    }

    public void setTextViewWidth( boolean isVertical){
        this.isVertical=isVertical;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.rec_layout_item, null);
        return new MHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MHolder){
            final EyesInfo eyesInfo = eyesInfoList.get(position);
            ((MHolder) holder).mTvName.setText(eyesInfo.getData().getSlogan());
            ((MHolder) holder).mTvTitle.setText(eyesInfo.getData().getDescription());
            Glide.with(context).load(eyesInfo.getData().getCover().getDetail()).into(((MHolder) holder).mImCover);
            Glide.with(context).load(eyesInfo.getData().getAuthor().getIcon()).into(((MHolder) holder).mCiHead);
            ((MHolder) holder).mTvPlayTimes.setText(eyesInfo.getData().getCategory());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("imagepath", eyesInfo.getData().getPlayUrl() + "#" + eyesInfo.getData().getCover(). getDetail() + "#" + eyesInfo.getData().getTitle());
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
                    shareSend(context, eyesInfo.getData().getWebUrl().getForWeibo());
                }
            });
            //加入收藏
            ((MHolder) holder).mImCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<CollectsInfo> newsList = DataSupport.where("cover = ?", eyesInfo.getData().getCover().getDetail()).find(CollectsInfo.class);
                    if (newsList.size() > 0) {
                        Toast.makeText(context, "已经添加到收藏了-_-", Toast.LENGTH_SHORT).show();
                        //存储了
                        return;
                    } else {
                        CollectsInfo collectInfo = new CollectsInfo();
                        collectInfo.setHeader(eyesInfo.getData().getAuthor().getIcon());
                        collectInfo.setCover(eyesInfo.getData().getCover().getDetail());
                        collectInfo.setTitle(eyesInfo.getData().getTitle());
                        collectInfo.setName(eyesInfo.getData().getSlogan());
                        collectInfo.setType(1);
                        collectInfo.setPlay_url(eyesInfo.getData().getPlayUrl());
                        collectInfo.save();
                        Toast.makeText(context, "添加到收藏成功", Toast.LENGTH_SHORT).show();
                        ((MHolder) holder).mImCollect.setImageResource(R.mipmap.collection_fill);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return eyesInfoList.size();
    }
    private void shareSend(Context context, String str) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, str);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, ""));
    }

    RecAdapter.CategroyCallBack mCategroyCallBack;
    public void getCategroyPos(RecAdapter.CategroyCallBack mCategroyCallBack){
        this.mCategroyCallBack=mCategroyCallBack;
    }
    public interface CategroyCallBack{
        void choosePos(int pos);
    }


    class Listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.tv_video) {
                mCategroyCallBack.choosePos(1);

            } else if (i == R.id.tv_txt) {
                mCategroyCallBack.choosePos(2);

            } else if (i == R.id.tv_image) {
                mCategroyCallBack.choosePos(3);

            } else if (i == R.id.tv_joke) {
                mCategroyCallBack.choosePos(4);

            } else if (i == R.id.tv_essay) {
                mCategroyCallBack.choosePos(5);

            }
        }
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
        @BindView(R2.id.ll)
        RelativeLayout linearLayout;

        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.con_banner)
        ConvenientBanner mConBanner;
        @BindView(R2.id.tv_video)
        TextView mTvVideo;
        @BindView(R2.id.tv_image)
        TextView mTvImage;
        @BindView(R2.id.tv_joke)
        TextView mTvJoke;
        @BindView(R2.id.tv_txt)
        TextView mTvTxt;
        @BindView(R2.id.tv_essay)
        TextView mTvEssay;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class LocalImageHolderView implements Holder<EyesInfo> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, EyesInfo data) {
            Glide.with(context).load(data.getData().getCover().getDetail()).into(imageView);
        }
    }
}
