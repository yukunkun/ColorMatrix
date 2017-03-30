package com.matrix.yukun.matrix.leshilive_module;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.recorder.bean.AudioParams;
import com.letv.recorder.bean.CameraParams;
import com.letv.recorder.callback.ISurfaceCreatedListener;
import com.letv.recorder.callback.PublishListener;
import com.letv.recorder.controller.CameraSurfaceView;
import com.letv.recorder.controller.Publisher;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.matrix.yukun.matrix.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveActivity extends AppCompatActivity {

    @BindView(R.id.time)
    TextView timeView;
    @BindView(R.id.open)
    ImageButton open;
    @BindView(R.id.change_flash)
    ImageButton changeFlash;
    @BindView(R.id.switch_camera)
    ImageButton switchCamera;
    @BindView(R.id.switch_filter)
    ImageButton switchFilter;
    @BindView(R.id.set_volume)
    ImageButton setVolume;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.rea_cover)
    RelativeLayout reaCover;
    private Context mContext;
    private Publisher publisher;
    private static final String TAG = "LiveActivity";
    //    19113.mpush.live.lecloud.com
//    rtmp://{推流域名}/发布点/{直播流名称}。
    private static String url /*= "rtmp://19113.mpush.live.lecloud.com/live/mylive"*/;
    private CameraSurfaceView cameraSurfaceView;
    private CameraParams cameraParams;
    private AudioParams audioParams;
    private Handler tenHandler = new Handler();
    private Handler timerHandler = new Handler();
    private int time = 0;
    private boolean timeFlag = false;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (publisher.isRecording()) {
                time++;
                timeView.setText("时间:" + time + "秒");
                timerHandler.postDelayed(timerRunnable, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);
        mContext = LiveActivity.this;
        init();
    }

    private void init() {
        cameraSurfaceView = (CameraSurfaceView) findViewById(R.id.camera_surface_view);
        //必须的代码调用用部分
        publisher = Publisher.getInstance();
        publisher.initPublisher((Activity) mContext);
        cameraParams = publisher.getCameraParams();
        audioParams = publisher.getAudioParams();
        audioParams.setEnableVolumeGain(false);//开启音量调节,注意,这一点会影响性能,如果没有必要,设置为false
        cameraParams.setFrontCameraMirror(true);//开启镜像
        publisher.getRecorderContext().setUseLanscape(false);//竖屏
        //告诉推流器器使用用横屏推流还是竖屏推流
        publisher.setPublishListener(new PublishListener() {
            @Override
            public void onPublish(int i, String s, Object... objects) {
                switch (i) {
                    case RecorderConstance.RECORDER_OPEN_URL_SUCESS:
                        Log.d(TAG, "推流连接成功:只有当连接成功以后才能开始推流");
                        break;
                    case RecorderConstance.RECORDER_OPEN_URL_FAILED:
                        Log.d(TAG, "推流连接失败:如果失败,大大多是推流地址不不可用用或者网网络问题");
                        break;
                    case RecorderConstance.RECORDER_PUSH_FIRST_SIZE:
                        Log.d(TAG, "第一一针画面面推流成功,代表成功的开始推流了了:推流成功的标志回调");
                        if (!timeFlag) {
                            timerHandler.postDelayed(timerRunnable, 1000);
                        }
                        timeFlag = true;
                        break;
                    case RecorderConstance.RECORDER_PUSH_AUDIO_PACKET_LOSS_RATE:
                        Log.d(TAG, "音音频出现丢帧现象。如果一一分钟丢帧次数大大于5次,导致声音音跳动:可以对网网络进行行行判 定");
                        break;
                    case RecorderConstance.RECORDER_PUSH_VIDEO_PACKET_LOSS_RATE:
                        Log.d(TAG, "视频出现丢帧现象,如果一一分钟丢帧次数大大于5次,导致画面面跳动:可以对网网络进行行行判");
                        break;
                    case RecorderConstance.RECORDER_PUSH_ERROR:
                        Log.d(TAG, "推流失败,原因:网网络较差,编码出错,推流崩溃,第一一针数据发送失败...等等各种原 因导致");
                        break;
                    case RecorderConstance.RECORDER_PUSH_STOP_SUCCESS:
                        Log.d(TAG, "成功的关闭了了底层推流,可以进行行行下次推流了了:保证推流成功关闭");
                        break;
                }
            }
        });//设置推流状态监听器器
        //绑定Camera显示View,要求必须是CameraSurfaceView
        publisher.getVideoRecordDevice().bindingGLView(cameraSurfaceView);
        //设置CameraSurfaceView 监听器器,当CameraSurfaceView 创建成功的时候回回调onGLSurfaceCreatedListener,这个时候才能开启摄像头
        publisher.getVideoRecordDevice().setSurfaceCreatedListener(new ISurfaceCreatedListener() {
            @Override
            public void onGLSurfaceCreatedListener() {
                if (!publisher.getVideoRecordDevice().isRecording()) {//判断摄像头是否没有打开
                    publisher.getVideoRecordDevice().start();//打开摄像头
                    if (!publisher.isRecording() && url != null) {
                        publisher.setUrl(url);//设置推流地址
                        publisher.publish();//在摄像头打开以后才能开始推流
                        Toast.makeText(LiveActivity.this, "start", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void zoomOnTouch(int i, int i1, int i2) {

            }
        });
    }

    @OnClick({R.id.sure, R.id.open, R.id.change_flash, R.id.switch_camera, R.id.switch_filter, R.id.set_volume})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open:
                time = 0;
                //暂且只有一个地址
                url = "rtmp://19113.mpush.live.lecloud.com/live/mylive";
                if (!publisher.isRecording() && url != null) {
                    timeView.setText("请求推流");
                    publisher.setUrl(url);//设置推流地址
                    publisher.publish();//在摄像头打开以后才能开始推流
                    open.setImageResource(R.drawable.letv_recorder_stop);
                } else {
                    timeView.setText("关闭推流");
                    publisher.stopPublish(); //结束推流
                    open.setImageResource(R.drawable.letv_recorder_open);
                }
                break;
            case R.id.change_flash:
                changeFlash();
                break;
            case R.id.switch_camera:
                switchCamera();
                break;
            case R.id.switch_filter:
                switchFilter();
                break;
            case R.id.set_volume:
                setVolume();
                break;
            case R.id.sure:
                getLive();
                break;
        }
    }

    //判断权限
    private void getLive() {
        String password = editPassword.getText().toString();
        if (password != null && password.equals("1")) {
            reaCover.setVisibility(View.GONE);
        }else {
            Toast.makeText(LiveActivity.this, "对不起你没有权限开启直播!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 切换摄像头,需要注意,切换摄像头不能太频繁,如果太频繁会导致应用程序崩溃。建议最快10秒一次
     */
    boolean isSwitch = false;
    boolean flag = false;

    public void switchCamera() {
        if (isSwitch) {
            Toast.makeText(this, "切换摄像头不能太频繁哦,等待10秒后在切换吧", Toast.LENGTH_SHORT).show();
            return;
        }
        tenHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "十秒已过,可以继续切换摄像头", Toast.LENGTH_SHORT).show();
                isSwitch = false;
            }
        }, 10 * 1000);
        isSwitch = true;
        int cameraID;
        if (cameraParams.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
            changeFlash.setVisibility(View.VISIBLE);
        } else {
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            if (flag) {
                flag = !flag;//切换前置摄像头会自动关闭闪光灯
                publisher.getVideoRecordDevice().setFlashFlag(flag);//切换闪关灯
            }
            changeFlash.setVisibility(View.GONE);
        }
        publisher.getVideoRecordDevice().switchCamera(cameraID);//切换摄像头
    }


    /**
     * 切换滤镜,设置为0为关闭滤镜
     */
    int model = CameraParams.FILTER_VIDEO_NONE;//无效果

    public void switchFilter() {
        if (model == CameraParams.FILTER_VIDEO_NONE) {
            model = CameraParams.FILTER_VIDEO_DEFAULT; //默认的美颜效果
            Toast.makeText(LiveActivity.this, "默认美颜效果", Toast.LENGTH_SHORT).show();
        } else {
            model = CameraParams.FILTER_VIDEO_NONE;//无效果
            Toast.makeText(LiveActivity.this, "无美颜效果", Toast.LENGTH_SHORT).show();
        }
        publisher.getVideoRecordDevice().setFilterModel(model);//切换滤镜
    }

    /**
     * 设置声音大小,必须对setEnableVolumeGain设置为true
     *
     * @param volume 0-1为缩小音量,1为正常音量,大于1为放大音量
     */
    int volume = 1;

    public void setVolume() {
        if (volume == 1) {
            volume = 0;
            setVolume.setImageResource(R.drawable.letv_recorder_voise_close);
        } else {
            volume = 1;
            setVolume.setImageResource(R.drawable.letv_recorder_voise_open);

        }
        publisher.setVolumeGain(volume);//设置声音大小
    }

    /**
     * 开启闪光灯。注意,当使用前置摄像头时不能打开闪光灯
     */
    public void changeFlash() {
        if (cameraParams.getCameraId() != Camera.CameraInfo.CAMERA_FACING_FRONT) {
            flag = !flag;
            publisher.getVideoRecordDevice().setFlashFlag(flag);//切换闪关灯
            if (flag) {
                changeFlash.setImageResource(R.drawable.letv_recorder_flash_light_open);
            } else {
                changeFlash.setImageResource(R.drawable.letv_recorder_flash_light_close);
            }
        } else {
            Toast.makeText(getApplicationContext(), "前置摄像头不能打开闪关灯", Toast.LENGTH_LONG).show();
        }
    }

    public void LiveBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        publisher.release();//销毁推流器
        cameraSurfaceView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (publisher.getVideoRecordDevice().isRecording()) {//判断摄像头是否已经打开
            publisher.getVideoRecordDevice().stop();//关闭摄像头
        }

        if (publisher.isRecording()) { //正在推流
            publisher.stopPublish();//停止推流
        }
        cameraSurfaceView.onPause();
    }


}
