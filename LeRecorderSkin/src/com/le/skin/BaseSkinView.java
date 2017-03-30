package com.le.skin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.le.skin.ui.FilterListView;
import com.le.skin.ui.RecorderDialogBuilder;
import com.le.skin.ui.Rotate3dAnimation;
import com.le.skin.ui.SkinParams;
import com.letv.recorder.bean.AudioParams;
import com.letv.recorder.bean.CameraParams;
import com.letv.recorder.callback.ISurfaceCreatedListener;
import com.letv.recorder.callback.LetvRecorderCallback;
import com.letv.recorder.callback.PublishListener;
import com.letv.recorder.controller.CameraSurfaceView;
import com.letv.recorder.controller.Publisher;
import com.letv.recorder.letvrecorderskin.R;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.util.LeLog;
import com.letv.recorder.util.NetworkUtils;

import java.util.Formatter;
import java.util.Locale;

public abstract class BaseSkinView extends FrameLayout {
    protected final static String TAG = "BaseSkinView";
    protected Publisher publisher;
    protected SkinParams skinParams;
    private boolean isback;
    private View fouceView;
    private View skinView;
    private ImageView backButton;
    protected TextView nameView;
    protected ImageView openButton;
    private ImageView flashButton;
    private ImageView volumeButton;
    private ImageView switchButton;
    private ImageView filterButton;
    private TextView timerView;
    private ImageView thumdButton;
    private ImageView mirrorButton;
    protected TextView recView;
    protected boolean isRec = true;
    private boolean isFlash = false;
    private boolean isVolume = false;
    private Dialog loadingDialog;
    private Dialog errorDialog;
    private Dialog messageDialog;
    private StringBuilder mFormatBuilder = new StringBuilder();
    private Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    private int time;
    private volatile int video_lose = 0;
    private volatile int audio_lose = 0;
    private FilterListView filterListView;
    private volatile  boolean showTimer = false;
    protected CameraSurfaceView surfaceView;
    private  RelativeLayout surfaceRoot;
    private boolean isFirstSize = false;
    private volatile int frameLose = 0;
    private final static int UPLOAD_FILE_ERROR = -100001;
    private final static int UPLOAD_FILE_SUCCESS = 100001;
    private SeekBar zoomBar;
    private RelativeLayout rlSeekLayout;
    private int zoomCurrent = 0;

    public BaseSkinView(Context context) {
        super(context);
        init(null);
    }

    public BaseSkinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BaseSkinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    /**
     * 初始化操作
     * @param attrs
     */
    private void init(AttributeSet attrs){
        Log.d(TAG,"初始化init");
        skinParams = new SkinParams();
        if(attrs != null){
            //判断是否使用自定义参数
            Log.d(TAG,"判断是否使用自定义参数");
            decodeSkinParams(attrs);
        }
        isback = false;
        zoomCurrent = 0;
        initView();
    }

    /**
     * 初始化view
     */
    private void initView(){
        Log.d(TAG,"初始化view,initView");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        skinView = inflater.inflate(R.layout.le_recorder_skin_view,null);
        surfaceRoot = (RelativeLayout) skinView.findViewById(R.id.rl_surface_root);
        addView(skinView);
        surfaceView = (CameraSurfaceView)skinView.findViewById(R.id.camera_surface_view);
        backButton = (ImageView) skinView.findViewById(R.id.imgB_back);
        nameView = (TextView) skinView.findViewById(R.id.tv_title);
        openButton = (ImageView) skinView.findViewById(R.id.imgV_open);
        flashButton = (ImageView) skinView.findViewById(R.id.imgV_flashlight);
        volumeButton = (ImageView) skinView.findViewById(R.id.imgV_voice);
        switchButton = (ImageView) skinView.findViewById(R.id.imgV_postposition_camera);
        filterButton = (ImageView) skinView.findViewById(R.id.imgV_postposition_filter);
        timerView = (TextView) skinView.findViewById(R.id.tv_time);
        mirrorButton = (ImageView) skinView.findViewById(R.id.imgV_mirror);
        rlSeekLayout = (RelativeLayout) skinView.findViewById(R.id.rl_zoom_seek_bar);
        recView = (TextView) skinView.findViewById(R.id.tv_rec);
        thumdButton = (ImageView) skinView.findViewById(R.id.imgV_thumd);
        zoomBar = (SeekBar) skinView.findViewById(R.id.seekB_zoom);
        rlSeekLayout.setVisibility(View.INVISIBLE);
        zoomBar.setOnSeekBarChangeListener(seekBarChangeListener);
        findViewById(R.id.include_top_skin).setOnTouchListener(onTouchListener);
        findViewById(R.id.include_bottom_skin).setOnTouchListener(onTouchListener);
        filterListView = new FilterListView((RelativeLayout) skinView.findViewById(R.id.rl_skin_root), getContext(), new FilterListView.FilterListListener() {
            @Override
            public void selectFilter(int current) {
                switchFilter(current);
            }
        });
    }

