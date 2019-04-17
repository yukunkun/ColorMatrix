package com.matrix.yukun.matrix.btmovie_module.activity;

import android.view.View;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.btmovie_module.BaseActivity;


public class DownLoadActivity extends BaseActivity {


    private ImageView mIvBack;

    @Override
    public int getLayout() {
        return R.layout.activity_bt_download;
    }

    @Override
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
