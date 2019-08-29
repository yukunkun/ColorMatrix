package com.matrix.yukun.matrix.gaia_module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaPlayActivity;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.bean.VideoType;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/7/10
 */
public class SearchWorkAdapter extends RecyclerView.Adapter<SearchWorkAdapter.MViewHolder> {

    Context mContext;
    List<GaiaIndexBean> mGaiaIndexBeans;
    int mType;

    public SearchWorkAdapter(Context context, List<GaiaIndexBean> gaiaIndexBeans, int type) {
        mContext = context;
        mGaiaIndexBeans = gaiaIndexBeans;
        mType=type;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_search_work, null);
        MViewHolder holder = new MViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MViewHolder holder, int position) {
        final GaiaIndexBean searchWorksResult = mGaiaIndexBeans.get(position);
        //封面
        String cover = searchWorksResult.getCover();
        String screenShot = searchWorksResult.getScreenshot();

        if(cover!=null&&!cover.isEmpty()&&!"null".equals(cover)) {
            cover = Api.COVER_PREFIX + cover/*.replace(".", "_18.")*/;
            GlideUtil.loadImage(cover,holder.workCover);
        } else if (screenShot!=null){
            if(searchWorksResult.getFlag()==0){
                screenShot = Api.COVER_PREFIX + screenShot.replace(".", "_18.");
            }else if(searchWorksResult.getFlag()==1){
                screenShot= Api.COVER_PREFIX+screenShot+"_18.png";
            }
            GlideUtil.loadImage(screenShot,holder.workCover);
        }
        //标题
        holder.workTitle.setText(searchWorksResult.getName());
        //作者
        holder.workAuthor.setText("by" + searchWorksResult.getNickName());
        //播放量
        holder.workPlayCountColCount.setText(searchWorksResult.getPlayCount() + "播放·" + searchWorksResult.getLikeCount() + "收藏");
        //作品时长
        holder.workDuration.setText(DataUtils.secToTime(searchWorksResult.getDuration()));
        //创建时间
        if(searchWorksResult.getTime()!=null&&searchWorksResult.getTime().getTime()!=0){
            holder.workCreateTime.setText(DataUtils.getGaiaTime(searchWorksResult.getTime().getTime()));
        }
        //点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cover="";
                GaiaIndexBean item = mGaiaIndexBeans.get(position);
                if(item.getCover()!=null&&!item.getCover().isEmpty()&&!"null".equals(item.getCover())){
                    cover= Api.COVER_PREFIX+item.getCover();
                }else if(!TextUtils.isEmpty(item.getScreenshot())){
                    if(item.getFlag()==1){
                        cover= Api.COVER_PREFIX+item.getScreenshot()+"_18.png";
                    }else if(item.getFlag()==0){
                        cover= Api.COVER_PREFIX+item.getScreenshot().replace(".","_18.");
                    }
                }
                if(mType==0){
                    GaiaPlayActivity.start(mContext,searchWorksResult.getId(),VideoType.WORK.getType(),cover);
                }else {
                    GaiaPlayActivity.start(mContext,searchWorksResult.getId(), VideoType.MATERIAL.getType(),cover);                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGaiaIndexBeans.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView workCover;
        private TextView workTitle;
        private TextView workAuthor;
        private TextView workPlayCountColCount;
        private TextView workDuration;
        private TextView workCreateTime;

        public MViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            workCover = (ImageView) itemView.findViewById(R.id.work_cover);
            workTitle = (TextView) itemView.findViewById(R.id.work_title);
            workAuthor = (TextView) itemView.findViewById(R.id.work_author);
            workPlayCountColCount = (TextView) itemView.findViewById(R.id.work_playcount_collcount);
            workDuration = (TextView) itemView.findViewById(R.id.work_duration);
            workCreateTime = (TextView) itemView.findViewById(R.id.work_create_time);
        }
    }
}



