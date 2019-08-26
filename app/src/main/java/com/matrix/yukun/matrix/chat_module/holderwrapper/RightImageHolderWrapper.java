package com.matrix.yukun.matrix.chat_module.holderwrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.holder.RightImageHolder;
import com.matrix.yukun.matrix.main_module.activity.PersonCenterActivity;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.activity.LoginActivity;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;

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
        if(MyApp.userInfo==null){
            Glide.with(mContext).load(chatListInfo.getBitmap()).placeholder(R.drawable.head_2).into((holder).mImageViewRight);
        }else {
            Glide.with(mContext).load(MyApp.getUserInfo().getImg()).into((holder).mImageViewRight);
        }
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
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(imagePath, opt);
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        // 获取屏的宽度和高度
        WindowManager windowManager = ((Activity)mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)
                opt.inSampleSize = picHeight / screenHeight;
        }
        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bm= BitmapFactory.decodeFile(imagePath, opt);
        if(bm!=null){
            ViewGroup.LayoutParams layoutParams=ivRight.getLayoutParams();
            if(bm.getHeight()>bm.getWidth()){
                layoutParams.width= ScreenUtils.instance().getWidth(mContext)/3;
                layoutParams.height= ScreenUtils.instance().getWidth(mContext)/2;
            }else {
                layoutParams.height= ScreenUtils.instance().getWidth(mContext)/3;
                layoutParams.width= ScreenUtils.instance().getWidth(mContext)/2;
            }
            ivRight.setLayoutParams(layoutParams);
        }
    }
}
