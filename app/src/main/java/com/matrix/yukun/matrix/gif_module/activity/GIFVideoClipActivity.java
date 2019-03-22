package com.matrix.yukun.matrix.gif_module.activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.download_module.service.UIHandler;
import com.matrix.yukun.matrix.gif_module.adapter.RVVideoImageAdapter;
import com.matrix.yukun.matrix.gif_module.bean.VideoInfo;

import com.matrix.yukun.matrix.gif_module.utils.VideoClipUtils;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.video_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GIFVideoClipActivity extends BaseActivity implements View.OnClickListener {

    private List<VideoInfo> videoBitmapList=new ArrayList<>();
    private ImageView mIvBack;
    private String mVideoPath;
    private VideoView mVideoView;
    private ImageView mIvPlay;
    private SeekBar mSeekBar;
    private TextView mTvTime;
    private MediaMetadataRetriever mMetadataRetriever;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RVVideoImageAdapter mRvVideoImageAdapter;
    private RVVideoImageAdapter mRvGallaryImageAdapter;
    private RecyclerView mRvGallary;
    private LinearLayoutManager mLayoutManager;
    private TextView mTvClip;
    private RelativeLayout mLayout;
    private RelativeLayout mRlLayoutContain;
    private TextView mTvNum;
    private TextView mTvPro;
    private int mVideoWidth;
    private int mVideoHeight;

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mSeekBar.setProgress(mVideoView.getCurrentPosition());
                    mTvTime.setText(DataUtils.secToTime(mVideoView.getCurrentPosition()/1000)+"/"+ DataUtils.secToTime(mVideoView.getDuration()/1000));
                    mRvGallary.scrollToPosition(mVideoView.getCurrentPosition()/1000);
                    mHandler.sendEmptyMessageDelayed(1,1000);
                    break;
                case 2:
                    mTvNum.setText("加油奔跑中。。。("+videoBitmapList.size()+"/"+mVideoView.getDuration()/1000+")");
                    break;
            }
        }
    };

    public static void start(Context context,String videoPath){
        Intent intent=new Intent(context,GIFVideoClipActivity.class);
        intent.putExtra("path",videoPath);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_video;
    }

    @Override
    public void initView() {
        mVideoPath = getIntent().getStringExtra("path");
        mIvBack = findViewById(R.id.iv_back);
        mVideoView = findViewById(R.id.videoview);
        mIvPlay = findViewById(R.id.iv_play);
        mRlLayoutContain = findViewById(R.id.rl_play_contain);
        mSeekBar = findViewById(R.id.seekbar);
        mTvTime = findViewById(R.id.tv_time);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRvGallary = findViewById(R.id.rv_gallary);
        mTvClip = findViewById(R.id.tv_clip);
        mLayout = findViewById(R.id.rl_load);
        mTvNum = findViewById(R.id.tv_num);
        mTvPro = findViewById(R.id.tv_product_gif);
    }

    @Override
    public void initDate() {
        mLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRvVideoImageAdapter = new RVVideoImageAdapter(this,videoBitmapList);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRvVideoImageAdapter);
        mRecyclerView.addItemDecoration(new SpacesDoubleDecoration(2,1,1,2));
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRvGallaryImageAdapter = new RVVideoImageAdapter(this,videoBitmapList);
        mRvGallary.setLayoutManager(mLayoutManager);
        mRvGallaryImageAdapter.setImageWidth(ScreenUtil.dip2px(40));
        mRvGallary.setAdapter(mRvGallaryImageAdapter);
        mRecyclerView.addItemDecoration(new SpacesDoubleDecoration(2,1,1,2));
        mVideoView.setVideoURI(Uri.fromFile(new File(mVideoPath)));
        mLayout.setVisibility(View.VISIBLE);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBar.setMax(mp.getDuration());
                createVideoBitmap();
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                mHandler.sendEmptyMessage(1);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mRlLayoutContain.setVisibility(View.VISIBLE);
                mHandler.removeMessages(1);
            }
        });
    }

    private void createVideoBitmap() {
        videoBitmapList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMetadataRetriever=new MediaMetadataRetriever();
                mMetadataRetriever.setDataSource(mVideoPath);
                for (int i = 0; i < mVideoView.getDuration() / 1000; i++) {
                    VideoInfo videoInfo=new VideoInfo(i*1000,extractFrame(i*1000,mVideoView.getDuration()/1000));
                    videoBitmapList.add(videoInfo);
                    mHandler.sendEmptyMessage(2);
                }
                mMetadataRetriever.release();
                UIHandler.run(new Runnable() {
                    @Override
                    public void run() {
                        mLayout.setVisibility(View.GONE);
                        if(mRvVideoImageAdapter!=null && videoBitmapList.size()>0){
                            mRvVideoImageAdapter.notifyDataSetChanged();
                            mRvGallaryImageAdapter.notifyDataSetChanged();
                            mIvPlay.setImageBitmap(videoBitmapList.get(0).getBitmap());
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void initListener() {
        mIvPlay.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvClip.setOnClickListener(this);
        mTvPro.setOnClickListener(this);
        mRvVideoImageAdapter.setOnClickListener(new RVVideoImageAdapter.OnClickListener() {
            @Override
            public void onCliclk(VideoInfo videoInfo,int position) {
                mVideoView.seekTo(position*1000);
                mSeekBar.setProgress(position*1000);
                mTvTime.setText(DataUtils.secToTime(mVideoView.getCurrentPosition()/1000)+"/"+ DataUtils.secToTime(mVideoView.getDuration()/1000));
                devideClickPos(videoInfo,position);
                ToastUtils.showToast(videoInfo.getTimeMs()+"s");
                mRvVideoImageAdapter.notifyDataSetChanged();
                mRvGallaryImageAdapter.notifyDataSetChanged();
            }
        });
        mRvGallaryImageAdapter.setOnClickListener(new RVVideoImageAdapter.OnClickListener() {
            @Override
            public void onCliclk(VideoInfo videoInfo, int position) {

            }
        });

        //seek
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    seekBar.setProgress(progress);
                    mVideoView.seekTo(progress);
                    mTvTime.setText(DataUtils.secToTime(mVideoView.getCurrentPosition()/1000)+"/"+ DataUtils.secToTime(mVideoView.getDuration()/1000));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_play:
                mVideoView.start();
                mRlLayoutContain.setVisibility(View.GONE);
                break;
            case R.id.tv_clip:
                clipVideo();
                break;
            case R.id.tv_product_gif:
                if(mVideoView.getDuration()/1000>11){
                    ToastUtils.showToast("视频超过10s制作时间会很长");
                }else {
                    GIFVideoProActivity.start(this,mVideoPath,mVideoView.getDuration(),mVideoWidth,mVideoHeight);
                }

                break;
        }
    }
    private void devideClickPos(VideoInfo videoInfo, int pos) {
        boolean hasStart = false;
        boolean hasEnd = false;
        int start=0;
        int end=0;

        //判断是否有已选
        for (int i = 0; i < videoBitmapList.size(); i++) {
            videoBitmapList.get(i).setChooseCenter(false);
            if(videoBitmapList.get(i).isChooseStart()){
                hasStart=true;
                start=i;
            }
            if(videoBitmapList.get(i).isChooseEnd()){
                hasEnd=true;
                end=i;
            }
        }
        //已选的点击
        if(videoInfo.isChooseStart()){
            videoBitmapList.get(pos).setChooseStart(false);
            return;
        }
        if(videoInfo.isChooseEnd()){
            videoBitmapList.get(pos).setChooseEnd(false);
            return;
        }
        if(!hasStart&&!hasEnd){ //没有开始,没有结束
            videoBitmapList.get(pos).setChooseStart(true);
            videoBitmapList.get(pos).setTimeMs(videoInfo.getTimeMs());
        }else if(hasStart&&!hasEnd){//有开始,没有结束
            if(pos<start){
                videoBitmapList.get(pos).setChooseStart(true);
                videoBitmapList.get(start).setChooseStart(false);
                videoBitmapList.get(start).setChooseEnd(true);
                videoBitmapList.get(start).setTimeMs(videoInfo.getTimeMs());
            }else if(pos>start){
                videoBitmapList.get(pos).setChooseEnd(true);
                videoBitmapList.get(pos).setTimeMs(videoInfo.getTimeMs());
            }
        }else if(hasStart && hasEnd){//有开始,有结束
            if(pos<end){
                videoBitmapList.get(pos).setChooseStart(true);
                videoBitmapList.get(start).setChooseStart(false);
                videoBitmapList.get(pos).setTimeMs(videoInfo.getTimeMs());
            }else if(pos>end){
                videoBitmapList.get(pos).setChooseEnd(true);
                videoBitmapList.get(end).setChooseEnd(false);
                videoBitmapList.get(pos).setTimeMs(videoInfo.getTimeMs());
            }
        }else if(!hasStart && hasEnd){//没有开始,有结束
            if(pos<end){
                videoBitmapList.get(pos).setChooseStart(true);
                videoBitmapList.get(pos).setTimeMs(videoInfo.getTimeMs());
            }else if(pos>end){
                videoBitmapList.get(pos).setChooseEnd(true);
                videoBitmapList.get(end).setChooseStart(true);
                videoBitmapList.get(end).setChooseEnd(false);
                videoBitmapList.get(end).setTimeMs(videoInfo.getTimeMs());
            }
        }
        //判断是否有已选
        for (int i = 0; i < videoBitmapList.size(); i++) {
            if(videoBitmapList.get(i).isChooseStart()){
                hasStart=true;
                start=i;
            }
            if(videoBitmapList.get(i).isChooseEnd()){
                hasEnd=true;
                end=i;
            }
        }
        //设置中间值
        if(hasStart&&hasEnd){
            for (int i = 0; i < videoBitmapList.size(); i++) {
                if(i>start&&i<end){
                    videoBitmapList.get(i).setChooseCenter(true);
                }
            }
        }
    }

    public Bitmap extractFrame(long timeMs,int fileLength) {
        //第一个参数是传入时间，只能是us(微秒)
        //OPTION_CLOSEST ,在给定的时间，检索最近一个帧,这个帧不一定是关键帧。
        //OPTION_CLOSEST_SYNC   在给定的时间，检索最近一个同步与数据源相关联的的帧（关键帧）
        //OPTION_NEXT_SYNC 在给定时间之后检索一个同步与数据源相关联的关键帧。
        //OPTION_PREVIOUS_SYNC 在给定时间之前检索一个同步与数据源相关联的关键帧。
        Bitmap bitmap = mMetadataRetriever.getFrameAtTime(timeMs * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        return bitmap;
    }

    public void clipVideo(){
        File file=new File(AppConstant.GIFVIDEO);
        String fileName="yk_video_"+System.currentTimeMillis()+".mp4";
        double startTime=-1;
        double endTime=-1;
        if(!file.exists()){
            file.mkdirs();
        }
        for (int i = 0; i < videoBitmapList.size(); i++) {
            if(videoBitmapList.get(i).isChooseStart()){
                startTime=videoBitmapList.get(i).getTimeMs();
            }
            if(videoBitmapList.get(i).isChooseEnd()){
                endTime=videoBitmapList.get(i).getTimeMs();
            }
        }
        if(startTime==-1||endTime==-1){
            ToastUtils.showToast("请选择裁剪时长");
            return;
        }
        try {
            LogUtil.i("=========",startTime+" "+endTime+" "+mVideoPath.length());
            if(endTime<=0){
                ToastUtils.showToast("裁剪时长太短");
                return;
            }
            VideoClipUtils.clip(mVideoPath, AppConstant.GIFVIDEO+"/"+fileName, startTime, endTime);
            ToastUtils.showToast("裁剪成功，保存到"+AppConstant.GIFVIDEO+"文件夹下");
            //update新的数据
            mVideoPath=null;
            mVideoPath=AppConstant.GIFVIDEO+"/"+fileName;
            mVideoView.setVideoURI(Uri.fromFile(new File(mVideoPath)));
            mLayout.setVisibility(View.VISIBLE);
            createVideoBitmap();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(mHandler!=null){
            mHandler.removeMessages(1);
            mHandler=null;
        }
        super.onDestroy();
    }
}
