package com.matrix.yukun.matrix.leshi_module;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.surfaceview.ISurfaceView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.sdk.videoview.vod.VodVideoView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshi_module.bean.VideoBean;
import com.matrix.yukun.matrix.leshi_module.present.LeShiListImple;
import com.matrix.yukun.matrix.leshi_module.present.LeShiPresent;
import com.matrix.yukun.matrix.leshi_module.present.PlayAdapter;
import com.matrix.yukun.matrix.leshi_module.present.PlayListPresent;
import com.matrix.yukun.matrix.leshi_module.present.PlayPresent;
import com.matrix.yukun.matrix.movie_module.MovieBaseActivity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //视频播放界面，保持屏幕常亮利于视频观看体验
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_shi);
        String video_id = getIntent().getStringExtra("video_id");
        setPos = getIntent().getIntExtra("pos",0);

        init();
        addVideoView();
        setListener();
        leShiPresent = new PlayPresent(this,video_id);
        this.basePresent=leShiPresent;
        leShiPresent.getInfo();
        playPresent = new PlayListPresent(this);
        playPresent.getInfo();
//        initPlay();
    }

    private void init() {
        videoContainer = (RelativeLayout)findViewById(R.id.videoContainer);
        //无皮肤播放器请初始化
        videoView = new VodVideoView(this);
        gridView = (GridView) findViewById(R.id.grideview);
        progressBar = (CircularProgressBar) findViewById(R.id.circlrPro);
        playAdapter = new PlayAdapter(getApplicationContext(),listBeen);
        gridView.setAdapter(playAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(gridView);
    }

    public void LeSHiBack(View view) {
        finish();
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
        listBeen.addAll(listBean);
        playAdapter.notifyDataSetChanged();
        playAdapter.setSectPos(setPos);
    }

    @Override
    public void dismissDialogs() {

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
                }
            }
        });
    }


    @Override
    public void getViews() {

    }
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
        params.width = wm.getDefaultDisplay().getWidth();
        int width=ScreenUtils.instance().getWidth(getApplicationContext());
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


}
