package com.matrix.yukun.matrix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iflytek.sunflower.FlowerCollector;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    //  讯飞统计的 Appid： 	58833c92
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initView();
        initDate();
        initListener();
    }

    public abstract int getLayout();

    public abstract void initView();

    public void initListener(){

    }

    public void initDate(){

    }
    @Override
    protected void onResume() {
        super.onResume();
        FlowerCollector.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FlowerCollector.onPause(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = MyApp.getRefWatcher(this);//1
//        refWatcher.watch(this);
    }
}
