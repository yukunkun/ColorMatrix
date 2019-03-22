package com.matrix.yukun.matrix.chat_module.holderwrapper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.holder.RightImageHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightTextHolder;
import com.matrix.yukun.matrix.util.BitmapUtil;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.matrix.yukun.matrix.video_module.play.AboutUsActivity;
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;
import com.matrix.yukun.matrix.video_module.play.LoginActivity;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtils;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class RightImageHolderWrapper {
    static RightImageHolderWrapper mRightTextHolderWrapper;
    Context mContext;

    public static RightImageHolderWrapper getInstance(){
        if(mRightTextHolderWrapper==null){
            mRightTextHolderWrapper=new RightImageHolderWrapper();
        }
        return mRightTextHolderWrapper;
    }

    public void content(Context context, final ChatListInfo chatListInfo, RightImageHolder holder){
        this.mContext=context;
        if(chatListInfo.isShowTime()){
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");//获取当前时间
            String str = formatter.format(chatListInfo.getMsgTime());
            (holder).mTextViewRightTime.setText(str);
            (holder).mTextViewRightTime.setVisibility(View.VISIBLE);
        }else {
            ( holder).mTextViewRightTime.setVisibility(View.GONE);
        }
        reMesuerSize(holder.mIvRight,chatListInfo.getImagePath());
        Glide.with(mContext).load(chatListInfo.getImagePath()).into(holder.mIvRight);
        if(MyApplication.userInfo==null){
            Glide.with(mContext).load(chatListInfo.getBitmap()).placeholder(R.drawable.head_2).into((holder).mImageViewRight);
        }else {
            Glide.with(mContext).load(MyApplication.getUserInfo().getImg()).into((holder).mImageViewRight);
        }
        (holder).mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.userInfo!=null) {
                    Intent intent=new Intent(mContext, AboutUsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }else {
                    Intent intent=new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });
        (holder).mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatListInfo.getImagePath().endsWith(".gif")){
                    ImageDetailActivity.start(mContext,chatListInfo.getImagePath(),true);
                }else {
                    ImageDetailActivity.start(mContext,chatListInfo.getImagePath(),false);
                }
            }
        });
    }

    private void reMesuerSize(ImageView ivRight, String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ViewGroup.LayoutParams layoutParams=ivRight.getLayoutParams();
        if(bitmap.getHeight()>bitmap.getWidth()){
            layoutParams.width= ScreenUtils.instance().getWidth(mContext)/3;
            layoutParams.height= ScreenUtils.instance().getWidth(mContext)/2;
        }else {
            layoutParams.height= ScreenUtils.instance().getWidth(mContext)/3;
            layoutParams.width= ScreenUtils.instance().getWidth(mContext)/2;
        }
        ivRight.setLayoutParams(layoutParams);
    }
}
