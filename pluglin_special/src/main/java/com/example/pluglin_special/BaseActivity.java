package com.example.pluglin_special;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }
    public abstract int getLayout();
    public abstract void initView();
    public abstract void initData();
    public abstract void initListener();
}
