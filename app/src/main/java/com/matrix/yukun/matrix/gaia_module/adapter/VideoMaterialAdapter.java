package com.matrix.yukun.matrix.gaia_module.adapter;

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
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtil;

import java.util.List;

/**
 * Created by haiyang-lu on 16-3-28.
 * 作品池列表适配器
 */
public class VideoMaterialAdapter extends RecyclerView.Adapter<VideoMaterialAdapter.WorkViewHolder> {
    private Context mContext;
    private List<GaiaIndexBean> materialInfos;
    private LayoutInflater inflater;

    public VideoMaterialAdapter(Context context, List<GaiaIndexBean> materialInfos) {
        this.mContext = context;
        this.materialInfos = materialInfos;
        //初始化inflater
        inflater = LayoutInflater.from(context);
        //获取屏幕的当前高宽
        getScreeWidth();
    }

    int sreenWidth;

    private void getScreeWidth() {
        sreenWidth = ScreenUtil.screenWidth;
    }

    private void setItemHeight(View itemView) {
        ImageView itemImageView = (ImageView) itemView.findViewById(R.id.work_cover);
        ViewGroup.LayoutParams layoutParams = itemImageView.getLayoutParams();
        layoutParams.height = (int) (((sreenWidth - 30) / 2) * 0.562);//16:9
        itemImageView.setLayoutParams(layoutParams);
    }

    @Override
    public VideoMaterialAdapter.WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_material_video, null);
        setItemHeight(itemView);
        WorkViewHolder holder = new WorkViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(final VideoMaterialAdapter.WorkViewHolder holder, int position) {
        final GaiaIndexBean videoInfo = materialInfos.get(position);
            //设置封面
        String cover = videoInfo.getCover();
        String screenshot = videoInfo.getScreenshot();

        if (cover != null && !cover.isEmpty()) {
            Glide.with(mContext).load(Api.COVER_PREFIX+cover).placeholder(R.mipmap.bg_header_nav).into(holder.workCover);
        } else if (screenshot != null && !screenshot.isEmpty()) {
            if(videoInfo.getFlag()==1){
                screenshot = Api.COVER_PREFIX +screenshot+"_18.png";
                Glide.with(mContext).load(screenshot).placeholder(R.mipmap.bg_header_nav).into(holder.workCover);
            }
        }
//        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageDetailActivity.start(mContext,Api.COVER_PREFIX+videoInfo.getAvatar(),false);
//            }
//        });

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

        holder.workCount.setText("播放·"+videoInfo.getPlayCount());
        //播放时长
        holder.workDuration.setText(DataUtils.secToTime(videoInfo.getDuration()));
        //作品名称
        holder.workName.setText(videoInfo.getName());
        holder.mTextViewPrice.setText("免费");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaiaPlayActivity.start(mContext,videoInfo.getId(), VideoType.MATERIAL.getType());
            }
        });
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return materialInfos.size();
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
        private TextView mTextViewPrice;

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
            mTextViewPrice= (TextView) itemView.findViewById(R.id.material_price);
            itemView.setOnClickListener(this);
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
