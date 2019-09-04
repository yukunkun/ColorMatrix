package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: kun .
 * date:   On 2018/12/28
 */
public class RVVerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    ArrayList<PlayAllBean> mPlayAllBeans;
    int selectPosition=0;
    OnItemViewClickListener mOnItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        mOnItemViewClickListener = onItemViewClickListener;
    }

    public RVVerticalAdapter(Context context, ArrayList<PlayAllBean> playAllBeans) {
        mContext = context;
        mPlayAllBeans=playAllBeans;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.vertical_video_item, null);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHolder){
            PlayAllBean playAllBean = mPlayAllBeans.get(position);
            ((MyHolder) holder).mTvTitle.setText(TextUtils.isEmpty(playAllBean.getText())?"":playAllBean.getText());
            ((MyHolder) holder).mTvName.setText(playAllBean.getUsername());
            ((MyHolder) holder).mTvLike.setText(playAllBean.getDown()+"");
            ((MyHolder) holder).mTvComment.setText(playAllBean.getComment()+"");
            Glide.with(mContext).load(playAllBean.getHeader()).into(((MyHolder) holder).mCVHeader);
            if(selectPosition==position){
                //隐藏
                ((MyHolder) holder).mImageView.setVisibility(View.GONE);
                ((MyHolder) holder).mIvCoverBg.setVisibility(View.GONE);
            }else {
                //显示
                Glide.with(mContext).load(playAllBean.getThumbnail()).into(((MyHolder) holder).mImageView);
                GlideUtil.loadBlurImage(playAllBean.getThumbnail(),((MyHolder) holder).mIvCoverBg);
                //高斯模糊
//                Glide.with(mContext).load(playAllBean.getThumbnail()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        Blurry.with(mContext).sampling(1).from(resource).into(((MyHolder) holder).mIvCoverBg);
//                    }
//                });
                ((MyHolder) holder).mImageView.setVisibility(View.VISIBLE);
                ((MyHolder) holder).mIvCoverBg.setVisibility(View.VISIBLE);
            }

            ((MyHolder) holder).mIvBack.setOnClickListener(new Listener(position,playAllBean));
            ((MyHolder) holder).mRLComment.setOnClickListener(new Listener(position,playAllBean));
            ((MyHolder) holder).mRLLike.setOnClickListener(new Listener(position,playAllBean));
            ((MyHolder) holder).mIvMore.setOnClickListener(new Listener(position,playAllBean));

        }
    }

    @Override
    public int getItemCount() {
        return mPlayAllBeans.size();
    }

    class Listener implements View.OnClickListener {
        int position;
        PlayAllBean mPlayAllBean;
        public Listener(int position, PlayAllBean playAllBean) {
            this.position = position;
            mPlayAllBean = playAllBean;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back:
                    ((Activity)mContext).finish();
                    break;
                case R.id.iv_more:
                    mOnItemViewClickListener.onMoreClick(position,mPlayAllBean);
                    break;
                case R.id.rl_comment:
                    mOnItemViewClickListener.onCommentClick(position,mPlayAllBean);
                    break;
                case R.id.rl_like:
                    mOnItemViewClickListener.onLikeClick(position,mPlayAllBean);
                    break;
            }
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mImageView,mIvMore,mIvBack,mIvCoverBg;
        RelativeLayout mRlRoot;
        CircleImageView mCVHeader;
        TextView mTvComment,mTvLike,mTvTitle,mTvName;
        RelativeLayout mRLLike,mRLComment;
        public MyHolder(View itemView) {
            super(itemView);
            mIvBack=itemView.findViewById(R.id.iv_back);
            mImageView=itemView.findViewById(R.id.iv_cover);
            mRlRoot=itemView.findViewById(R.id.rl_root);
            mIvMore=itemView.findViewById(R.id.iv_more);
            mCVHeader=itemView.findViewById(R.id.cv_header);
            mTvComment=itemView.findViewById(R.id.tv_comment);
            mTvLike=itemView.findViewById(R.id.tv_like);
            mTvTitle=itemView.findViewById(R.id.tv_title);
            mTvName=itemView.findViewById(R.id.tv_name);
            mRLLike=itemView.findViewById(R.id.rl_like);
            mIvCoverBg=itemView.findViewById(R.id.iv_cover_bg);
            mRLComment=itemView.findViewById(R.id.rl_comment);
            ViewGroup.LayoutParams layoutParams = mRlRoot.getLayoutParams();
            int height = ScreenUtils.instance().getHeight(mContext);
            layoutParams.height= height;
            mRlRoot.setLayoutParams(layoutParams);

        }
    }

    public interface OnItemViewClickListener{
        void onMoreClick(int position, PlayAllBean playAllBean);
        void onLikeClick(int position, PlayAllBean playAllBean);
        void onCommentClick(int position, PlayAllBean playAllBean);
    }
}