    /**
     * 初始化图标状态
     */
    private void initIcon(){
        Log.d(TAG,"初始化图标状态");
        timerView.setVisibility(View.INVISIBLE);
        recView.setVisibility(View.INVISIBLE);
        thumdButton.setVisibility(View.INVISIBLE);
        if(skinParams.isVolumeGain()){
            volumeButton.setImageResource(R.drawable.letv_recorder_voise_open);
            isVolume = true;
            setVolume(1);
        }else{
            volumeButton.setImageResource(R.drawable.letv_recorder_voise_close);
            isVolume = false;
            setVolume(0);
        }
        if(skinParams.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT){
            flashButton.setVisibility(View.INVISIBLE);
            isFlash = false;
            flashButton.setImageResource(R.drawable.letv_recorder_flash_light_close);
        }
        if(skinParams.isLanscape()){
            rlSeekLayout.setPadding(dip2px(100),0,dip2px(100),0);
        }else{
            rlSeekLayout.setPadding(dip2px(45),0,dip2px(45),0);
        }
        if(skinParams.getCameraId() == Camera.CameraInfo.CAMERA_FACING_BACK){
            mirrorButton.setVisibility(View.INVISIBLE);
        }else{
            enableFrontCameraMirror(skinParams.isMirror());
        }
        rlSeekLayout.setOnTouchListener(onTouchListener);
        volumeButton.setOnClickListener(onClickListener);
        backButton.setOnClickListener(onClickListener);
        openButton.setOnClickListener(onClickListener);
        flashButton.setOnClickListener(onClickListener);
        volumeButton.setOnClickListener(onClickListener);
        switchButton.setOnClickListener(onClickListener);
        filterButton.setOnClickListener(onClickListener);
        mirrorButton.setOnClickListener(onClickListener);
    }
    //拦截点击事件
    private OnTouchListener onTouchListener =  new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if(i == R.id.imgB_back){
                Log.d(TAG,"点击后退按钮");
                ((Activity)getContext()).finish();
            }else if(i == R.id.imgV_open){
                boolean recording = publisher.isRecording();
                Log.d(TAG,"点击开始推流按钮，是否正在推流："+recording);
                if(recording){
                    stopPublisher();
                    openButton.setImageResource(R.drawable.letv_recorder_open);
                }else{
                   openClickPublisher();
                }
            }else if(i == R.id.imgV_flashlight){//点击闪关灯按钮
                Log.d(TAG,"点击闪光灯按钮，当前是否已经开启闪光灯："+isFlash);
                isFlash = !isFlash;
                changeFlash(isFlash);
                if(isFlash) {
                    flashButton.setImageResource(R.drawable.letv_recorder_flash_light_open);
                }else {
                    flashButton.setImageResource(R.drawable.letv_recorder_flash_light_close);
                }
            }else if(i == R.id.imgV_voice){
                Log.d(TAG,"点击音量按钮，当前音量状态："+isVolume);
                isVolume = !isVolume;
                if(isVolume){
                    volumeButton.setImageResource(R.drawable.letv_recorder_voise_open);
                    setVolume(1);
                }else{
                    volumeButton.setImageResource(R.drawable.letv_recorder_voise_close);
                    setVolume(0);
                }
            }else if(i == R.id.imgV_postposition_camera){
                boolean temp = publisher.getCameraParams().getCameraId() == Camera.CameraInfo.CAMERA_FACING_BACK;
                Log.d(TAG,"切换前后摄像头，当前是后置摄像头么？"+temp);
                if(temp){
                    switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                }else{
                    switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                }
            }else if(i == R.id.imgV_postposition_filter){
                Log.d(TAG,"点击选择滤镜框");
                filterListView.showFilter();
            }else if(i == R.id.imgV_mirror){
                Log.d(TAG,"点击镜像切换");
                enableFrontCameraMirror(!skinParams.isMirror());
            }
        }
    };
    /**
     * 初始化推流
     */
    protected void initPublish(){
        LeLog.d(TAG,"初始化推流器initPublish,所有的初始化参数是："+skinParams.toString());
//        skinView.setBackgroundColor(0x01000001);
        FrameLayout.LayoutParams params;
        if(skinParams.getSurfaceWidth() <=0 || skinParams.getSurfaceHeight() <= 0) {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        }else{
            params = new FrameLayout.LayoutParams(skinParams.getSurfaceWidth(),skinParams.getSurfaceHeight(), Gravity.CENTER);
        }
        surfaceRoot.setLayoutParams(params);
        fouceView = new View(getContext());
        fouceView.setBackgroundResource(skinParams.getFoceView());
        surfaceRoot.addView(fouceView,dip2px(70),dip2px(70));
        publisher.initPublisher((Activity) getContext());
        publisher.getRecorderContext().setUseLanscape(skinParams.isLanscape());
        CameraParams cameraParams = publisher.getCameraParams();
        AudioParams audioParams = publisher.getAudioParams();
        publisher.setPublishListener(listener);
        publisher.getVideoRecordDevice().bindingGLView(surfaceView);
        publisher.getVideoRecordDevice().setSurfaceCreatedListener(surfaceCreatedListener);
        if(!(skinParams.getVideoWidth()<=0 ||  skinParams.getVideoHeight()<=0)){
            cameraParams.setWidth(skinParams.getVideoWidth());
            cameraParams.setHeight(skinParams.getVideoHeight());
        }
        cameraParams.setCameraId(skinParams.getCameraId()); //开启默认前置摄像头
        cameraParams.setVideoBitrate(skinParams.getVideoBitrate()); //设置码率
        audioParams.setEnableVolumeGain(true);//开启音量调节,注意,这一点会影响性能,如果没有必要,设置为false
        cameraParams.setFocusOnTouch(skinParams.isOnTouch());//关闭对焦功能
        cameraParams.setFocusOnAnimation(skinParams.isOnAnimation());//关闭对焦动画
        cameraParams.setOpenGestureZoom(skinParams.isOpenGestureZoom());
        publisher.getVideoRecordDevice().setFocusView(fouceView);//设置对焦图片。如果需要对焦功能和对焦动画,请打开上边两个设置,并且在这里传入一个合适的View
        publisher.getRecorderContext().setAutoUpdateLogFile(skinParams.isUpdateLogFile()); //是否开启日志文件自动上报
        initIcon();
    }

    /**
     * 显示加载对话框
     */
    protected void showLoadingDialog(){
        Log.d(TAG,"显示加载对话框showLoadingDialog");
        openButton.setClickable(false);
        loadingDialog = RecorderDialogBuilder.showLoadDialog(getContext(),"正在急速加载中,请稍后");
    }

    /**
     * 隐藏加载对话框
     */
    protected void hideLoadingDialog(){
        Log.d(TAG,"隐藏加载对话框hideLoadingDialog");
        openButton.setClickable(true);
        if(loadingDialog != null && loadingDialog.isShowing()){
            try {
                loadingDialog.dismiss();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            loadingDialog = null;
        }
    }

    /**
     * 显示错误对话框
     */
    private void showErrorDialog(){
        Log.d(TAG,"显示错误对话框showErrorDialog");
        if(errorDialog != null && errorDialog.isShowing()){
            errorDialog.dismiss();
            errorDialog = null;
        }
        errorDialog = RecorderDialogBuilder.showCommentDialog(getContext(), ((NetworkUtils.getNetType(getContext()) == null)?"网络异常,":"")+"无法连接推流服务器","我知道了",null, new OnClickListener() {

            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
                errorDialog = null;
            }
        }, null);
    }

    /**
     * 开始推流时间计数
     */
    private void showTimerView() {
        Log.d(TAG,"开始推流时间计数");
        showTimer = true;
        timerView.setVisibility(View.VISIBLE);
        if (isRec){
            recView.setVisibility(View.VISIBLE);
        }
        thumdButton.setVisibility(View.VISIBLE);
        timerView.setText(stringForTime(time));
        uiHandler.postDelayed(timeRunnable,1000);
    }

    /**
     * 关闭推流时间计数
     */
    private void hideTimerView(){
        Log.d(TAG,"关闭推流时间计数");
        showTimer = false;
        uiHandler.removeCallbacks(timeRunnable);
        timerView.setVisibility(View.INVISIBLE);
        recView.setVisibility(View.INVISIBLE);
        thumdButton.setVisibility(View.INVISIBLE);
    }

    /**
     * 主动上传日志文件
     */
    public void updataFile(){
        Log.d(TAG,"主动上传日志文件");
        publisher.sendLogFile(new LetvRecorderCallback() {
            @Override
            public void onFailed(int code, String msg) {
                Message message = mHandler.obtainMessage(UPLOAD_FILE_ERROR);
                message.arg1 = code;
                message.obj = msg;
                mHandler.sendMessage(message);
            }

            @Override
            public void onSucess(Object data) {
                Message message =  mHandler.obtainMessage(UPLOAD_FILE_SUCCESS);
                mHandler.sendMessage(message);
            }
        });
    }

    public void onPause() {
        Log.d(TAG,"生命周期：onPause");
        zoomCurrent = publisher.getVideoRecordDevice().getZoom();
        surfaceView.onPause();
        if(publisher.isRecording()) { //正在推流
            isback = true;
            stopPublisher();//停止推流
        }
        //关闭摄像头
        publisher.getVideoRecordDevice().stop();
    }/**
     * 切换摄像头,需要注意,切换摄像头不能太频繁,如果太频繁会导致应用程序崩溃。建议最快10秒一次
     */
    boolean isSwitch = false;
    private void switchCamera(int cameraId){
        Log.d(TAG,"switchCamera切换摄像头:"+cameraId);
        if(isSwitch){
            Log.d(TAG,"切换摄像头不能太频繁哦,等待10秒后在切换吧");
            Toast.makeText(getContext(),"切换摄像头不能太频繁哦,等待10秒后在切换吧",Toast.LENGTH_SHORT).show();
            return;
        }
        Rotate3dAnimation animation=new Rotate3dAnimation(0, 180, switchButton.getWidth()/2f, switchButton.getHeight()/2f, 0f, true);
        animation.setDuration(500);
        animation.setFillAfter(true);
        switchButton.startAnimation(animation);
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isSwitch = false;
            }
        },10*1000);
        isSwitch = true;
        zoomCurrent = 0;
        publisher.getVideoRecordDevice().switchCamera(cameraId);//切换摄像头
        if(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
            isFlash = false;
            flashButton.setImageResource(R.drawable.letv_recorder_flash_light_close);
            flashButton.setVisibility(View.INVISIBLE);
            mirrorButton.setVisibility(View.VISIBLE);
            enableFrontCameraMirror(skinParams.isMirror());
        }else{
            flashButton.setVisibility(View.VISIBLE);
            mirrorButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 开启闪光灯。注意,当使用前置摄像头时不能打开闪光灯
     */
    private void changeFlash(boolean flash){
        Log.d(TAG,"changeFlash,切换闪光灯："+flash);
        publisher.getVideoRecordDevice().setFlashFlag(flash);//切换闪关灯
    }

    /**
     * 切换滤镜,设置为0为关闭滤镜
     */
    private void switchFilter(int model){
        Log.d(TAG,"switchFilter,切换滤镜："+model);
        publisher.getVideoRecordDevice().setFilterModel(model);//切换滤镜
    }

    private void openClickPublisher(){
        Log.d(TAG,"openClickPublisher，推流前网络判断");
        if(NetworkUtils.getNetType(getContext())==null){
            messageDialog = RecorderDialogBuilder.showCommentDialog(getContext(), "网络连接失败,请检查后重试", "我知道了", null, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG,"网络连接失败,请检查后重试");
                    messageDialog.dismiss();
                    messageDialog = null;
                    openButton.setClickable(true);
                }
            }, null);
        } else if(!NetworkUtils.isWifiNetType(getContext())){
            Log.d(TAG,"移动网络推流");
            messageDialog = RecorderDialogBuilder.showMobileNetworkWarningDialog(getContext(), new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(messageDialog!=null){
                        messageDialog.dismiss();
                        messageDialog = null;
                    }
                    if(startPublisher()){
                        showLoadingDialog();
                        openButton.setImageResource(R.drawable.letv_recorder_stop);
                    }else{
                        openButton.setImageResource(R.drawable.letv_recorder_open);
                    }
                }
            },new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(messageDialog!=null){
                        messageDialog.dismiss();
                        messageDialog = null;
                    }
                    openButton.setClickable(true);
                }
            });
        }else{
            Log.d(TAG,"wifi推流");
            if(startPublisher()){
                showLoadingDialog();
                openButton.setImageResource(R.drawable.letv_recorder_stop);
            }else{
                openButton.setImageResource(R.drawable.letv_recorder_open);
            }
        }


    }

    /**
     * 开始推流方法，这个方法需要子类去实现
     * @return
     */
    protected boolean startPublisher(){
        Log.d(TAG,"startPublisher:"+this.getClass().getName());
        time = 0;
        return false;
    }

    /**
     * 停止推流。
     */
    protected void stopPublisher(){
        Log.d(TAG,"stopPublisher,停止推流");
        hideTimerView();
        publisher.stopPublish();
    }
    /**
     * 设置声音大小,必须对setEnableVolumeGain设置为true
     * @param volume 0-1为缩小音量,1为正常音量,大于1为放大音量
     */
    private void setVolume(int volume){
        Log.d(TAG,"setVolume设置音量大小："+volume);
        publisher.setVolumeGain(volume);//设置声音大小
    }

    /**
     * 切换镜像模式。注意，对于后置摄像头不存在非镜像模式。所以会自动切换回镜像模式
     * @param isMirror
     */
    private void enableFrontCameraMirror(boolean isMirror){
        if(publisher.getCameraParams().getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            Log.d(TAG, "开启镜像模式：" + isMirror);
            publisher.getVideoRecordDevice().enableFrontCameraMirror(isMirror);
            skinParams.setMirror(isMirror);
            if (isMirror) {
                mirrorButton.setImageResource(R.drawable.letv_recorder_mirror_open);
            } else {
                mirrorButton.setImageResource(R.drawable.letv_recorder_mirror_close);
            }
        }else{
            Log.w(TAG,"后置摄像头没有镜像模式");
        }
    }
    /**
     * 测试方法。如果调用，那么请在获取第一帧推流成功事件之后，每隔5秒以上调用一次。不可以重复调用
     * @return
     */
    public int getPushStreamingTime(){
        Log.d(TAG,"getPushStreamingTime：获取每一帧推流时间");
        if(isFirstSize && publisher != null && publisher.isRecording()){
            return publisher.getStreamingDelay();
        }else{
            isFirstSize = false;
        }
        return -1;
    }

    /**
     * 获取丢包率
     * @return
     */
    public int getFrameLose(){
        Log.d(TAG,"getFrameLose获取丢包率");
        int temp = frameLose;
        frameLose = 0;
        return temp;
    }

    /**
     * VIew生命周期
     */
    public void onResume() {
        Log.d(TAG,"onResume生命周期");
        if(skinParams.isLanscape()){
            ((Activity)getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            ((Activity)getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        surfaceView.onResume();

        if(publisher != null){
            isFlash = false;
            changeFlash(false);
            if(publisher.getCameraParams().getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT){
                flashButton.setVisibility(View.INVISIBLE);
            }else{
                flashButton.setVisibility(View.VISIBLE);
            }
            flashButton.setImageResource(R.drawable.letv_recorder_flash_light_close);
        }
    }
    /**
     * VIew生命周期
     */
    public void onDestroy() {
        Log.d(TAG,"onDestroy,生命周期");
        zoomCurrent = 0;
        surfaceView.onDestroy();
        isback = false;
        publisher.release();//销毁推流器
    }

    protected abstract void publisherMessage(Message msg);
    private PublishListener listener = new PublishListener() {
        @Override
        public void onPublish(int code, String msg, Object... obj) {
            Message message = mHandler.obtainMessage(code);
            message.obj = msg;
            if(obj != null || obj.length > 0 ) {
                Bundle bundle = new Bundle();
                try {
                    bundle.putBoolean("bool", (Boolean) obj[0]);
                }catch (Exception e){

                }
                message.setData(bundle);
            }
            mHandler.sendMessage(message);
        }
    };
    private Handler uiHandler = new Handler();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RecorderConstance.RECORDER_OPEN_URL_FAILED:
                    Log.d(TAG,"UI mHandler，RTMP 连接建立失败");
                    openButton.setImageResource(R.drawable.letv_recorder_open);
                    hideLoadingDialog();
                    showErrorDialog();
                    break;
                case RecorderConstance.RECORDER_OPEN_URL_SUCESS:
                    Log.d(TAG,"UI mHandler，RTMP 连接建立成功");
                    break;
                case RecorderConstance.RECORDER_PUSH_FIRST_SIZE:
                    Log.d(TAG,"UI mHandler，RTMP 第一帧画面推流成功");
                    isFirstSize = true;
                    hideLoadingDialog();
                    if(!showTimer) {
                        showTimerView();
                    }
                    break;
                case RecorderConstance.RECORDER_PUSH_ERROR:
                    Log.d(TAG,"UI mHandler，RTMP 推流失败");
                    showErrorDialog();
                case RecorderConstance.RECORDER_PUSH_STOP_SUCCESS:
                    Log.d(TAG,"UI mHandler，停止推流");
                    openButton.setImageResource(R.drawable.letv_recorder_open);
                    hideLoadingDialog();
                    hideTimerView();
                    break;
                case RecorderConstance.RECORDER_PUSH_AUDIO_PACKET_LOSS_RATE:
                    //Log.d(TAG,"UI mHandler，音频丢包");
                    audio_lose ++;
                    break;
                case RecorderConstance.RECORDER_PUSH_VIDEO_PACKET_LOSS_RATE:
                    //Log.d(TAG,"UI mHandler，视频丢包");
                    video_lose ++;
                    frameLose ++;
                    break;
                case UPLOAD_FILE_ERROR:
                    Log.d(TAG,"UI mHandler，文件上传失败，状态吗:"+msg.arg1+",失败原因："+msg.obj);
                    Toast.makeText(getContext(),"文件上传失败，状态吗:"+msg.arg1+",失败原因："+msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case UPLOAD_FILE_SUCCESS:
                    Log.d(TAG,"UI mHandler，文件上传成功");
                    Toast.makeText(getContext(),"日志文件上传成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    publisherMessage(msg);
                    break;
            }
        }
    };
    private ISurfaceCreatedListener surfaceCreatedListener = new ISurfaceCreatedListener() {
        @Override
        public void onGLSurfaceCreatedListener() {
            Log.d(TAG,"ISurfaceCreatedListener 创建成功");
            boolean temp =publisher.getVideoRecordDevice().setZoom(zoomCurrent);
            publisher.getVideoRecordDevice().start();//打开摄像头
            Log.d(TAG,"打开摄像头，设置zoom值:"+zoomCurrent+"设置是否生效："+temp);
            if(isback && skinParams.isResume()){
                isback = false;
                publisher.publish();
                openButton.setImageResource(R.drawable.letv_recorder_stop);
            }
            //在获取最大Zoom值得时候，必须保证Camera已经成功打开。所以把获取方法放在这个方法中
            int maxZoom = publisher.getVideoRecordDevice().getMaxZoom();
            zoomBar.setMax(maxZoom);
            zoomBar.setProgress(publisher.getVideoRecordDevice().getZoom());
        }

        @Override
        public void zoomOnTouch(int state,int zoom, int maxZoom) {
            switch (state){
                case STATE_ZOOM_DOWN:
                    uiHandler.removeCallbacks(zoomGone);
                    if(rlSeekLayout.getVisibility() != View.VISIBLE ){
                        rlSeekLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case STATE_ZOOM_UP:
                    uiHandler.postDelayed(zoomGone,3000);
                    break;
                case STATE_ZOOM_MOVE:
                    break;
            }
            zoomBar.setProgress(zoom);
        }
    };
    private Runnable zoomGone = new Runnable() {
        @Override
        public void run() {
            if(rlSeekLayout != null)
            rlSeekLayout.setVisibility(View.INVISIBLE);
        }
    };
    public SkinParams getSkinParams(){
        return skinParams;
    }
    private void decodeSkinParams(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.publisher_skin);
        skinParams.setVideoBitrate(typedArray.getInt(R.styleable.publisher_skin_videoBitrate,skinParams.getVideoBitrate()));
        skinParams.setCameraId(typedArray.getInt(R.styleable.publisher_skin_cameraId,skinParams.getCameraId()));
        skinParams.setFoceView(typedArray.getResourceId(R.styleable.publisher_skin_foceView,skinParams.getFoceView()));
        skinParams.setLanscape(typedArray.getBoolean(R.styleable.publisher_skin_isLanscape,skinParams.isLanscape()));
//        skinParams.setNetResume(typedArray.getBoolean(R.styleable.publisher_skin_isNetResume,skinParams.isNetResume()));
//        skinParams.setNowPush(typedArray.getBoolean(R.styleable.publisher_skin_isNowPush,skinParams.isNowPush()));
        skinParams.setOnAnimation(typedArray.getBoolean(R.styleable.publisher_skin_isOnAnimation,skinParams.isOnAnimation()));
        skinParams.setOnTouch(typedArray.getBoolean(R.styleable.publisher_skin_isOnTouch,skinParams.isOnTouch()));
        skinParams.setResume(typedArray.getBoolean(R.styleable.publisher_skin_isResume,skinParams.isResume()));
        skinParams.setTitle(typedArray.getString(R.styleable.publisher_skin_pushTitle));
        skinParams.setUpdateLogFile(typedArray.getBoolean(R.styleable.publisher_skin_updateLogFile,skinParams.isUpdateLogFile()));
        skinParams.setVolumeGain(typedArray.getBoolean(R.styleable.publisher_skin_isVolumeGain,skinParams.isVolumeGain()));
        skinParams.setVideoHeight(typedArray.getInt(R.styleable.publisher_skin_videoHeight,skinParams.getVideoHeight()));
        skinParams.setVideoWidth(typedArray.getInt(R.styleable.publisher_skin_videoWidth,skinParams.getVideoWidth()));
        skinParams.setFirstMachine(typedArray.getBoolean(R.styleable.publisher_skin_isFirstMachine,skinParams.isFirstMachine()));
        skinParams.setSurfaceWidth(typedArray.getInt(R.styleable.publisher_skin_surfaceWidth,skinParams.getSurfaceWidth()));
        skinParams.setSurfaceHeight(typedArray.getInt(R.styleable.publisher_skin_surfaceHeight,skinParams.getSurfaceHeight()));
        skinParams.setMirror(typedArray.getBoolean(R.styleable.publisher_skin_mirror,skinParams.isMirror()));
        skinParams.setOpenGestureZoom(typedArray.getBoolean(R.styleable.publisher_skin_isOpenGestureZoom,skinParams.isOpenGestureZoom()));
        typedArray.recycle();
    }

    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if(!showTimer) return;
            uiHandler.postDelayed(timeRunnable, 1000);
            time++;
            timerView.setText(stringForTime(time));
            if (time % 2 == 0) {
                thumdButton.setVisibility(View.VISIBLE);
            } else {
                thumdButton.setVisibility(View.INVISIBLE);
            }
        }
    };
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(publisher.isRecording()){
                if(video_lose > 3 || audio_lose > 3){
                    Toast.makeText(getContext(),"当前网络较差,请更换网络环境",Toast.LENGTH_SHORT).show();
                }
                video_lose = 0;
                audio_lose = 0;
                uiHandler.postDelayed(runnable,30*1000);
            }
        }
    };
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener =  new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser && publisher != null && publisher.getVideoRecordDevice() != null){
                if(!publisher.getVideoRecordDevice().setZoom(progress)){
                    seekBar.setProgress(publisher.getVideoRecordDevice().getZoom());

                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            uiHandler.removeCallbacks(zoomGone);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            uiHandler.postDelayed(zoomGone,3000);
        }
    };
}
