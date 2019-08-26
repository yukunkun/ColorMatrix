package com.matrix.yukun.matrix.tool_module.gif.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/2/14
 */
public class RVGifAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<String> mImageBeans;

    public RVGifAdapter(Context context, List<String> imageBeans) {
        mContext = context;
        mImageBeans = imageBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.image_gif_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){

            Bitmap bitmap= BitmapFactory.decodeFile(mImageBeans.get(position));
            ((MyHolder) holder).mIvImage.setImageBitmap(bitmap);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(mContext,mImageBeans.get(position),true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImageBeans.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mIvImage;
        ImageView mIvDel;
        public MyHolder(View itemView) {
            super(itemView);
            mIvImage=itemView.findViewById(R.id.iv_image);
            mIvDel=itemView.findViewById(R.id.iv_del);
        }
    }

}
