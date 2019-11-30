package com.matrix.yukun.matrix.main_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.ImageData;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import org.litepal.crud.DataSupport;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.fragment_image_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
             ImageData data = mImageData.get(position);
            GlideUtil.loadImage(data.getUrl(),((MyHolder) holder).mImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(mContext,data.getUrl(),((MyHolder) holder).mImageView,false);
                }
            });
            ((MyHolder) holder).mIvColl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CollectsInfo collectInfo=new CollectsInfo();
                    List<CollectsInfo> newsList = DataSupport.where("cover = ?", data.getUrl()).find(CollectsInfo.class);
                    if(newsList.size()>0){
                        ToastUtils.showToast("已经收藏成功了");
                        return;
                    }
                    collectInfo.setHeader(data.getUrl());
                    collectInfo.setCover(data.getUrl());
                    collectInfo.setTitle("佚名");
                    collectInfo.setName("佚名");
                    collectInfo.setType(2);
                    collectInfo.setPlay_url(data.getUrl());
                    collectInfo.setGif(false);
                    collectInfo.save();
                    Toast.makeText(mContext, "添加到收藏成功", Toast.LENGTH_SHORT).show();
                    ((MyHolder) holder).mIvColl.setImageResource(R.mipmap.collection_fill);
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
}
