package com.matrix.yukun.matrix.chat_module.holderwrapper;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.holder.ShakeHolder;


/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class ShakeHolderWrapper {

    static ShakeHolderWrapper mShakeHolderWrapper;
    Context mContext;
    public static ShakeHolderWrapper getInstance(){
        if(mShakeHolderWrapper==null){
            mShakeHolderWrapper=new ShakeHolderWrapper();
        }
        return mShakeHolderWrapper;
    }

    public void content(Context context, ChatListInfo chatListInfo, ShakeHolder holder){
        this.mContext=context;
        Glide.with(context).load(chatListInfo.getBitmap()).placeholder(R.drawable.head_7).into((holder).mCircleImageView);
//        holder.mTextView.setText("抖了你一下");
    }
}
