package com.matrix.yukun.matrix.leshilive_module;

import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.live.ActionLiveVideoView;
import com.matrix.yukun.matrix.R;

public class LeShiLiveActivity extends AppCompatActivity {
    private RelativeLayout videoContainer;
    private IMediaDataVideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //视频播放界面，保持屏幕常亮利于视频观看体验
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_le_shi_live);
        init();
    }

    private void init() {
        videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoView = new ActionLiveVideoView(this);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
//        videoContainer.addView((View) videoView, params);
    }
}
