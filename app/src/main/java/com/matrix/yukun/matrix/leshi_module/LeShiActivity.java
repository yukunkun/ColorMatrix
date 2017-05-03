package com.matrix.yukun.matrix.leshi_module;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.location.GpsStatus;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.surfaceview.ISurfaceView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.sdk.videoview.vod.VodVideoView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshi_module.bean.VideoBean;
import com.matrix.yukun.matrix.leshi_module.present.LeShiListImple;
import com.matrix.yukun.matrix.leshi_module.present.PlayAdapter;
import com.matrix.yukun.matrix.leshi_module.present.PlayListPresent;
import com.matrix.yukun.matrix.leshi_module.present.PlayPresent;
import com.matrix.yukun.matrix.movie_module.MovieBaseActivity;
import com.matrix.yukun.matrix.selfview.VoiceLoadView;
import com.matrix.yukun.matrix.util.MarTextView;
import com.matrix.yukun.matrix.util.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class LeShiActivity extends MovieBaseActivity implements LeShiListImple{
    private RelativeLayout videoContainer;
    private VodVideoView videoView;
    private Bundle mBundle;
    private ISurfaceView surfaceView;
    private PlayPresent leShiPresent;
    private String  KEY_PLAY_VUID;
    private GridView gridView;
    private List<ListBean> listBeen=new ArrayList<>();
    private PlayAdapter playAdapter;
    private PlayListPresent playPresent;
    private int setPos;
    private CircularProgressBar progressBar;
    private int index=1;
    private LinearLayout layoutTitle;
    private MarTextView textViewTitle;
    private String title;
    private RelativeLayout layoutBotton;
    private ImageView imageViewScreen;
    private RelativeLayout view_con;
    private ImageView imagePlay;
    private VerticalSeekBar lightSeekBar;
    private float downPosX;
    private float downPosY;
    private GestureDetector detector;
    private int widthScreen;
    private int heightScreen;
    private AudioManager audiomanager;
    private float maxVolume;
    private Handler handlerTime=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                isVisiable=false;
                layoutTitle.setVisibility(View.GONE);
                layoutBotton.setVisibility(View.GONE);
            }
        }
    };
    private VoiceLoadView mVoiceLoadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //视频播放界面，保持屏幕常亮利于视频观看体验
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_le_shi);
        String video_id = getIntent().getStringExtra("video_id");
        setPos = getIntent().getIntExtra("pos",0);
        title = getIntent().getStringExtra("title");
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVoice=audiomanager.getStreamVolume( AudioManager.STREAM_MUSIC);
//        streamVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(streamVolume),0);

        getViews();
        addVideoView();
        leShiPresent = new PlayPresent(this,video_id);
        this.basePresent=leShiPresent;
        leShiPresent.getInfo();
        playPresent = new PlayListPresent(this);
        playPresent.getInfo(index);
        setListener();
        widthScreen = ScreenUtils.instance().getWidth(this);
        heightScreen = ScreenUtils.instance().getHeight(this);
        detector=new GestureDetector(this, Listener);
