package com.matrix.yukun.matrix.main_module.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.ImageData;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.List;

/**
 * author: kun .
 * date:   On 2018/12/28
 */
public class RVAvatarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<ImageData> mImageData;

    public RVAvatarAdapter(Context context, List<ImageData> mImageData) {
        mContext = context;
        this.mImageData = mImageData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.avator_image_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            ImageData data = mImageData.get(position);
            Glide.with(mContext).load(data.getUrl()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    mOnItemClickListener.onLoadError(position,data);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(((MyHolder) holder).mImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClickListener(position,data.getUrl());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImageData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        CardView cardView;
        public MyHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_cover);
            cardView = itemView.findViewById(R.id.card_view);
            ViewGroup.LayoutParams layoutParams=cardView.getLayoutParams();
            layoutParams.width= ScreenUtils.instance().getWidth(mContext)/4-ScreenUtils.dip2Px(4);
            layoutParams.height= ScreenUtils.instance().getWidth(mContext)/4-ScreenUtils.dip2Px(4);
            cardView.setLayoutParams(layoutParams);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClickListener(int pos,String url);
        void onLoadError(int pos,ImageData imageData);
    }
}
