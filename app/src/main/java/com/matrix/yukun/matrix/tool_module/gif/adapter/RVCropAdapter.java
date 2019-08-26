package com.matrix.yukun.matrix.tool_module.gif.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.gif.bean.ImageBean;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/2/14
 */
public class RVCropAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<ImageBean> mImageBeans;

    public RVCropAdapter(Context context, List<ImageBean> imageBeans) {
        mContext = context;
        mImageBeans = imageBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.image_choose_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            Glide.with(mContext).load(mImageBeans.get(position).getPath()).into(((MyHolder) holder).mIvImage);
            if(mImageBeans.get(position).isChecked()){
                ((MyHolder) holder).mCheckBox.setImageResource(R.mipmap.icon_checkboxed);
            }else {
                ((MyHolder) holder).mCheckBox.setImageResource(R.mipmap.icon_checkbox);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mImageBeans.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mIvImage;
        ImageView mCheckBox;
        public MyHolder(View itemView) {
            super(itemView);
            mIvImage=itemView.findViewById(R.id.iv_image);
            mCheckBox=itemView.findViewById(R.id.cb_choose);
        }
    }

}
