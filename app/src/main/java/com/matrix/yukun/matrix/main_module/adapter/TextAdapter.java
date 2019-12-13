package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.TextDetailActivity;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.NewsInfo;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class TextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NewsInfo> jokeInfoList;
    List<Integer> mListHead=new ArrayList<>();
    private Random mRandom=new Random();

    public TextAdapter(Context context, List<NewsInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
        mListHead.add(R.drawable.head_1);
        mListHead.add(R.drawable.head_2);
        mListHead.add(R.drawable.head_3);
        mListHead.add(R.drawable.head_4);
        mListHead.add(R.drawable.head_5);
        mListHead.add(R.drawable.head_6);
        mListHead.add(R.drawable.head_7);
        mListHead.add(R.drawable.head_8);
        mListHead.add(R.drawable.head_9);
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
            ((MHolder) holder).mTvTitle.setText(textInfo.getTitle());

            Glide.with(context).load(textInfo.getImgsrc()).into(((MHolder) holder).mImCover);
            int headPos=mRandom.nextInt(9);
            GlideUtil.loadCircleImage(mListHead.get(headPos),(((MHolder) holder).mCiHead));
            if(textInfo.getSource()==null){
                ((MHolder) holder).mTvName.setText("佚名");
            }else {
                ((MHolder) holder).mTvName.setText(textInfo.getSource());
            }
            ((MHolder) holder).mTvTime.setText(textInfo.getMtime());
            ((MHolder) holder).mTvDes.setText(textInfo.getDigest());
            ((MHolder) holder).mTvForword.setText(textInfo.getTitle());
            ((MHolder) holder).mImCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TextDetailActivity.class);
                    intent.putExtra("url",textInfo.getUrl_3w());
                    intent.putExtra("title",textInfo.getTitle());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                }
            });
            ((MHolder) holder).mIvCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<CollectsInfo> newsList= DataSupport.where("cover = ?", textInfo.getImgsrc()+"").find(CollectsInfo.class);
                    if(newsList.size()>0){
                        //存储了
                        Toast.makeText(context, "已经添加到收藏了-_-", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        CollectsInfo collectInfo=new CollectsInfo();
                        if(textInfo.getImgsrc()!=null){
                            collectInfo.setHeader(textInfo.getImgsrc());
                            collectInfo.setCover(textInfo.getImgsrc());
                        }else {
                            collectInfo.setHeader("");
                            collectInfo.setCover("");
                        }
                        collectInfo.setTitle(textInfo.getTitle());
                        collectInfo.setName(textInfo.getSource());
                        collectInfo.setType(3);
                        collectInfo.setPlay_url(textInfo.getUrl_3w());
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
        @BindView(R.id.ci_head)
        CircleImageView mCiHead;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.im_cover)
        ImageView mImCover;
        @BindView(R.id.iv_collect)
        ImageView mIvCollect;
        @BindView(R.id.tv_des)
        TextView mTvDes;
        @BindView(R.id.tv_forword)
        TextView mTvForword;
        @BindView(R.id.delete)
        Button mButton;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
