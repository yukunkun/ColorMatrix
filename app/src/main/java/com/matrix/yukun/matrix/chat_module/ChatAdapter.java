package com.matrix.yukun.matrix.chat_module;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yukun on 17-5-11.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ChatListInfo>  mChatInfos;
    private Toast mToastStart;

    public ChatAdapter(Context context, List<ChatListInfo> chatInfos) {
        mContext = context;
        mChatInfos = chatInfos;
    }

    public void cancenToast(){
        if(mToastStart!=null)
        mToastStart.cancel();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_left_item,null);
            return new LeftHolder(view);
        }else {
            view=LayoutInflater.from(mContext).inflate(R.layout.chat_right_item,null);
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
            Glide.with(mContext).load(mChatInfos.get(position).getBitmap()).placeholder(R.mipmap.icons).into(((LeftHolder) holder).mImageViewLeft);
            ((LeftHolder) holder).mImageViewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToast(mContext,R.mipmap.icons);
                }
            });

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
            Glide.with(mContext).load(mChatInfos.get(position).getBitmap()).placeholder(R.mipmap.icon_ai).into(((RightHolder) holder).mImageViewRight);

            ((RightHolder) holder).mImageViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToast(mContext,R.mipmap.icon_ai);
                }
            });
        }
    }

    private void setToast(Context context, int bitmap) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_item, null);
        //初始化布局控件
        ImageView mImageView = (ImageView) toastRoot.findViewById(R.id.im_toast);
        //为控件设置属性
        mImageView.setImageResource(bitmap);
        //Toast的初始化
        mToastStart = new Toast(context);
        mToastStart.setGravity(Gravity.CENTER, 0, 0);
        mToastStart.setDuration(Toast.LENGTH_SHORT);
        mToastStart.setView(toastRoot);
        mToastStart.show();
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
    };
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
