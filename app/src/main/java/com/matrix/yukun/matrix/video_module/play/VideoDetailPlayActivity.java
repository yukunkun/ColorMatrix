package com.matrix.yukun.matrix.video_module.play;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.service.DownLoadEngine;
import com.matrix.yukun.matrix.download_module.service.DownLoadManager;
import com.matrix.yukun.matrix.download_module.service.DownLoadServiceImpl;
import com.matrix.yukun.matrix.download_module.service.DownloadNotificationService;
import com.matrix.yukun.matrix.task.LogUtils;
import com.matrix.yukun.matrix.util.NetStates;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.entity.EventVideo;
import com.matrix.yukun.matrix.video_module.entity.EyesInfo;
import com.matrix.yukun.matrix.video_module.entity.HistoryPlay;
import com.matrix.yukun.matrix.video_module.fragment.VideoDetailComment;
import com.matrix.yukun.matrix.video_module.fragment.VideoDetailFragment;
import com.matrix.yukun.matrix.video_module.fragment.VideoListFragment;
import com.matrix.yukun.matrix.video_module.utils.SPUtils;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

public class VideoDetailPlayActivity extends BaseActivity {

    private String mNextUrl;
    private int mType;
    private EyesInfo mEyesInfo;
    private JZVideoPlayerStandard mVideoView;
    private ImageView mImCover;
    private RelativeLayout mRlBg;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList=new ArrayList<>();
    private String[] mStrings;
    private FloatingActionButton mFloatingActionButton;

    public static void start(Context context, EyesInfo eyesInfo, String nextUrl){
        Intent intent=new Intent(context,VideoDetailPlayActivity.class);
        intent.putExtra("next_url",nextUrl);
        intent.putExtra("eyesInfo",eyesInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_video_detail_play;
    }

    @Override
    public void initView() {
        mVideoView = findViewById(R.id.jzvps_player);
        mImCover = findViewById(R.id.iv_cover);
        mRlBg = findViewById(R.id.rl_bg);
        mTabLayout = findViewById(R.id.tl_video);
        mViewPager = findViewById(R.id.vp_video);
        mFloatingActionButton = findViewById(R.id.bt_download);
    }

    @Override
    public void initDate() {
        Intent intent = getIntent();
        mEyesInfo = (EyesInfo) intent.getSerializableExtra("eyesInfo");
        mNextUrl = intent.getStringExtra("next_url");
        mType = intent.getIntExtra("type", 0);
        if(mType!=1&&mType!=2){
            mStrings = getResources().getStringArray(R.array.video_detail);
        }else {
            mStrings = getResources().getStringArray(R.array.video_detail_comment);
        }
        mVideoView.setUp(mEyesInfo.getData().getPlayUrl(), JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, mEyesInfo.getData().getTitle());
        //网络
        Glide.with(this).load(mEyesInfo.getData().getCover().getDetail()).into(mImCover);
        mRlBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mRlBg.setVisibility(View.GONE);
                saveToDB();
                return false;
            }
        });

        for (int i = 0; i < mStrings.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mStrings[i]));
        }
        VideoDetailFragment videoDetailFragment=VideoDetailFragment.getInstance(mEyesInfo,mNextUrl,mType);
        VideoListFragment videoListFragment=VideoListFragment.getInstance(mEyesInfo,mNextUrl,mType);
        VideoDetailComment videoDetailComment= VideoDetailComment.getInstance((ArrayList<EyesInfo.DataBean.TagsBean>) mEyesInfo.getData().getTags());
        mFragmentList.add(videoDetailFragment);
        if(mType==1||mType==2){
            mFragmentList.add(videoDetailComment);
        }
        mFragmentList.add(videoListFragment);
        MViewPagerAdapter mViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mStrings);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        boolean iswifi = SPUtils.getInstance().getBoolean("iswifi");
        if(iswifi&&!NetStates.isWIFIConnected(this)){
            showDialog();
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("播放设置")
                .setMessage("当前设置是仅仅wifi播放，是否重置并播放")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton("播放", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.getInstance().saveBoolean("iswifi",false);
                    }
                }).show();
    }

    private void saveToDB(){
        List<HistoryPlay> historyPlays = DataSupport.where("play_url = ?", mEyesInfo.getData().getPlayUrl()).find(HistoryPlay.class);
        if(historyPlays.size()==0){
            HistoryPlay.setHistoryInfo(mEyesInfo,mNextUrl,mType);
        }
    }

    @Override
    public void initListener() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加到下载
                DownloadNotificationService.getInstance().start(getApplicationContext());
                DownloadNotificationService.getInstance().startDownload(mEyesInfo.getData().getPlayUrl(),mEyesInfo.getData().getCover().getDetail());
                ToastUtils.showToast("添加到下载列表成功");
            }
        });
    }
    @Override
    public void onBackPressed() {
        boolean iswifi = SPUtils.getInstance().getBoolean("iswifi");
        if(iswifi&&!NetStates.isWIFIConnected(this)){
        }else {
            EventBus.getDefault().post(new EventVideo(mEyesInfo,mType,mNextUrl));
        }
        if (mVideoView.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.releaseAllVideos();
    }
}
