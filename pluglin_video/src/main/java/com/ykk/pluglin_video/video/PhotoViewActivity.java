package com.ykk.pluglin_video.video;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.photoview)
    PhotoView mPhotoview;

    @Override
    public int getLayout() {
        return R.layout.activity_photo_view;
    }

    @Override
    public void initView() {
        String imagepath = getIntent().getStringExtra("imagepath");
        Glide.with(this).load(imagepath).into(mPhotoview);
    }

    @OnClick(R2.id.iv_back)
    public void onClick() {
        finish();
    }
}
