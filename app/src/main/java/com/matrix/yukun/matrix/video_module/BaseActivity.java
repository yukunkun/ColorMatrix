package com.matrix.yukun.matrix.video_module;

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
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = MyApp.getRefWatcher(this);//1
//        refWatcher.watch(this);
    }
}
