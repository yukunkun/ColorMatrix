package com.matrix.yukun.matrix.gaia_module.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtil;

import java.util.List;

/**
 * Created by haiyang-lu on 16-3-28.
 * 作品池列表适配器
 */
public class WorkPoolAdapter extends RecyclerView.Adapter<WorkPoolAdapter.WorkViewHolder> {
    private Context mContext;
    private List<GaiaIndexBean> videoInfoList;
    private LayoutInflater inflater;

    public WorkPoolAdapter(Context context, List<GaiaIndexBean> videoInfoList) {
        this.mContext = context;
        this.videoInfoList = videoInfoList;
        //初始化inflater
        inflater = LayoutInflater.from(context);
    }

    int sreenWidth;


    private void setItemHeight(View itemView) {
        sreenWidth= ScreenUtil.screenWidth;
        ImageView itemImageView = (ImageView) itemView.findViewById(R.id.work_cover);
        ViewGroup.LayoutParams layoutParams = itemImageView.getLayoutParams();
        layoutParams.height = (int) (((sreenWidth - 30) / 2) * 0.562);//16:9
        itemImageView.setLayoutParams(layoutParams);
    }

    @Override
    public WorkPoolAdapter.WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_work_pool, null);
        setItemHeight(itemView);
        WorkViewHolder holder = new WorkViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(final WorkPoolAdapter.WorkViewHolder holder, int position) {
        final GaiaIndexBean videoInfo = videoInfoList.get(position);
            //设置封面
        String cover = videoInfo.getCover();
        String screenshot = videoInfo.getScreenshot();
        if (cover != null && !cover.isEmpty()&&!"null".equals(cover)) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, cover);
            cover = Api.COVER_PREFIX + cover/*.replace(".", "_18.")*/;
            Glide.with(mContext).load(cover).placeholder(R.mipmap.bg_header_nav).into(holder.workCover);
        } else if (screenshot != null && !screenshot.isEmpty()) {
//                ImageUtils.getInstance(mContext).getNetworkBitmap(holder.workCover, screenshot);
            if(videoInfo.getFlag()==1){
                screenshot = Api.COVER_PREFIX + screenshot+"_18.png";
            }else {
                screenshot = Api.COVER_PREFIX + screenshot.replace(".", "_18.");
            }
            Glide.with(mContext).load(screenshot).placeholder(R.mipmap.bg_header_nav).into(holder.workCover);
        }

        //用户头像
        if (videoInfo.getAvatar() != null && !videoInfo.getAvatar().equals("null")) {
            Glide.with(mContext).load(Api.COVER_PREFIX+videoInfo.getAvatar()).placeholder(R.drawable.head_2).into(holder.userAvatar);
        }

        //是否显示官方授权标志
        if (videoInfo.getIsOfficial() == 1) {
            holder.outh.setVisibility(View.VISIBLE);
        } else {
            holder.outh.setVisibility(View.GONE);
        }
        //是否是4k
        if (videoInfo.getIs4K() == 1) {
            holder.is4k.setVisibility(View.VISIBLE);
        } else {
            holder.is4k.setVisibility(View.GONE);
        }
        //播放时长
        holder.workDuration.setText(DataUtils.secToTime(videoInfo.getDuration()));
        //作品名称
        holder.workName.setText(videoInfo.getName());
        //播放次数
        holder.workCount.setText(String.valueOf(videoInfo.getPlayCount()));
        //评分
        holder.workGrade.setText(String.valueOf(videoInfo.getGrade()));
        //头像点击事件
        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailActivity.start(mContext,Api.COVER_PREFIX+videoInfo.getAvatar(),false);
            }
        });

        //用户名
        holder.userName.setText(videoInfo.getNickName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaiaPlayActivity.start(mContext,videoInfo.getId(), VideoType.WORK.getType());
            }
        });
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return videoInfoList.size();
    }


    public class WorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private ImageView workCover;
        private TextView workDuration;
        private ImageView outh;
        private ImageView is4k;
        private TextView workName;
        private TextView workCount;
        private TextView workGrade;
        private ImageView userAvatar;
        private TextView userName;
        private ImageView imageViewMore;

        public WorkViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            workCover = (ImageView) itemView.findViewById(R.id.work_cover);
            workDuration = (TextView) itemView.findViewById(R.id.work_duration);
            outh = (ImageView) itemView.findViewById(R.id.outh);
            is4k = (ImageView) itemView.findViewById(R.id.quality4);
            workName = (TextView) itemView.findViewById(R.id.work_name);
            workCount = (TextView) itemView.findViewById(R.id.work_count);
            workGrade = (TextView) itemView.findViewById(R.id.work_grade);
            userAvatar = (ImageView) itemView.findViewById(R.id.work_avatar);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            imageViewMore= (ImageView) itemView.findViewById(R.id.image_more);
            itemView.setOnClickListener(this);
            workName.setMaxLines(2);
            //设置标题的最大宽度
            workName.setMaxWidth((int) (((sreenWidth - 48) / 2) * 0.75));
        }

        @Override
        public void onClick(View v) {
            //点击事件
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition());
            }
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


}
