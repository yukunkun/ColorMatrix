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
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.entity.CollectsInfo;
import com.ykk.pluglin_video.entity.EyesInfo;
import com.ykk.pluglin_video.video.VideoPlayActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Beck on 2018/4/14.
 */

public class EyeRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<EyesInfo> eyesInfoList;
    boolean isFist = false;
    public EyeRecAdapter(Context context, List<EyesInfo> eyesInfoList) {
        this.context = context;
        this.eyesInfoList = eyesInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.rec_header_layout, null);
            return new HeaderHolder(view);

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.rec_layout_item, null);
            return new MHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder){
            if (isFist || eyesInfoList.size() == 0) {
                return;
            }
            final List<EyesInfo> list = new ArrayList<>();
            list.add(eyesInfoList.get(3));
            if(eyesInfoList.get(5)!=null){
                list.add(eyesInfoList.get(5));
            }
            if(eyesInfoList.get(5)!=null){
                list.add(eyesInfoList.get(7));
            }
            if(eyesInfoList.get(8)!=null){
                list.add(eyesInfoList.get(8));
            }

            ((HeaderHolder) holder).mConBanner.setPages(
                    new CBViewHolderCreator<LocalImageHolderView>() {
                        @Override
                        public LocalImageHolderView createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, list)
                    .startTurning(3000)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicatored})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
            ;
            ((HeaderHolder) holder).mConBanner.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("imagepath", list.get(position).getData().getPlayUrl() + "#" +
                            list.get(position).getData().getCover().getDetail() + "#" +
                            list.get(position).getData().getTitle());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            isFist = true;

            ((HeaderHolder) holder).mTvVideo.setOnClickListener(new Listener());
            ((HeaderHolder) holder).mTvImage.setOnClickListener(new Listener());
            ((HeaderHolder) holder).mTvJoke.setOnClickListener(new Listener());
            ((HeaderHolder) holder).mTvTxt.setOnClickListener(new Listener());
            ((HeaderHolder) holder).mTvEssay.setOnClickListener(new Listener());
        }else if(holder instanceof MHolder){
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
                    intent.putExtra("imagepath", eyesInfo.getData().getPlayUrl() + "#" + eyesInfo.getData().getCover().getDetail() + "#" + eyesInfo.getData().getTitle());
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

            } else if (i == R.id.tv_image) {
                mCategroyCallBack.choosePos(2);

            } else if (i == R.id.tv_joke) {
                mCategroyCallBack.choosePos(3);

            } else if (i == R.id.tv_essay) {
                mCategroyCallBack.choosePos(4);

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