//        initPlay();
    }

    @Override
    public void getViews() {
        videoContainer = (RelativeLayout)findViewById(R.id.videoContainer);
        //无皮肤播放器请初始化
        videoView = new VodVideoView(this);
        layoutTitle = (LinearLayout) findViewById(R.id.title);
        layoutBotton = (RelativeLayout) findViewById(R.id.bottom_sheet);
        view_con = (RelativeLayout)findViewById(R.id.view_con);
        mVoiceLoadView = (VoiceLoadView) findViewById(R.id.voiceloadview);

        textViewTitle = (MarTextView) findViewById(R.id.text_title);
        imageViewScreen = (ImageView) findViewById(R.id.switchScreen);
        imagePlay = (ImageView) findViewById(R.id.play_video);
        lightSeekBar = (VerticalSeekBar) findViewById(R.id.lightSeek);
        lightSeekBar.setMax(100);
        gridView = (GridView) findViewById(R.id.grideview);
        progressBar = (CircularProgressBar) findViewById(R.id.circlrPro);
        playAdapter = new PlayAdapter(LeShiActivity.this,listBeen);
        gridView.setAdapter(playAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(gridView);
        textViewTitle.setText(title);

        try {
            mScreenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void LeSHiBack(View view) {
        if(isFullScreen){
            switchPortrait();
            switchPortraitVideoView();
            switchPortraitView();
            isFullScreen=false;
            imageViewScreen.setImageResource(R.mipmap.ic_halfscreen);
        }else {
            finish();
        }
    }
    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void getInfo(List<ListBean> list) {

    }
    public void getInfos(VideoBean videoBean) {
        KEY_PLAY_VUID=videoBean.getVideo_unique();
        initPlay();
    }

    public void getListInfo(List<ListBean> listBean) {
        if(listBean.size()>0){
            listBeen.addAll(listBean);
            playAdapter.setSectPos(setPos);
            playAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dismissDialogs() {

    }
    GestureDetector.OnGestureListener Listener=new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent event) {
            downPosX = event.getRawX();
            downPosY = event.getRawY();
            return false;

        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //分为向上滑和向下滑
            downPosX = e1.getRawX();
            if(downPosX<widthScreen/2){

                if(lightSeekBar.getVisibility()==View.GONE&&isFullScreen){
                    lightSeekBar.setVisibility(View.VISIBLE);
                }
                float movePosY = e2.getRawY();
                float distancY = downPosY - movePosY;
                float value = mScreenBrightness;
                value += (distancY / heightScreen) / 4;
                if (value < 0.1F) value = 0.1F;
                if (value > 1) value = 1F;
                if(isFullScreen){
                    changeBrightness(value);
                }

            }else if((widthScreen/2)<downPosX){
                if(mVoiceLoadView.getVisibility()==View.GONE&&isFullScreen){
                    mVoiceLoadView.setVisibility(View.VISIBLE);
                }
//                float movePosY = e2.getRawY();
//                float distancY = downPosY - movePosY;
                float value = currentVoice;

                if(value>=0&&distanceY>=10&&value<=maxVolume){
                    value++;
                }
                if(value>0&&distanceY<=-10){
                    value--;
                }
                if(isFullScreen){
                    updateVoice(value);
                }
            }
            return false;
        }


        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return false;
        }
    };

    /**
     *
     * @param value 声音
     */
    private void updateVoice(float value) {
        currentVoice=value;
        if(value<=maxVolume){
            mVoiceLoadView.setCurrentCount((int) (10*currentVoice/maxVolume));
            audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,(int) currentVoice,0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            lightSeekBar.setVisibility(View.GONE);
            mVoiceLoadView.setVisibility(View.GONE);
//            voice.setVisibility(View.GONE);
        }
        return super.dispatchTouchEvent(ev);
    }

    private float mScreenBrightness = 50f;
    private float currentVoice ;

    /**
     * 改变屏幕亮度
     *
     * @param value 0到1.0
     */
    private void changeBrightness(float value) {
            mScreenBrightness = value;
            WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
            layoutParams.screenBrightness = value;
            this.getWindow().setAttributes(layoutParams);
            updateLightProgress(value);
    }

    public void updateLightProgress(float light) {
        if (lightSeekBar != null) {
            lightSeekBar.setProgress((int) (light*100));
        }
    }

    @Override
    public void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KEY_PLAY_VUID=listBeen.get(position).getVideo_unique();
                progressBar.setVisibility(View.VISIBLE);
                if (videoView != null) {
                    videoView.resetPlayer();
                    mBundle=null;
                    mBundle = new Bundle();
                    mBundle.putString(PlayerParams.KEY_PLAY_UUID, "cnsmvgmgt8");
                    mBundle.putString(PlayerParams.KEY_PLAY_VUID,KEY_PLAY_VUID);
                    videoView.setDataSource(mBundle);
                    playAdapter.setSectPos(position);
                    playAdapter.notifyDataSetChanged();
                    textViewTitle.setText(listBeen.get(position).getVideo_name());
                }
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount==firstVisibleItem+visibleItemCount){
                    index++;
                    playPresent.getInfo(index);
                }
            }
        });
        //title 的显示或者隐藏
        videoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView();
