package com.matrix.yukun.matrix.chat_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;

import java.util.List;

/**
 * Created by yukun on 17-5-11.
 */
public class ChatPictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Photo> imgList;
    int mHeight;
    int mMaxWidth;

    public ChatPictureAdapter(Context context,  List<Photo> imgList) {
        mContext = context;
        this.imgList = imgList;
        mHeight = ScreenUtil.dip2px(330);
        mMaxWidth = ScreenUtil.dip2px(400);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(mContext).inflate(R.layout.chat_select_picture,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Photo photo = imgList.get(position);
        if(holder instanceof MyHolder){
            Glide.with(mContext).load(photo.path).into(((MyHolder) holder).mImageView);
            if (photo.getHeight() == 0) {
                photo.setHeight(mHeight);
                photo.setWidth(mHeight);
            }
            else {
                int width = mHeight * photo.getWidth() / photo.getHeight();
                photo.setWidth(Math.min(width, mMaxWidth));
                photo.setHeight(mHeight);
            }
            ViewGroup.LayoutParams layoutParams = ((MyHolder) holder).mImageView.getLayoutParams();
            layoutParams.width=photo.width/2;
            ((MyHolder) holder).mImageView.setLayoutParams(layoutParams);
            if(!photo.selected){
                ((MyHolder) holder).mCheckBox.setImageResource(R.mipmap.icon_checkbox);
            }else {
                ((MyHolder) holder).mCheckBox.setImageResource(R.mipmap.icon_checkboxed);
            }
            ((MyHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        ImageView mCheckBox;
        public MyHolder(View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.image_select_item);
            mCheckBox=itemView.findViewById(R.id.pv_cb);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

}
