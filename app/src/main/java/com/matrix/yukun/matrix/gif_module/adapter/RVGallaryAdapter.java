package com.matrix.yukun.matrix.gif_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;

import java.io.File;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/2/14
 */
public class RVGallaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<String> mImageBeans;

    public RVGallaryAdapter(Context context, List<String> imageBeans) {
        mContext = context;
        mImageBeans = imageBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.image_gallary_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            Glide.with(mContext).load(mImageBeans.get(position)).into(((MyHolder) holder).mIvImage);
            ((MyHolder) holder).mIvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file=new File(mImageBeans.get(position));
                    file.delete();
                    mImageBeans.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mImageBeans.size() - position);
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