//                visiableView();
            }
        });

        imageViewScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFullScreen){
                    switchLand();
                    switchLandVideoView();
                    switchLandView();
                    isFullScreen=true;
                    imageViewScreen.setImageResource(R.mipmap.ic_halfscreen);
                }else {
                    switchPortrait();
                    switchPortraitVideoView();
                    switchPortraitView();
                    isFullScreen=false;
                    imageViewScreen.setImageResource(R.mipmap.ic_fullscreen);
                }
            }
        });
        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlayering){
                    pauseVideo();//暂停
                    isPlayering=false;
                }else {
                    starVideo();//开始
                    isPlayering=true;
                }
            }
        });
    }
    //更新title和bottow
    private void updateView() {
        if(isVisiable){
            isVisiable=false;
            layoutTitle.setVisibility(View.GONE);
            layoutBotton.setVisibility(View.GONE);
            handlerTime.removeMessages(1);
        }else {
            isVisiable=true;
            handlerTime.sendEmptyMessageDelayed(1,6000);
            layoutTitle.setVisibility(View.VISIBLE);
            layoutBotton.setVisibility(View.VISIBLE);
        }
    }

    private void pauseVideo() {
        imagePlay.setImageResource(R.mipmap.ic_player);
        leShiPresent.pauseVideo();
    }

    private void starVideo() {
        imagePlay.setImageResource(R.mipmap.ic_pause);
        leShiPresent.startVideo();
    }

    private boolean isFullScreen=false;
    private boolean isPlayering=true;
    private boolean isVisiable=true;

    private void initPlay() {
        // Url可以是在线视频，也可以是本地视频
        //  String playPath = "/sdcard/demo.mp4"
//        videoView.setDataSource(playPath);
        mBundle = new Bundle();
        mBundle.putString(PlayerParams.KEY_PLAY_UUID, "cnsmvgmgt8");
        mBundle.putString(PlayerParams.KEY_PLAY_VUID,KEY_PLAY_VUID);
        videoView.setDataSource(mBundle);
        VideoViewListener videoViewListener = new VideoViewListener() {
            @Override
            public void onStateResult(int event, Bundle bundle) {
                handlePlayerEvent(event, bundle);
    //处理播放器事件
            }
            @Override
            public String onGetVideoRateList(LinkedHashMap<String, String> linkedHashMap) {
                return null;
            }
        };
        videoView.setVideoViewListener(videoViewListener);
    }

    /**
     * 处理播放器本身事件，具体事件可以参见IPlayer类
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {
            case PlayerEvent.PLAY_PREPARED:
                //播放器准备完成，此刻调用start()就可以进行播放了
                if (videoView != null) {
                    videoView.onStart();
                }
                if(progressBar!=null){
                    progressBar.setVisibility(View.GONE);
                }
                break;
            case PlayerEvent.PLAY_VIDEOSIZE_CHANGED:
                /**播放器返回视频的宽、高根据返回视频的宽、高计算出Surfaceview的大小(按比例缩放)*/
//                if (videoView != null && videoView instanceof ISurfaceView) {
//                    /**需要在UIVodVideoView中添加如下代码*/
//                     surfaceView = ((ISurfaceView)videoView).getSurfaceView();
//                    ((BaseSurfaceView) surfaceView).setDisplayMode(BaseSurfaceView.DISPLAY_MODE_SCALE_ZOOM);
//                    int width = bundle.getInt(PlayerParams.KEY_WIDTH);
//                    int height = bundle.getInt(PlayerParams.KEY_HEIGHT);
//                    ((BaseSurfaceView) surfaceView).onVideoSizeChanged(width, height);
//                }
                break;
            default:
                break;
        }
    }

    private void addVideoView() {
    //将播放器添加到容器中
    //在这儿，竖屏我们使用的是16:9的比例适配播放器界面
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width=ScreenUtils.instance().getWidth(getApplicationContext());
        params.width =width;
        params.height = width * 9 / 16;
        videoContainer.addView((View) videoView, params);
/**
 点播切换下一首
 */
//        Button next = (Button) findViewById(R.id.next);
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (videoView != null) {
//                    videoView.resetPlayer();
//                    mBundle = new Bundle();
//                    mBundle.putString(PlayerParams.KEY_PLAY_UUID, "cnsmvgmgt8");
//                    mBundle.putString(PlayerParams.KEY_PLAY_VUID,"8845c81835");
//                    videoView.setDataSource(mBundle);
//                }
//            }
//        });
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

        leShiPresent.onsubscriber();
        if(handlerTime!=null){
            handlerTime.removeMessages(1);
            handlerTime=null;
        }
    }
//在AndroidManifest.xml中Activity申明时，需要添加配置
//android:configChanges="keyboard|screenSize|orientation|layoutDirection"，以使该回调方法生效
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
    }

    public void switchPortrait() {
        //半屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) videoContainer.getLayoutParams();
        lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
        lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        videoContainer.setLayoutParams(lp);
    }

    public void switchPortraitVideoView() {
        //半屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) videoView.getLayoutParams();
        lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
        lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        videoView.setLayoutParams(lp);
    }

    public void switchPortraitView() {
        //半屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) view_con.getLayoutParams();
        lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
        lp.height = dp2px(this,220);
        view_con.setLayoutParams(lp);
    }

    public void switchLand() {
        //全屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) videoContainer.getLayoutParams();
        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        videoContainer.setLayoutParams(lp);
    }
    public void switchLandVideoView() {
        //全屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) videoView.getLayoutParams();
        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        videoView.setLayoutParams(lp);

    }
    public void switchLandView() {
        //全屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) view_con.getLayoutParams();
        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        view_con.setLayoutParams(lp);
    }

    @Override
    public void onBackPressed() {
        if(isFullScreen){
            switchPortrait();
            switchPortraitVideoView();
            switchPortraitView();
            isFullScreen=false;
            imageViewScreen.setImageResource(R.mipmap.ic_fullscreen);
        }else {
            finish();
//            super.onBackPressed();
        }
    }

    public static int dp2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
