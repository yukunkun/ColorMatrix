package com.matrix.yukun.matrix.video_module.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.video_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.video_module.entity.NewsInfo;
import com.matrix.yukun.matrix.video_module.play.TextDetailActivity;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class TextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NewsInfo> jokeInfoList;



    public TextAdapter(Context context, List<NewsInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_layout_item, null);
        return new MHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MHolder) {
            final NewsInfo textInfo = jokeInfoList.get(position);
            ((MHolder) holder).mTvTitle.setText(textInfo.getCategory());
            if(textInfo.getPicInfo()!=null&&textInfo.getPicInfo().size()>0){
                Glide.with(context).load(textInfo.getPicInfo().get(0).getUrl()+"").into(((MHolder) holder).mImCover);
            }
            if(textInfo.getSource()==null){
                ((MHolder) holder).mTvName.setText("佚名");
            }else {
                ((MHolder) holder).mTvName.setText(textInfo.getSource());
            }
            Glide.with(context).load("").placeholder(R.drawable.head_2).into(((MHolder) holder).mCiHead);
            ((MHolder) holder).mTvDes.setText(textInfo.getDigest());
            ((MHolder) holder).mTvForword.setText(textInfo.getTitle());

            ((MHolder) holder).mImCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TextDetailActivity.class);
                    intent.putExtra("url",textInfo.getLink());
                    intent.putExtra("title",textInfo.getTitle());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
                }
            });
            ((MHolder) holder).mIvCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<CollectsInfo> newsList= DataSupport.where("cover = ?", textInfo.getPicInfo().get(0).getUrl()+"").find(CollectsInfo.class);
                    if(newsList.size()>0){
                        //存储了
                        Toast.makeText(context, "已经添加到收藏了-_-", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        CollectsInfo collectInfo=new CollectsInfo();
                        if(textInfo.getPicInfo()!=null&&textInfo.getPicInfo().size()>0){
                            collectInfo.setHeader(textInfo.getPicInfo().get(0).getUrl());
                            collectInfo.setCover(textInfo.getPicInfo().get(0).getUrl());
                        }else {
                            collectInfo.setHeader("");
                            collectInfo.setCover("");
                        }
                        collectInfo.setTitle(textInfo.getTitle());
                        collectInfo.setName(textInfo.getSource());
                        collectInfo.setType(3);
                        collectInfo.setPlay_url(textInfo.getLink());
                        collectInfo.save();
                        Toast.makeText(context, "添加到收藏成功", Toast.LENGTH_SHORT).show();
                        ((MHolder) holder).mIvCollect.setImageResource(R.mipmap.collection_fill);
                    }
                }
            });
            ((MHolder) holder).mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(position);
                    jokeInfoList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
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
        @BindView(R2.id.tv_title)
        TextView mTvTitle;
        @BindView(R2.id.im_cover)
        ImageView mImCover;
        @BindView(R2.id.iv_collect)
        ImageView mIvCollect;
        @BindView(R2.id.tv_des)
        TextView mTvDes;
        @BindView(R2.id.tv_forword)
        TextView mTvForword;
        @BindView(R2.id.delete)
        Button mButton;

        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
