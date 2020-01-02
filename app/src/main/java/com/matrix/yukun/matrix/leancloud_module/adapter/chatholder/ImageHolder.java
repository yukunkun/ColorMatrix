package com.matrix.yukun.matrix.leancloud_module.adapter.chatholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.adapter.LeanChatAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

/**
 * author: kun .
 * date:   On 2019/12/26
 */
public class ImageHolder extends BaseHolder {

    private ImageView mImageView;

    public ImageHolder(Context context, LeanChatMessage item, LeanChatAdapter leanChatAdapter, LinearLayout view) {
        super(context, item, leanChatAdapter,view);
    }

    @Override
    protected void initViewData() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.lean_chat_image, mLayout);
        mImageView = inflate.findViewById(R.id.iv_image);
        reSizeView();
        if(item.isReceived()){
            setGravity(mImageView, Gravity.LEFT| Gravity.CENTER_VERTICAL);
            mImageView.setBackgroundResource(R.drawable.balloon_l_pressed);
        }else {
            setGravity(mImageView, Gravity.RIGHT| Gravity.CENTER_VERTICAL);
            mImageView.setBackgroundResource(R.drawable.balloon_r_pressed);
        }
        if(!TextUtils.isEmpty(item.getImagePath())){
            GlideUtil.loadPlaceholderImage(item.getImagePath(),mImageView);
        }else {
            GlideUtil.loadPlaceholderImage(item.getImageUrl(),mImageView);
        }
    }

    @Override
    protected void initListener() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(item.getImagePath())){
                    ImageDetailActivity.start(context,item.getImagePath(),false);
                }else {
                    ImageDetailActivity.start(context,item.getImageUrl(),false);
                }
            }
        });
    }

    private void reSizeView(){
        ViewGroup.LayoutParams layoutParams=mImageView.getLayoutParams();
        if(item.getImageHeight()>item.getImageWidth()){
            layoutParams.width= ScreenUtils.instance().getWidth(context)/3;
            layoutParams.height= ScreenUtils.instance().getWidth(context)/2;
        }else {
            layoutParams.height= ScreenUtils.instance().getWidth(context)/3;
            layoutParams.width= ScreenUtils.instance().getWidth(context)/2;
        }
        mImageView.setLayoutParams(layoutParams);
    }


}
