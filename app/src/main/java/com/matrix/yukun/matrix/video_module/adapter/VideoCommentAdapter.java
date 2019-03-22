package com.matrix.yukun.matrix.video_module.adapter;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.video_module.entity.EyesInfo;
import com.matrix.yukun.matrix.video_module.entity.ImageInfo;
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class VideoCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<EyesInfo.DataBean.TagsBean> mTagsBeans;
    boolean isVertical=true;
    private int mWidth;
    ShareCallBack mShareCallBack;

    public VideoCommentAdapter(Context context, List<EyesInfo.DataBean.TagsBean> tagsBeans) {
        this.context = context;
        this.mTagsBeans = tagsBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MHolder) {
            EyesInfo.DataBean.TagsBean tagsBean = mTagsBeans.get(position);
            Glide.with(context).load(tagsBean.getHeaderImage()).into(((MHolder) holder).mCvHead);
            Glide.with(context).load(tagsBean.getBgPicture()).into(((MHolder) holder).mImageView);
            ((MHolder) holder).mTvName.setText(tagsBean.getName());
            ((MHolder) holder).mTvDes.setText((TextUtils.isEmpty(tagsBean.getDesc()))||tagsBean.getDesc().equals("null")?"不知道该说啥,大赞":tagsBean.getDesc().toString());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailActivity.start(context,mTagsBeans.get(position).getBgPicture(),false);
            }
        });
        ((MHolder) holder).mCvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailActivity.start(context,mTagsBeans.get(position).getHeaderImage(),false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTagsBeans.size();
    }

    class MHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.cv_header)
        CircleImageView mCvHead;
        @BindView(R2.id.tv_name)
        TextView mTvName;
        @BindView(R2.id.tv_des)
        TextView mTvDes;
        @BindView(R.id.iv_comment_bg)
        ImageView mImageView;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
