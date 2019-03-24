package com.matrix.yukun.matrix.video_module.video;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayActivity extends BaseActivity {

    @BindView(R2.id.videoplayer)
    JZVideoPlayerStandard mVideoplayer;
    @BindView(R2.id.im_cover)
    ImageView mImCover;
    @BindView(R2.id.im_play)
    ImageView mImPlay;
    @BindView(R2.id.rl_bg)
    RelativeLayout mRlBg;
    private String mVideoPath;
    private String mVideoCover;
    private String mVideoTitle;
    private int mType;


    @Override
    public int getLayout() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_video_play;
    }

    @Override
    public void initView() {
        mVideoPath = getIntent().getStringExtra("video_path");
        mVideoCover = getIntent().getStringExtra("video_cover");
        mVideoTitle = getIntent().getStringExtra("video_title");
        mType = getIntent().getIntExtra("type",0);
        mVideoplayer.setUp(mVideoPath, JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mVideoTitle);
        //网络
        Glide.with(this).load(mVideoCover).into(mImCover);
        mRlBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mRlBg.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mVideoplayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoplayer.releaseAllVideos();
    }
}
