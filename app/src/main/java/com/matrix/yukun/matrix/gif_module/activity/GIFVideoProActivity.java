package com.matrix.yukun.matrix.gif_module.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.download_module.service.UIHandler;
import com.matrix.yukun.matrix.gif_module.utils.gifencoder.GifEncoderListener;
import com.matrix.yukun.matrix.gif_module.utils.gifencoder.GifExtractor;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.vincent.videocompressor.VideoCompress;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GIFVideoProActivity extends BaseActivity implements View.OnClickListener, GifEncoderListener {

    private String mPath;
    private int mWidth;
    private int mHeight;
    private ImageView mIvBack;
    private TextView mTvSave;
    private FloatingActionButton mFlbShare;
    private GifImageView mGifImageView;
    private SeekBar mSeekBar;
    private String mGifPath;
    private int mVideoDuration;
    private RelativeLayout mLayout;
    private TextView mTvLoad;
    private GifDrawable mGifDrawable;

    public static void start(Context context, String path, int duration,int width, int height){
        Intent intent=new Intent(context,GIFVideoProActivity.class);
        intent.putExtra("path",path);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        intent.putExtra("duration",duration);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_gifvideo_pro;
    }

    @Override
    public void initView() {
        mPath = getIntent().getStringExtra("path");
        mWidth = getIntent().getIntExtra("width", 480);
        mHeight = getIntent().getIntExtra("height", 320);
        mVideoDuration = getIntent().getIntExtra("duration",0);
        mIvBack = findViewById(R.id.iv_back);
        mTvSave = findViewById(R.id.tv_pro);
        mFlbShare = findViewById(R.id.btn_share);
        mLayout = findViewById(R.id.rl_load);
        mTvLoad = findViewById(R.id.tv_load);
        mGifImageView = findViewById(R.id.giv1);
        mSeekBar = findViewById(R.id.seekbar);
        mGifPath= AppConstant.GIFLOAD+"/yk_gif_"+System.currentTimeMillis()+".gif";
        File file=new File(mGifPath);
        if(file.exists()){
            file.delete();
        }
        final String compressPath = AppConstant.GIFVIDEO+"/yk_gif_"+System.currentTimeMillis()+".mp4";
        VideoCompress.compressVideoMedium(mPath, compressPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                mLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess() {
                GifExtractor gifExtractor = new GifExtractor(GIFVideoProActivity.this, compressPath);
                gifExtractor.addEncolerListener(GIFVideoProActivity.this);
                gifExtractor.encoder(mGifPath, 0, mVideoDuration, 5,10, mWidth, mHeight);
            }

            @Override
            public void onFail() {
                mTvLoad.setText("视频压缩失败");
            }

            @Override
            public void onProgress(float percent) {
                mTvLoad.setText("视频压缩中。。。"+String.format("%1.2f", percent) +"%");
            }
        });
    }

    @Override
    public void startEncoder() {

    }

    @Override
    public void endEncoder() {
        UIHandler.run(new Runnable() {
            @Override
            public void run() {
                mLayout.setVisibility(View.GONE);
                startPlayGif();
                ToastUtils.showToast("GIF已经保存到"+AppConstant.GIFLOAD);
            }
        });
    }

    private void startPlayGif() {
        try {
            mGifDrawable = new GifDrawable(new File(mGifPath));
            mGifImageView.setImageDrawable(mGifDrawable);//这里是实际决定资源的地方，优先级高于xml文件的资源定义
        } catch (IOException e) {
            e.printStackTrace();
        }
        mGifDrawable.setLoopCount(0);
        mGifDrawable.start();
    }

    @Override
    public void progressEncoder(final int total, final int progress) {
        UIHandler.run(new Runnable() {
            @Override
            public void run() {
                mTvLoad.setText("GIF合成中。。。("+progress+"/"+total+")");
            }
        });
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
        mFlbShare.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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
            case R.id.btn_share:
                ShareDialog shareDialog = ShareDialog.getImageInstance(mGifPath, AppConstant.APP_STORE);
                shareDialog.show(getSupportFragmentManager(),"");
                break;
        }
    }

}
