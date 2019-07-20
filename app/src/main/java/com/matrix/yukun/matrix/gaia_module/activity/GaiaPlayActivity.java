package com.matrix.yukun.matrix.gaia_module.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.bean.MaterialDetailInfo;
import com.matrix.yukun.matrix.gaia_module.bean.VideoDetailInfo;
import com.matrix.yukun.matrix.gaia_module.bean.VideoType;
import com.matrix.yukun.matrix.gaia_module.fragment.DetailFragment;
import com.matrix.yukun.matrix.gaia_module.gaiaplayer.GaiaJzvdStd;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.gaia_module.net.VideoUtils;
import com.matrix.yukun.matrix.gaia_module.util.EventInitView;
import com.matrix.yukun.matrix.gaia_module.util.EventPosition;
import com.matrix.yukun.matrix.gaia_module.util.FileDelete;
import com.matrix.yukun.matrix.gaia_module.util.M3U8Service;
import com.matrix.yukun.matrix.gaia_module.util.VideoControl;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class GaiaPlayActivity extends BaseActivity {

    @BindView(R.id.gaia_player)
    GaiaJzvdStd gaiaPlayer;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    private long mId;
    private int mType;
    private VideoDetailInfo mDetailInfo;
    private MaterialDetailInfo mMaterialInfo;
    private String serviceUri;
    private VideoControl videoControl;
    private List<String> tsString;
    private String mCover;

    public static void start(Context context, long id, int type, String cover) {
        Intent intent = new Intent(context, GaiaPlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", id);
        intent.putExtra("type", type);  // 0 作品 1 素材
        intent.putExtra("cover", cover);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_gaia_play;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mId = getIntent().getLongExtra("id", 0);
        mType = getIntent().getIntExtra("type", 0);
        mCover = getIntent().getStringExtra("cover");
        initNetUtil();
    }

    private void initNetUtil() {
        boolean portAvailable = M3U8Service.isPortAvailable(M3U8Service.PORT);
        if (portAvailable) {
            M3U8Service.execute();//再打开
        }
        String format = String.format("http://localhost:%d", M3U8Service.PORT);
        serviceUri = format + "/yukun/gaia/test.m3u8";
        videoControl = new VideoControl();
    }

    private int initView = 0;

    //下载完成,播放的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPlay(EventPosition event) {
        videoControl.setDownloadTag(event.position);//下载一次就加入一个值
        tsString = videoControl.getTsString();
        if (tsString.size() > 3) {
            if (event.position < tsString.size()) {
                if (event.position == 2 + initView) {
                    videoControl.setHandlerStart(1);
                }
                videoControl.load();
            }
        }
        //只有两片的视频
        if (tsString.size() == 2) {
            if (event.position < tsString.size()) {
                if (event.position == 1 + initView) {
                    videoControl.setHandlerStart(2);
                }
                videoControl.load();
            }
        }
        //只有两片的视频
        if (tsString.size() == 3) {
            if (event.position < tsString.size()) {
                if (event.position == 1 + initView) {
                    videoControl.setHandlerStart(4);
                }
                videoControl.load();
            }
        }
    }

    //下载好了,初始化播放器
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInitView(EventInitView event) {
        if (event.play == 1) {
            gaiaPlayer.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.GONE);
            if(mDetailInfo!=null){
                gaiaPlayer.setUp(serviceUri, mDetailInfo.getWorks().getName(), GaiaJzvdStd.SCREEN_NORMAL, this, mCover);
            }else {
                gaiaPlayer.setUp(serviceUri, mMaterialInfo.getMaterial().getName(), GaiaJzvdStd.SCREEN_NORMAL, this, mCover);
            }
            gaiaPlayer.startVideo();
            gaiaPlayer.setVideoDuration(videoControl.getMaxTime());
        }
    }

    private void downloadVideoUrl(String uri) {
        if (!TextUtils.isEmpty(uri)) {
            OkHttpUtils.get().url(uri).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception error, int id) {
                    Log.i("-------m3u8error", error.toString());
                }

                @Override
                public void onResponse(String response, int id) {
                    videoControl.setM3u8String(response);
                }
            });
        }
    }

    @Override
    public void initDate() {
        if (mType == VideoType.WORK.getType()) {
            VideoUtils.getWorkVideo((int) mId, new VideoUtils.GetVideoListener() {
                @Override
                public void getVideo(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        mDetailInfo = new Gson().fromJson(jsonObject.optJSONObject("o").toString(), VideoDetailInfo.class);
                        initVideoView();
//                        Glide.with(GaiaPlayActivity.this).load(Api.COVER_PREFIX+).into(gaiaPlayer.thumbImageView);
                        downloadVideoUrl(VideoUtils.selectDefaultUri(mDetailInfo));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void getError(String error) {
                    LogUtil.i(error);
                }
            });
        } else {
            VideoUtils.getMaterialVideo((int) mId, new VideoUtils.GetVideoListener() {
                @Override
                public void getVideo(String data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        mMaterialInfo = new Gson().fromJson(jsonObject.optJSONObject("o").toString(), MaterialDetailInfo.class);
                        initMaterialVide();
                        Glide.with(GaiaPlayActivity.this).load(Api.COVER_PREFIX + mMaterialInfo.getMaterial().getScreenshot()).into(gaiaPlayer.thumbImageView);
                        downloadVideoUrl(VideoUtils.selectDefaultUri(mMaterialInfo));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void getError(String error) {
                    LogUtil.i(error);
                }
            });
        }

    }

    private void initVideoView() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl, DetailFragment.instance(mId, mDetailInfo)).commitAllowingStateLoss();
    }

    private void initMaterialVide() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl, DetailFragment.instance(mId, mMaterialInfo)).commitAllowingStateLoss();
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        FileDelete.deleteAll();
        videoControl.setHandlerStop(3);
        if (videoControl != null) {
            //取消计时
            videoControl.setdownThreadCon();
        }
        videoControl.setHandlerStop(3);
        gaiaPlayer.reset();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
