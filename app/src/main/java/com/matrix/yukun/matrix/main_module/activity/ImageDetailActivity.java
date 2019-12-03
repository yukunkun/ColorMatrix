package com.matrix.yukun.matrix.main_module.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.dialog.ImageDownLoadDialog;
import com.matrix.yukun.matrix.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class ImageDetailActivity extends BaseActivity {

    @BindView(R2.id.photoview)
    PhotoView mPhotoview;
    @BindView(R2.id.iv_more)
    ImageView mIvMore;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.rl)
    RelativeLayout mRl;
    @BindView(R2.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.iv_change)
    ImageView ivChange;
    private String downloadurl;
    private boolean mIsGif;

    @Override
    public int getLayout() {
        return R.layout.activity_show_image_detail;
    }

    public static void start(Context context, String url, boolean isGif) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isGif", isGif);
        context.startActivity(intent);
    }

    public static void start(Context context, String url, View view,boolean isGif) {
        Intent intent=new Intent(context, ImageDetailActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("isGif", isGif);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
//            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,view,"shareView").toBundle());
//        }else {
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
//        }
    }


    @Override
    public void initView() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
//        StatusBarUtil.setStatusBarDarkTheme(this, true);
        downloadurl = getIntent().getStringExtra("url");
        mIsGif = getIntent().getBooleanExtra("isGif", false);
        Glide.with(this).load(downloadurl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(mPhotoview);
    }

    @OnClick({R2.id.iv_more, R2.id.iv_back,R2.id.iv_change})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_more) {
            ImageDownLoadDialog imageDownLoadDialog = ImageDownLoadDialog.getInstance(downloadurl);
            imageDownLoadDialog.show(getSupportFragmentManager(), "");
        } else if (i == R.id.iv_back) {
            finish();
        }else if (i == R.id.iv_change) {
            changeScreen();
        }
    }

    private void changeScreen() {
        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //切换竖屏
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ivChange.setImageResource(R.mipmap.icon_image_shu);
        }else{
            //切换横屏
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ivChange.setImageResource(R.mipmap.icon_image_hen);
        }
    }

    private void DownLoad() {
        new AlertDialog.Builder(this).setTitle("download...").setMessage("是否下载图片?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();

    }

}
