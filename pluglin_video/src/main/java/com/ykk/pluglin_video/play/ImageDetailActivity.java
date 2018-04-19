package com.ykk.pluglin_video.play;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.utils.DownLoadUtils;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class ImageDetailActivity extends BaseActivity {

    @BindView(R2.id.photoview)
    PhotoView mPhotoview;
    @BindView(R2.id.iv_load)
    ImageView mIvLoad;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    String downloadurl;
    @BindView(R2.id.iv_images)
    ImageView mIvImage;
    private boolean mIsGif;

    @Override
    public int getLayout() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void initView() {
        downloadurl = getIntent().getStringExtra("url");
        mIsGif = getIntent().getBooleanExtra("isGif",false);
        if (mIsGif) {
            Glide.with(ImageDetailActivity.this).load(downloadurl).into(mIvImage);
            mPhotoview.setVisibility(View.GONE);
        } else {
            Glide.with(this).load(downloadurl).into(mPhotoview);
        }
    }

    @OnClick({R2.id.iv_load, R2.id.iv_back})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_load) {
            DownLoad();

        } else if (i == R.id.iv_back) {
            finish();
            overridePendingTransition(R.anim.rotate, R.anim.rotate_out);

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
               Toast.makeText(ImageDetailActivity.this, "Download...", Toast.LENGTH_SHORT).show();
               DownLoadUtils.download(ImageDetailActivity.this, downloadurl,mIsGif);
           }
       }).show();

    }
}
