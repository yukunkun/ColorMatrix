package com.matrix.yukun.matrix.chat_module.holderwrapper;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.holder.RightTextHolder;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.main_module.activity.PersonCenterActivity;
import com.matrix.yukun.matrix.main_module.activity.JokeDetailActivity;
import com.matrix.yukun.matrix.main_module.activity.LoginActivity;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.text.SimpleDateFormat;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class RightTextHolderWrapper {
    static RightTextHolderWrapper mRightTextHolderWrapper;
    Context mContext;

    public static RightTextHolderWrapper getInstance(){
        if(mRightTextHolderWrapper==null){
            mRightTextHolderWrapper=new RightTextHolderWrapper();
        }
        return mRightTextHolderWrapper;
    }

    public void content(Context context, final ChatListInfo chatListInfo, final RightTextHolder holder){
        this.mContext=context;
        if(chatListInfo.isShowTime()){
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");//获取当前时间
            String str = formatter.format(chatListInfo.getMsgTime());
            (holder).mTextViewRightTime.setText(str);
            (holder).mTextViewRightTime.setVisibility(View.VISIBLE);
        }else {
            ( holder).mTextViewRightTime.setVisibility(View.GONE);
        }
        (holder).mTextViewRight.setText(chatListInfo.getChatInfo());
        if(MyApp.userInfo==null){
            GlideUtil.loadOptionsImage("",(holder).mImageViewRight,GlideUtil.getOptions(R.drawable.head_2));
        }else {
            Glide.with(mContext).load(MyApp.getUserInfo().getAvator()).into((holder).mImageViewRight);
        }
        holder.mTextViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JokeDetailActivity.start(mContext,chatListInfo.getChatInfo(),(holder).mTextViewRight);
            }
        });

        (holder).mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApp.userInfo!=null) {
                    Intent intent=new Intent(mContext, PersonCenterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }else {
                    Intent intent=new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

    }
}
