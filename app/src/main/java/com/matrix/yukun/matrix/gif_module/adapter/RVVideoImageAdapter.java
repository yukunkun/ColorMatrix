package com.matrix.yukun.matrix.gif_module.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gif_module.bean.VideoInfo;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/2/14
 */
public class RVVideoImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<VideoInfo> mImageBeans;
    int width;
    public RVVideoImageAdapter(Context context, List<VideoInfo> imageBeans) {
        mContext = context;
        mImageBeans = imageBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.image_video_item, null);
        return new MyHolder(inflate);
    }
    public void setImageWidth(int width){
        this.width=width;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            ((MyHolder) holder).mIvImage.setImageBitmap(mImageBeans.get(position).getBitmap());
            if(width!=0){
                ViewGroup.LayoutParams layoutParams = ((MyHolder) holder).mIvImage.getLayoutParams();
                layoutParams.width=width;
                ((MyHolder) holder).mIvImage.setLayoutParams(layoutParams);
                ((MyHolder) holder).mIvBg.setLayoutParams(layoutParams);
                ((MyHolder) holder).mTextView.setTextSize(10);
            }
            if(mImageBeans.get(position).isChooseStart()){
                ((MyHolder) holder).mLayout.setVisibility(View.VISIBLE);
                ((MyHolder) holder).mTextView.setText("开始位置");
            }else if(mImageBeans.get(position).isChooseEnd()){
                ((MyHolder) holder).mLayout.setVisibility(View.VISIBLE);
                ((MyHolder) holder).mTextView.setText("结束位置");
            }else if(mImageBeans.get(position).isChooseCenter()){
                ((MyHolder) holder).mLayout.setVisibility(View.VISIBLE);
                ((MyHolder) holder).mTextView.setText("已选");
            }else {
                ((MyHolder) holder).mLayout.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onCliclk(mImageBeans.get(position),position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImageBeans.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mIvImage,mIvBg;
        RelativeLayout mLayout;
        TextView mTextView;
        public MyHolder(View itemView) {
            super(itemView);
            mIvImage=itemView.findViewById(R.id.iv_image);
            mLayout=itemView.findViewById(R.id.rl_choose);
            mTextView=itemView.findViewById(R.id.tv_clip);
            mIvBg=itemView.findViewById(R.id.iv_cover_bg);
        }
    }

    OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener{
       void onCliclk(VideoInfo videoInfo,int position);
    }

}
