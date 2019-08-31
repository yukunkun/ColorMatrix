package com.matrix.yukun.matrix.main_module.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.dialog.ImageDownLoadDialog;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class ImageDetailActivity extends BaseActivity {

    @BindView(R2.id.photoview)
    PhotoView mPhotoview;
    @BindView(R2.id.iv_more)
    ImageView mIvMore;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.iv_images)
    ImageView mIvImage;
    @BindView(R2.id.rl)
    RelativeLayout mRl;
    @BindView(R2.id.progress_bar)
    ProgressBar mProgressBar;
    private String downloadurl;
    private boolean mIsGif;


    @Override
    public int getLayout() {
        return R.layout.activity_show_image_detail;
    }

    public static void start(Context context,String url,boolean isGif){
        Intent intent=new Intent(context,ImageDetailActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("isGif",isGif);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
//        StatusBarUtil.setStatusBarDarkTheme(this, true);
        downloadurl = getIntent().getStringExtra("url");
        mIsGif = getIntent().getBooleanExtra("isGif",false);

//        Glide.with(this).load(downloadurl).into(new GlideDrawableImageViewTarget(mPhotoview){
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                super.onResourceReady(resource, animation);
//                mProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                super.onLoadFailed(e, errorDrawable);
//                ToastUtils.showToast( "图片加载失败");
//                mProgressBar.setVisibility(View.GONE);
//            }
//        });


        if (mIsGif) {
            Glide.with(ImageDetailActivity.this).asGif().load(downloadurl).into(mIvImage);
            mPhotoview.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(downloadurl).into(mPhotoview);
        }
    }

    @OnClick({R2.id.iv_more, R2.id.iv_back})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_more) {
            ImageDownLoadDialog imageDownLoadDialog= ImageDownLoadDialog.getInstance(downloadurl);
            imageDownLoadDialog.show(getSupportFragmentManager(),"");
        } else if (i == R.id.iv_back) {
            finish();
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
