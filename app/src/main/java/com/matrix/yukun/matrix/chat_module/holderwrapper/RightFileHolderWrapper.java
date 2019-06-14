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
import com.matrix.yukun.matrix.chat_module.holder.RightFileHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightVideoHolder;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.matrix.yukun.matrix.video_module.play.AboutUsActivity;
import com.matrix.yukun.matrix.video_module.play.LoginActivity;
import com.matrix.yukun.matrix.video_module.utils.FileUtils;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.video_module.video.VideoPlayActivity;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class RightFileHolderWrapper {
    static RightFileHolderWrapper mRightTextHolderWrapper;
    Context mContext;

    public static RightFileHolderWrapper getInstance(){
        if(mRightTextHolderWrapper==null){
            mRightTextHolderWrapper=new RightFileHolderWrapper();
        }
        return mRightTextHolderWrapper;
    }

    public void content(final Context context, final ChatListInfo chatListInfo, RightFileHolder holder){
        this.mContext=context;
        if(chatListInfo.isShowTime()){
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");//获取当前时间
            String str = formatter.format(chatListInfo.getMsgTime());
            (holder).mTextViewRightTime.setText(str);
            (holder).mTextViewRightTime.setVisibility(View.VISIBLE);
        }else {
            ( holder).mTextViewRightTime.setVisibility(View.GONE);
        }
        if(MyApplication.userInfo==null){
            Glide.with(mContext).load(chatListInfo.getBitmap()).placeholder(R.drawable.head_2).into((holder).mImageViewRight);
        }else {
            Glide.with(mContext).load(MyApplication.getUserInfo().getImg()).into((holder).mImageViewRight);
        }
        File file=new File(chatListInfo.getFilePath());
        holder.mTvFileName.setText(file.getName());
        holder.mTvFileSize.setText(FileUtil.formatFileSize(file.length()));
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
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.openFile(context,new File(chatListInfo.getFilePath()));
            }
        });
    }

}
