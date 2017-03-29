package com.matrix.yukun.matrix.leshilive_module;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.matrix.yukun.matrix.R;

import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.rtsp.RtspClient;
import net.majorkernelpanic.streaming.video.VideoQuality;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveActivity extends AppCompatActivity implements Session.Callback{

    @BindView(R.id.surfaceview)
    net.majorkernelpanic.streaming.gl.SurfaceView mSurfaceView;
    private Session mSession;
    private RtspClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //音频编码格式
//音频参数 采样率
//视频编码格式
//视频参数 分辨率1280*720 帧率15 码率1000*1000
//用于进行预览展示的SurfaceView
//SurfaceView//Camera方向
//一些监听回调
        mSession = SessionBuilder.getInstance()
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_AAC)//音频编码格式
                .setAudioQuality(new AudioQuality(8000,16000))//音频参数 采样率
                .setVideoEncoder(SessionBuilder.VIDEO_H264)//视频编码格式
                //视频参数 分辨率1280*720 帧率15 码率1000*1000
                .setVideoQuality(new VideoQuality(1280, 720, 15, 1000*1000))
                .setSurfaceView(mSurfaceView)//用于进行预览展示的SurfaceView
                .setPreviewOrientation(90)//SurfaceView//Camera方向
                .setCallback(this)//一些监听回调
                .build();

        mClient = new RtspClient();
        mClient.setSession(mSession);//设置Session
        mClient.setCallback(new RtspClient.Callback() {
            @Override
            public void onRtspUpdate(int message, Exception exception) {
                Log.i("---callback",message+"");
            }
        });  //回调监听
        mClient.setServerAddress("192.168.1.115", 554);//服务器的ip和端口号
        //这里算是一个标识符，服务器会在连接后创建一个名为live.sdp的文件，所以这里的名字一定要唯一。
        mClient.setStreamPath("/live.sdp");
        mClient.startStream();//开始推流
    }

    public void Back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.stopStream();
    }

    @Override
    public void onBitrateUpdate(long bitrate) {

    }

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {
        Log.e("error", "An error occured", e);
    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void onSessionConfigured() {
        mSession.start();
    }

    @Override
    public void onSessionStarted() {

    }

    @Override
    public void onSessionStopped() {
        mSession.stop();
    }

}
