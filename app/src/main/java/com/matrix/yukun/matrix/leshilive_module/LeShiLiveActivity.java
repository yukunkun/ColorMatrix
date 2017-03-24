package com.matrix.yukun.matrix.leshilive_module;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.sdk.videoview.live.ActionLiveVideoView;
import com.matrix.yukun.matrix.R;

import java.util.LinkedHashMap;

public class LeShiLiveActivity extends AppCompatActivity {
    private RelativeLayout videoContainer;
    private IMediaDataVideoView videoView;

    //mActionId,活动 id, 可调用OpenApi接口批量获取

    String mActionId = "A2016062700000gx";
    //mUseHls = true,表示使用 hls协议播放;mUseHls = false,表示使用 rtmp协议播放;
    //默认使用 rtmp协议播放
    private boolean mUseHls = false;
    //用户标识 可通过官网用户中心-用户私钥获取-用户 ID
    String mCustomerId = "838389";
    //业务 ID，p 值需要
    String p = "102";
    //cuid,utoken是直播付费验证需要的两个参数
    String cuid = "";
    String utoken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //视频播放界面，保持屏幕常亮利于视频观看体验
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_le_shi_live);
        init();
        setInfo();
        setListener();
    }

    private void init() {
        videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoView = new ActionLiveVideoView(this);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        videoContainer.addView((View) videoView, params);
    }

    private void setInfo() {
        Bundle mBundle = new Bundle();
        //配置播放类型为活动直播
        mBundle.putInt(PlayerParams.KEY_PLAY_MODE, PlayerParams.VALUE_PLAYER_ACTION_LIVE);
//        // ActionId配置
        mBundle.putString(PlayerParams.KEY_PLAY_ACTIONID,mActionId);
        mBundle.putBoolean(PlayerParams.KEY_PLAY_USEHLS,mUseHls);
        mBundle.putString(PlayerParams.KEY_PLAY_CUSTOMERID, mCustomerId);
        mBundle.putString(PlayerParams.KEY_PLAY_BUSINESSLINE,p);
        mBundle.putString(PlayerParams.KEY_ACTION_CUID, cuid);
        mBundle.putString(PlayerParams.KEY_ACTION_UTOKEN, utoken);
        videoView.setDataSource(mBundle);
    }

    private void setListener() {
        VideoViewListener videoViewListener = new VideoViewListener() {
            @Override
            public void onStateResult(int event, Bundle bundle) {
                handlePlayerEvent(event, bundle);//处理播放器事件
            }

            @Override
            public String onGetVideoRateList(LinkedHashMap<String, String> linkedHashMap) {

                return null;
            }
        };
        videoView.setVideoViewListener(videoViewListener);
    }
    /**
     *
     处理播放器本身事件，具体事件可以参见
     IPlayer
     类
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {

            case PlayerEvent.PLAY_PREPARED:
                //播放器准备完成，此刻调用 start() 就可以进行播放了
                if (videoView != null) {
                    videoView.onStart();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
    }
}
