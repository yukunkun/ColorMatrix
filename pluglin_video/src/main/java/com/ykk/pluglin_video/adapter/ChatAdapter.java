package com.ykk.pluglin_video.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.entity.ChatListInfo;


import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yukun on 17-5-11.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ChatListInfo> mChatInfos;

    public ChatAdapter(Context context, List<ChatListInfo> chatInfos) {
        mContext = context;
        mChatInfos = chatInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_left_item,null);
            return new LeftHolder(view);
        }else {
            view= LayoutInflater.from(mContext).inflate(R.layout.chat_right_item,null);
            return new RightHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof LeftHolder){
            //左边的布局
            if(mChatInfos.get(position).getIsShowTime()){
                SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");//获取当前时间
                String str = formatter.format(mChatInfos.get(position).getMsgTime());
                ((LeftHolder) holder).mTextViewLeftTime.setText(str);
                ((LeftHolder) holder).mTextViewLeftTime.setVisibility(View.VISIBLE);
            }else {
                ((LeftHolder) holder).mTextViewLeftTime.setVisibility(View.GONE);
            }
            ((LeftHolder) holder).mTextViewLeft.setText(mChatInfos.get(position).getChatInfo());
            Glide.with(mContext).load(mChatInfos.get(position).getBitmap()).placeholder(R.drawable.head_7).into(((LeftHolder) holder).mImageViewLeft);

        }else if(holder instanceof RightHolder){
            //右边的布局
            if(mChatInfos.get(position).getIsShowTime()){
                SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");//获取当前时间
                String str = formatter.format(mChatInfos.get(position).getMsgTime());
                ((RightHolder) holder).mTextViewRightTime.setText(str);
                ((RightHolder) holder).mTextViewRightTime.setVisibility(View.VISIBLE);
            }else {
                ((RightHolder) holder).mTextViewRightTime.setVisibility(View.GONE);
            }
            ((RightHolder) holder).mTextViewRight.setText(mChatInfos.get(position).getChatInfo());
            Glide.with(mContext).load(mChatInfos.get(position).getBitmap()).placeholder(R.drawable.head_2).into(((RightHolder) holder).mImageViewRight);
        }
    }

    @Override
    public int getItemCount() {
        return mChatInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatInfos.get(position).getType()==1){
            return 1;
        }else {
            return 2;
        }
    }

    class LeftHolder extends RecyclerView.ViewHolder{
        TextView mTextViewLeft;
        ImageView mImageViewLeft;
        TextView mTextViewLeftTime;
        public LeftHolder(View itemView) {
            super(itemView);
            mTextViewLeftTime= (TextView) itemView.findViewById(R.id.left_time);
            mTextViewLeft= (TextView) itemView.findViewById(R.id.tv_left_content);
            mImageViewLeft=(ImageView)itemView.findViewById(R.id.ci_left_head);
        }
    }

    class RightHolder extends RecyclerView.ViewHolder {
        TextView mTextViewRight;
        ImageView mImageViewRight;
        TextView mTextViewRightTime;

        public RightHolder(View itemView) {
            super(itemView);
            mTextViewRightTime= (TextView) itemView.findViewById(R.id.rigth_time);
            mTextViewRight = (TextView) itemView.findViewById(R.id.tv_right_content);
            mImageViewRight = (ImageView) itemView.findViewById(R.id.ci_right_head);
        }
    };
}
