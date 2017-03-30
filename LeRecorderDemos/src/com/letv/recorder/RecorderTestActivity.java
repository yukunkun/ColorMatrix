package com.letv.recorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 什么叫做无皮肤版:
 *      也就是我不知道大家的业务逻辑。只能给大家提供一些最基本的接口。至于业务逻辑需要大家自己去设置。
 *   这个Activity和CameraView。只是告诉大家如何初始化推流,如何开始推流,如何使用有皮肤中提供的一些功能,比如滤镜、摄像头切换等。。。。
 *   如果你觉得麻烦,请直接对接有皮肤版SDK。
 *
 * 这个Activity 为了方便大家更快的了解和接入推流SDK。但是不建议大家直接使用这个
 * 在Activity中涉及CameraView类,这个也需要大家了解和使用。里面有很多提供的接口和配置,可以自行修改和设置
 * RecorderTestActivity 和 CameraView 是不能满足大家的产品需求的,如果需要,可以自行改动或者复制相关代码
 */
public class RecorderTestActivity extends Activity {
    private CameraView cameraView;
    private String path = "rtmp://216.mpush.live.lecloud.com/live/camerView";
    //播放地址:rtmp://216.mpull.live.lecloud.com/live/camerView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_test);
        cameraView = (CameraView) findViewById(R.id.camera_view);
        findViewById(R.id.open).setOnClickListener(listener);// 开始推流点击事件
        findViewById(R.id.change_flash).setOnClickListener(listener);//闪光灯点击事件
        findViewById(R.id.switch_filter).setOnClickListener(listener);//滤镜点击事件
        findViewById(R.id.switch_camera).setOnClickListener(listener);//切换摄像头点击事件
        findViewById(R.id.set_volume).setOnClickListener(listener);//声音点击事件
        cameraView.setTime((TextView) findViewById(R.id.time));
        cameraView.init(this,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.onDestroy();
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.open://开始推流以及结束推流
                    Toast.makeText(v.getContext(),"点击推流按钮",Toast.LENGTH_SHORT).show();
                    cameraView.publish(path);
                    break;
                case R.id.change_flash://切换闪光灯
                    Toast.makeText(v.getContext(),"点击闪光灯按钮",Toast.LENGTH_SHORT).show();
                    cameraView.changeFlash();
                    break;
                case R.id.switch_filter: //切换滤镜效果
                    Toast.makeText(v.getContext(),"点击滤镜按钮",Toast.LENGTH_SHORT).show();
                    cameraView.switchFilter();
                    break;
                case R.id.switch_camera://切换前后置摄像头
                    Toast.makeText(v.getContext(),"点击摄像头切换按钮",Toast.LENGTH_SHORT).show();
                    cameraView.switchCamera();
                    break;
                case R.id.set_volume://切换声音
                    Toast.makeText(v.getContext(),"点击声音按钮",Toast.LENGTH_SHORT).show();
                    cameraView.setVolume();
                    break;
            }
        }
    };
}
