package com.matrix.yukun.matrix.download_module;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.fragment.DownLoadedFragment;
import com.matrix.yukun.matrix.video_module.BaseActivity;

public class MyDownloadActivity extends BaseActivity {


    private ImageView mImageView;

    public static void start(Context context){
        Intent intent=new Intent(context,MyDownloadActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_my_download;
    }

    @Override
    public void initView() {
        mImageView = findViewById(R.id.iv_back);
        DownLoadedFragment mDownLoadedFragment = DownLoadedFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_contain, mDownLoadedFragment).commit();
    }

    @Override
    public void initListener() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
