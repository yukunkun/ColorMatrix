package com.matrix.yukun.matrix.gif_module.activity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import static android.view.View.GONE;

public class GifProActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIvBack;
    private RelativeLayout mRlImage;
    private RelativeLayout mRlVideo;
    private ImageView mIvGif;

    @Override
    public int getLayout() {
        return R.layout.activity_gif_pro;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mRlImage = findViewById(R.id.rl_image);
        mRlVideo = findViewById(R.id.rl_video);
        mIvGif = findViewById(R.id.iv_gif);
        mIvGif.post(new Runnable() {
            @Override
            public void run() {
                startAnimation(findViewById(R.id.card));
            }
        });
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mRlImage.setOnClickListener(this);
        mRlVideo.setOnClickListener(this);
        mIvGif.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_gif:
                GIFActivity.start(this);
                break;
            case R.id.rl_image:
                ImageActivity.start(this,ImageActivity.IMAGE);
                break;
            case R.id.rl_video:
                ImageActivity.start(this,ImageActivity.VIDEO);

                break;
        }
    }
    private  void startAnimation(View view){
        AnimationSet animationSet = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f, view.getWidth() / 2f, view.getHeight() / 2f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setInterpolator(new BounceInterpolator());
        view.setAnimation(scaleAnimation);
        animationSet.addAnimation(scaleAnimation);
        view.setAnimation(animationSet);
    }
}
