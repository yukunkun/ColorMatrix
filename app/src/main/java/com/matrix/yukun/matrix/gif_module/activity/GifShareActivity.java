package com.matrix.yukun.matrix.gif_module.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GifShareActivity extends BaseActivity implements View.OnClickListener {


    private String mRealPathFromURI;
    private GifImageView mGifView;
    private GifDrawable mGifDrawable;
    private ImageView mIvBack;
    private ImageView mIvPlay;
    private SeekBar mSeekBar;
    private ImageView mIvShare;
    private TextView mTvTitle;
    private LinearLayout mLayout;
    private RelativeLayout mRlayout;
    private TextView mTvTime;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                mTvTime.setText(DataUtils.secToTime(mGifDrawable.getCurrentPosition()/1000)+"/"+DataUtils.secToTime(mGifDrawable.getDuration()/1000));
                mHandler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    @Override
    public int getLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_gif_share;
    }

    @Override
    public void initView() {
        mGifView = findViewById(R.id.giv1);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mIvPlay = findViewById(R.id.iv_control);
        mSeekBar = findViewById(R.id.seekbar);
        mIvShare = findViewById(R.id.iv_share);
        mTvTime = findViewById(R.id.tv_time);
        mLayout = findViewById(R.id.ll_con);
        mRlayout = findViewById(R.id.rl_con);
        mSeekBar.setMax(15);
        mSeekBar.setProgress(1);
    }

    @Override
    public void initDate() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();
        // 判断Intent是否是“分享”功能(Share Via)
        if (Intent.ACTION_SEND.equals(action)) {
            // 获取资源路径Uri
            Uri uri = extras.getParcelable(Intent.EXTRA_STREAM);
            //解析Uri资源GIF
            mRealPathFromURI = getRealPathFromURI(uri);
            if(!TextUtils.isEmpty(mRealPathFromURI)){
                try {
                    if(!mRealPathFromURI.endsWith("gif")) {
                        ImageDetailActivity.start(this,mRealPathFromURI,false);
                        finish();
                    }
                    mGifDrawable = new GifDrawable(new File(mRealPathFromURI));
                    mGifView.setImageDrawable(mGifDrawable);//这里是实际决定资源的地方，优先级高于xml文件的资源定义
                    mGifDrawable.start();
                    mGifDrawable.setLoopCount(0);
                    mTvTitle.setText(mRealPathFromURI);
                    mTvTime.setText(DataUtils.secToTime(mGifDrawable.getDuration()/1000));
                    mHandler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                finish();
            }
        }
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mGifView.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mGifDrawable.setSpeed(progress+1);
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
    /**
     * 通过Uri获取文件在本地存储的真实路径
     */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor=getContentResolver().query(contentUri, proj, null, null, null);
        try{
            if(cursor.moveToNext()){
                return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            }
        }catch (Exception e){
        }

        cursor.close();
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                ShareDialog shareDialog = ShareDialog.getImageInstance(mRealPathFromURI, AppConstant.APP_STORE);
                shareDialog.show(getSupportFragmentManager(),"");
                break;
            case R.id.iv_download:
                File file = new File(AppConstant.GIFLOAD);
                if (!file.exists()){
                    file.mkdirs();
                }
                break;
            case R.id.iv_control:
                if(mGifDrawable.isPlaying()){
                    mGifDrawable.pause();
                    mIvPlay.setImageResource(R.mipmap.icon_video_play);
                }else {
                    mGifDrawable.start();
                    mIvPlay.setImageResource(R.mipmap.icon_video_pause);
                }
                break;
            case R.id.giv1:
                if(mLayout.getVisibility()==View.GONE){
                    mLayout.setVisibility(View.VISIBLE);
                    mRlayout.setVisibility(View.VISIBLE);
                }else {
                    mLayout.setVisibility(View.GONE);
                    mRlayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeMessages(1);
            mHandler=null;
        }
    }
}
