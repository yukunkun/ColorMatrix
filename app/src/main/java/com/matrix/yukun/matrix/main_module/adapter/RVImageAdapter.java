package com.matrix.yukun.matrix.main_module.adapter;

import android.content.Context;
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
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.entity.ImageData;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.List;

/**
 * author: kun .
 * date:   On 2018/12/28
 */
public class RVImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<ImageData> mImageData;

    public RVImageAdapter(Context context, List<ImageData> mImageData) {
        mContext = context;
        this.mImageData = mImageData;
    }

    public void updateItem(int pos,ImageData imageData){
        mImageData.set(pos,imageData);
        notifyItemChanged(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.fragment_image_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            ImageData data = mImageData.get(position);
            Glide.with(mContext).load(data.getUrl()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    mOnClickItemListener.onLoadError(position,data);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(((MyHolder) holder).mImageView);

//            GlideUtil.loadImage(data.getUrl(),((MyHolder) holder).mImageView/*, GlideUtil.getErrorOptions(R.mipmap.icon_error,R.mipmap.icon_error,R.mipmap.icon_error)*/);
            if(data.isCollect()){
                ((MyHolder) holder).mIvColl.setImageResource(R.mipmap.collection_fill);
            }else {
                ((MyHolder) holder).mIvColl.setImageResource(R.mipmap.collection);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(mContext,data.getUrl(),((MyHolder) holder).mImageView,false);
                }
            });
            ((MyHolder) holder).mIvColl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.isCollect()){
                        data.setCollect(false);
                    }else {
                        data.setCollect(true);
                    }
                    mOnClickItemListener.onClickItemClick(position,data);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImageData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mImageView,mIvColl;
        public MyHolder(View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.iv_cover);
            mIvColl=itemView.findViewById(R.id.iv_collect);
//            ViewGroup.LayoutParams layoutParams=mImageView.getLayoutParams();
//            layoutParams.width= ScreenUtil.getDisplayWidth();
//            layoutParams.height= ScreenUtil.getDisplayHeight();
//            mImageView.setLayoutParams(layoutParams);
        }
    }
    OnClickItemListener mOnClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        mOnClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener{
        void onClickItemClick(int pos,ImageData imageData);
        void onLoadError(int pos,ImageData imageData);
    }
}
