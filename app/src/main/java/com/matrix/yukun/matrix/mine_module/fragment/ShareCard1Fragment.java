package com.matrix.yukun.matrix.mine_module.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
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
public class ShareCard1Fragment extends BaseCardFragment {

    private CardView mCardView;
    private String imageName="card_1.png";
    private Scene     mSceneStart;
    public static ShareCard1Fragment getInstance(){
        ShareCard1Fragment shareCard1Fragment=new ShareCard1Fragment();
        return shareCard1Fragment;
    }
    @Override
    public int getLayout() {
        return R.layout.card_share_1_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mCardView = inflate.findViewById(R.id.cardview);
        //延迟1s动画
        mCardView.post(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        });
    }

    private void startAnimation() {
        mSceneStart = Scene.getSceneForLayout(mCardView, R.layout.include_card_1_show, getContext());
        /**
         * 切换到开始场景状态
         */
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            TransitionManager.go(mSceneStart , new Explode());
        }
    }

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
