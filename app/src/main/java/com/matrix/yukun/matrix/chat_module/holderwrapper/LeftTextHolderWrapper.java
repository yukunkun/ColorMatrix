package com.matrix.yukun.matrix.chat_module.holderwrapper;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.holder.LeftTextHolder;
import com.matrix.yukun.matrix.main_module.activity.JokeDetailActivity;

import java.text.SimpleDateFormat;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class LeftTextHolderWrapper {

    static LeftTextHolderWrapper mLeftTextHolderWrapper;
    Context mContext;
    public static LeftTextHolderWrapper getInstance(){
        if(mLeftTextHolderWrapper==null){
            mLeftTextHolderWrapper=new LeftTextHolderWrapper();
        }
        return mLeftTextHolderWrapper;
    }

    public void content(Context context, final ChatListInfo chatListInfo, final LeftTextHolder holder){
        this.mContext=context;
        if(chatListInfo.isShowTime()){
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");//获取当前时间
            String str = formatter.format(chatListInfo.getMsgTime());
            (holder).mTextViewLeftTime.setText(str);
            (holder).mTextViewLeftTime.setVisibility(View.VISIBLE);
        }else {
            (holder).mTextViewLeftTime.setVisibility(View.GONE);
        }
        (holder).mTextViewLeft.setText(chatListInfo.getChatInfo());
        holder.mTextViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JokeDetailActivity.start(mContext,chatListInfo.getChatInfo(),(holder).mTextViewLeft);
            }
        });
        Glide.with(context).load(chatListInfo.getBitmap()).placeholder(R.drawable.head_7).into((holder).mImageViewLeft);
    }
}
