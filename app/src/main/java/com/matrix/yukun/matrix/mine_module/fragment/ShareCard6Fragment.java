package com.matrix.yukun.matrix.mine_module.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.fragment.BaseCardFragment;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.ImageUtils;

/**
 * author: kun .
 * date:   On 2018/12/10
 */
public class ShareCard6Fragment extends BaseCardFragment {
    CardView mCardView;
    private String imageName="card_6.png";
    private Scene     mSceneStart;
    public static ShareCard6Fragment getInstance(){
        ShareCard6Fragment shareCard1Fragment=new ShareCard6Fragment();
        return shareCard1Fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.card_share_6_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mCardView = inflate.findViewById(R.id.cardview);
        mCardView.post(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        });
    }

    private void startAnimation() {
        mSceneStart = Scene.getSceneForLayout(mCardView, R.layout.include_card_6_show, getContext());
        /**
         * 切换到开始场景状态
         */
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            TransitionManager.go(mSceneStart , new Explode());
        }
    }

    @Override
    public void saveCardView(){
        Bitmap bitmap = ImageUtils.createViewBitmap(mCardView, mCardView.getWidth(), mCardView.getHeight());
        FileUtil.loadImage(bitmap,imageName);
    }

    @Override
    public String getImagePath() {
        String path= AppConstant.IMAGEPATH+"/"+imageName;
        return path;
    }
}
