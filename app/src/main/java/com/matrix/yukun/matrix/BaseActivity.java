package com.matrix.yukun.matrix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iflytek.sunflower.FlowerCollector;


public class BaseActivity extends AppCompatActivity {
    //  讯飞统计的 Appid： 	58833c92
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

//        RefWatcher refWatcher = MyApp.getRefWatcher(this);
//        refWatcher.watch(this);

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
}
