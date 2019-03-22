package com.example.pluglin_special.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pluglin_special.BaseActivity;
import com.example.pluglin_special.R;

public class DownLoadActivity extends BaseActivity {


    private ImageView mIvBack;

    @Override
    public int getLayout() {
        return R.layout.activity_down_load;
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
