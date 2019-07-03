package com.matrix.yukun.matrix.gif_module.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.download_module.service.UIHandler;
import com.matrix.yukun.matrix.util.ImageUtils;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GIFImageProActivity extends BaseActivity implements View.OnClickListener {

    private String mFilePath;
    List<String> mListPath=new ArrayList<>();
    private int mWidth;
    private int mHeight;
    private String mFileName="";
    private GifImageView mGifView;
    private GifDrawable mGifDrawable;
    private ImageView mIvBack;
    private ImageView mIvDownload;
    private ImageView mIvPlay;
    private SeekBar mSeekBar;
    private int mPro=1;
    private ImageView mIvShare;
    private ImageView mIvGallary;
    private TextView mTvTitle;
    int fps=1;
    private RelativeLayout mLayout;

    public static void start(Context context, String filePath,int width,int height ){
        Intent intent=new Intent(context,GIFImageProActivity.class);
        intent.putExtra("filePath",filePath);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_gifimage_pro;
    }

    @Override
    public void initView() {
        mFilePath = getIntent().getStringExtra("filePath");
        mWidth = getIntent().getIntExtra("width", 480);
        mHeight = getIntent().getIntExtra("height", 480);
        mGifView = findViewById(R.id.giv1);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mIvDownload = findViewById(R.id.iv_download);
        mIvPlay = findViewById(R.id.iv_control);
        mSeekBar = findViewById(R.id.seekbar);
        mIvShare = findViewById(R.id.iv_share);
        mIvGallary = findViewById(R.id.iv_gallary);
        mLayout = findViewById(R.id.rl_load);
        mSeekBar.setMax(15);
        mSeekBar.setProgress(1);
    }

    @Override
    public void initDate() {
        mListPath.clear();
        File file=new File(mFilePath);
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            mListPath.add(listFiles[i].getPath());
        }
        Bitmap bitmap=BitmapFactory.decodeFile(mListPath.get(0));
        mWidth=bitmap.getWidth();
        mHeight=bitmap.getHeight();
        mIvBack.post(new Runnable() {
            @Override
            public void run() {
                mFileName = "/yk_gif_"+System.currentTimeMillis()+".gif";
                mTvTitle.setText("yk_gif_"+System.currentTimeMillis()+".gif");
                createGif(mFileName,mListPath,fps,mWidth,mHeight,true);
            }
        });
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvDownload.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvGallary.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mPro = progress+1;
                    mGifDrawable.setSpeed(mPro);
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
            case R.id.iv_gallary:
                GallaryActivity.start(this,mFilePath);
                break;
            case R.id.iv_share:
                ShareDialog shareDialog = ShareDialog.getImageInstance(AppConstant.GIFLOAD+mFileName, AppConstant.APP_STORE);
                shareDialog.show(getSupportFragmentManager(),"");
                break;
            case R.id.iv_download:
                File file = new File(AppConstant.GIFLOAD);
                if (!file.exists()){
                    file.mkdirs();
                }
                createGif(mFileName,mListPath,mPro,mWidth,mHeight,false);
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
        }
    }

    /**
     * 创建gif成功后的回调
     * @param isPro
     */
    private void endCreateListener(boolean isPro) {
        if(isPro){
            try {
                mGifDrawable = new GifDrawable(new File(AppConstant.GIFLOAD+"/"+mFileName));
                mGifView.setImageDrawable(mGifDrawable);//这里是实际决定资源的地方，优先级高于xml文件的资源定义
            } catch (IOException e) {
                e.printStackTrace();
            }
            mGifDrawable.start();
            mLayout.setVisibility(View.GONE);
        }else {
            ToastUtils.showToast("文件下载到"+AppConstant.GIFLOAD);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==2){
                initDate();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startGifPlay(){

    }

    public void createGif(final String filename, final List<String> paths, final int fps, final int width, final int height, final boolean isPro)  {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
                localAnimatedGifEncoder.start(baos);//start
                localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
                localAnimatedGifEncoder.setDelay(2000/fps); //没帧的延迟时间 ms
                if (paths.size() > 0) {
                    for (int i = 0; i < paths.size(); i++) {
                        Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
                        Bitmap resizeBm = ImageUtils.resizeImage(bitmap, width, height);
                        localAnimatedGifEncoder.addFrame(resizeBm);
                    }
                }
                localAnimatedGifEncoder.finish();//finish
                File file = new File(AppConstant.GIFLOAD);
                if (!file.exists()){
                    file.mkdirs();
                }
                String path = AppConstant.GIFLOAD+"/"+filename;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(path);
                    baos.writeTo(fos);
                    baos.flush();
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    baos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UIHandler.run(new Runnable(){
                    @Override
                    public void run() {
                        endCreateListener(isPro);
                    }
                });
            }
        });
        thread.start();
    }

}
