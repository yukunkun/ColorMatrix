package com.matrix.yukun.matrix.video_module.fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.adapter.EyeRecAdapter;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.video_module.entity.EventCategrayPos;
import com.matrix.yukun.matrix.video_module.entity.EventVideo;
import com.matrix.yukun.matrix.video_module.entity.EyesInfo;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.play.VideoDetailPlayActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.matrix.yukun.matrix.video_module.video.VideoPlayActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class RecFragment extends BaseFragment implements View.OnClickListener, EyeRecAdapter.ShareCallBack {
    String url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected?num=5&page=0";
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    List<EyesInfo> eyesInfos=new ArrayList<>();
    @BindView(R2.id.rl_video_contain)
    RelativeLayout mLayoutVideo;
    @BindView(R2.id.jzps_player)
    VideoView mVideoView;
    @BindView(R2.id.iv_close_video)
    ImageView mIvCloseVideo;
    @BindView(R2.id.iv_play_video)
    ImageView mIvVideoPlay;
    @BindView(R2.id.rl_remind)
    RelativeLayout mLayoutBg;
    @BindView(R2.id.tv_remind)
    TextView mTvRemind;
    private int page;
    private EyeRecAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;
    private EventVideo mEventVideo;
    private boolean isRefresh=true;

    public static RecFragment getInstance() {
        RecFragment recFragment = new RecFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mLayoutManager = new LinearLayoutManager(getContext());
        mTvRemind.setText("加油奔跑中。。。");
        mLayoutBg.setVisibility(View.VISIBLE);
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new EyeRecAdapter(getContext(),eyesInfos);
        mJokeAdapter.setShareCallBack(this);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
        mSw.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,android.R.color.black,
                 android.R.color.holo_red_light, android.R.color.holo_orange_light
                );
    }

    //分享
    @Override
    public void onShareCallback(int pos) {
        EyesInfo eyesInfo = eyesInfos.get(pos);
        ShareDialog shareDialog=ShareDialog.getInstance(eyesInfo.getData().getTitle(),eyesInfo.getData().getWebUrl().getForWeibo(),eyesInfo.getData().getCover().getDetail());
        shareDialog.show(getChildFragmentManager(),"");
    }

    @Override
    public void onItemClickListener(EyesInfo eyesInfo,View view) {
        Intent intent = new Intent(getContext(), VideoDetailPlayActivity.class);
        intent.putExtra("eyesInfo",eyesInfo);
        intent.putExtra("next_url",url);
        intent.putExtra("type",1);//小视频界面要用
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
            getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(),view,"shareView").toBundle());
        }else {
            getContext().startActivity(intent);
            ((Activity)getContext()).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
        }
    }

    @Override
    public void onItemCollectClickListener(EyesInfo eyesInfo, View view) {
        CollectsInfo.setCollectInfo(eyesInfo,url);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=0;
                url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected?num=5&page=0";
                eyesInfos.clear();
                mJokeAdapter.notifyDataSetChanged();
                getInfo();
                mSw.setRefreshing(false);
            }
        });

        mRvJoke.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                        page++;
                        getInfo();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mJokeAdapter.getCategroyPos(new EyeRecAdapter.CategroyCallBack() {
            @Override
            public void choosePos(int pos) {
                EventBus.getDefault().post(new EventCategrayPos(pos));
            }
        });
        mIvCloseVideo.setOnClickListener(this);
        mIvVideoPlay.setOnClickListener(this);
        // 不知道为什么 ，魅族使用 videoview.setOnTouchListener没反应，其他手机可以
        mLayoutVideo.setOnTouchListener(new View.OnTouchListener() {
            private float mX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                     case MotionEvent.ACTION_DOWN://0
                         mX = event.getX();
                         return false;
                     case MotionEvent.ACTION_UP://1
                         startPlayActivity();
                         break;
                     case MotionEvent.ACTION_MOVE://2
                         //右滑动移除
                         float  x = event.getX();
                         if(x-mX>25){
                             setRemoveAnimation();
                         }
                        break;
                }
                return true;
            }
        });
    }

    private void startPlayActivity() {
        Intent intent = new Intent(getContext(), VideoDetailPlayActivity.class);
        intent.putExtra("eyesInfo",mEventVideo.mEyesInfo);
        intent.putExtra("next_url",url);
        intent.putExtra("type",1);//小视频界面要用
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
            getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(),mLayoutVideo,"shareView").toBundle());
        }else {
            getContext().startActivity(intent);
            ((Activity)getContext()).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
        }
    }

    /**
     * video移动的动画
     */
    private void setRemoveAnimation() {
        TranslateAnimation translateAnimation=new TranslateAnimation(0,mLayoutVideo.getWidth(),0,0);
        translateAnimation.setDuration(1000);
        mLayoutVideo.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mVideoView.stopPlayback();
                mLayoutVideo.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getInfo() {
        if(TextUtils.isEmpty(url) || url.equals("null")){
            ToastUtils.showToast("没有更多了");
            return;
        }
        NetworkUtils.networkGet(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //断网，加载本地
                List<EyesInfo> all = DataSupport.findAll(EyesInfo.class);
                if(all.size()>0){
                    eyesInfos.addAll(all);
                    mJokeAdapter.notifyDataSetChanged();
                    mLayoutBg.setVisibility(View.GONE);
                    mSw.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Gson gson=new Gson();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray itemList = jsonObject.optJSONArray("itemList");
                    if(itemList!=null&&itemList.length()>0){
                        List<EyesInfo> jokeList = gson.fromJson(itemList.toString(), new TypeToken<List<EyesInfo>>(){}.getType());
                        eyesInfos.addAll(jokeList);
                        for (int i = 0; i < jokeList.size(); i++) {
                            if(!jokeList.get(i).getType().equals("video")){
                                eyesInfos.remove(jokeList.get(i));
                            }
                        }
                        url=jsonObject.optString("nextPageUrl");
                        mJokeAdapter.notifyDataSetChanged();
                        mLayoutBg.setVisibility(View.GONE);
                        //存储
                        if(isRefresh){
                            isRefresh=false;
                            DataSupport.deleteAll(EyesInfo.class);
                            for (int i = 0; i < eyesInfos.size(); i++) {
                                eyesInfos.get(i).setCover(eyesInfos.get(i).getData().getCover().getDetail());
                                eyesInfos.get(i).setIcon(eyesInfos.get(i).getData().getAuthor().getIcon());
                                eyesInfos.get(i).setCategory(eyesInfos.get(i).getData().getCategory());
                                eyesInfos.get(i).setSlogan(eyesInfos.get(i).getData().getSlogan());
                                eyesInfos.get(i).setDescription(eyesInfos.get(i).getData().getDescription());
                                eyesInfos.get(i).save();
                            }
                        }
                    }
                    mSw.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateVideo(EventVideo eventVideo){
        if(eventVideo.mEyesInfo.getData().getPlayUrl()!=null && eventVideo.type==1){
            mEventVideo=eventVideo;
            mLayoutVideo.setVisibility(View.VISIBLE);
            mVideoView.setVideoURI(Uri.parse(eventVideo.mEyesInfo.getData().getPlayUrl()));
            mVideoView.start();
            mIvVideoPlay.setImageResource(R.mipmap.icon_video_play);
        }
    }

    public void getCurrentSelectViewPager(int position){
        if(mLayoutVideo!=null){
            if(mLayoutVideo.getVisibility()!=View.GONE){
                if(position==0){
                    updatePlayButton(false);
                }else{
                    updatePlayButton(true);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_video:
                setRemoveAnimation();
                break;
            case R.id.iv_play_video:
                updatePlayButton(mVideoView.isPlaying());
                break;
        }
    }

    private void updatePlayButton(boolean playing) {
        if(playing){
            mIvVideoPlay.setImageResource(R.mipmap.icon_video_pause);
            mVideoView.pause();
        }else {
            mIvVideoPlay.setImageResource(R.mipmap.icon_video_play);
            mVideoView.start();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
