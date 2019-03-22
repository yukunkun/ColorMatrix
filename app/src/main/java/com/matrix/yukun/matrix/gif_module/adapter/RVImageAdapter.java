package com.matrix.yukun.matrix.gif_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gif_module.bean.ImageBean;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/2/14
 */
public class RVImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<ImageBean> mImageBeans;
    int count=-1;
    int currentCount;
    OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public RVImageAdapter(Context context, List<ImageBean> imageBeans) {
        mContext = context;
        mImageBeans = imageBeans;
    }

    public void setCount(int count){
        this.count=count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.image_choose_item, null);
        return new MyHolder(inflate);
    }

    public void update(List<ImageBean> mImageBeans){
        currentCount=0;
        this.mImageBeans=mImageBeans;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            Glide.with(mContext).load(mImageBeans.get(position).getPath()).into(((MyHolder) holder).mIvImage);
            if(mImageBeans.get(position).isChecked()){
                ((MyHolder) holder).mCheckBox.setImageResource(R.mipmap.icon_checkboxed);
                currentCount++;
            }else {
                ((MyHolder) holder).mCheckBox.setImageResource(R.mipmap.icon_checkbox);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mImageBeans.get(position).isChecked()){ //未点击
                        if(count!=-1){
                            if(count<=currentCount){
                                ToastUtils.showToast("超过做大限制了");
                                mOnClickListener.onClickCheckBoxListener(position,false);
                                return;
                            }else {
                                currentCount++;
                                mImageBeans.get(position).setChecked(true);
                                mOnClickListener.onClickCheckBoxListener(position,true);
                            }
                        }else {
                            currentCount++;
                                mImageBeans.get(position).setChecked(true);
                            mOnClickListener.onClickCheckBoxListener(position,true);
                        }
                    }else { //取消
                        currentCount--;
                        mImageBeans.get(position).setChecked(false);
                        mOnClickListener.onClickCheckBoxListener(position,false);
                    }
                //返回数据
                    mOnClickListener.onClickListener(getChooseList());
                }
            });
        }
    }

    public List<String> getChooseList(){
        List<String> mList=new ArrayList<>();
        for (ImageBean imageBean:mImageBeans) {
            if(imageBean.isChecked()){
                mList.add(imageBean.getPath());
            }
        }
        return mList;
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
    public interface OnClickListener{
        void onClickListener(List<String> list);
        void onClickCheckBoxListener(int pos,boolean isCheck);
    }
}
