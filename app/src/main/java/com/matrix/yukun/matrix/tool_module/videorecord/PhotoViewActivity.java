package com.matrix.yukun.matrix.tool_module.videorecord;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.photoview)
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

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
