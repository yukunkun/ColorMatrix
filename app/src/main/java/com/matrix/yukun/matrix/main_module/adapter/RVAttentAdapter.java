package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.PersonActivity;
import com.matrix.yukun.matrix.main_module.entity.AttentList;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: kun .
 * date:   On 2019/1/17
 */
public class RVAttentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<AttentList> mAttentLists;

    public RVAttentAdapter(Context context, List<AttentList> attentLists) {
        mContext = context;
        mAttentLists = attentLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.attent_item_layout, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            AttentList attentList = mAttentLists.get(position);
            ((MyHolder) holder).mTvName.setText(attentList.getName());
            Glide.with(mContext).load(attentList.getHeader()).into(((MyHolder) holder).mCircleImageView);
            ((MyHolder) holder).mTvAttent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSupport.deleteAll(AttentList.class, "cover = ?", attentList.getCover());
                    mAttentLists.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EyesInfo mEyesInfo=new EyesInfo();
                    EyesInfo.DataBean data = new EyesInfo.DataBean();
                    data.setPlayUrl(attentList.getPlay_url());
                    data.setRemark(attentList.getName());
                    data.setDuration(attentList.getDuration());
                    data.setDescription(attentList.getDescription());
                    data.setTitle(attentList.getTitle());

                    EyesInfo.DataBean.CoverBean cover = new EyesInfo.DataBean.CoverBean();
                    EyesInfo.DataBean.AuthorBean authorBean = new EyesInfo.DataBean.AuthorBean();
                    cover.setDetail(attentList.getCover());
                    data.setCover(cover);
                    data.setDate(attentList.getData());
                    authorBean.setIcon(attentList.getHeader());
                    authorBean.setName(attentList.getName());
                    authorBean.setDescription(attentList.getAuthorDes());
                    data.setAuthor(authorBean);
                    mEyesInfo.setData(data);
                    Intent intent = new Intent(mContext, PersonActivity.class);
                    intent.putExtra("eyesInfo",mEyesInfo);
                    intent.putExtra("next_url",attentList.getNextUrl());
                    intent.putExtra("type",1);//小视频界面要用
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
                        mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,((MyHolder) holder).mCircleImageView,"shareView").toBundle());
                    }else {
                        mContext.startActivity(intent);
                        ((Activity)mContext).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAttentLists.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        CircleImageView mCircleImageView;
        TextView mTvAttent,mTvName;
        public MyHolder(View itemView) {
            super(itemView);
            mCircleImageView=itemView.findViewById(R.id.cv_header);
            mTvAttent=itemView.findViewById(R.id.tv_attent);
            mTvName=itemView.findViewById(R.id.tv_name);

        }
    }
}
