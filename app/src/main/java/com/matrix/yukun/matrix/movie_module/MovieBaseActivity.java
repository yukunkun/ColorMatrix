package com.matrix.yukun.matrix.movie_module;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

import org.greenrobot.eventbus.EventBus;

public class MovieBaseActivity extends AppCompatActivity {

    public BasePresentImpl basePresent;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        EventBus.getDefault().register(this);
        basePresent.onsubscriber();
//        RefWatcher refWatcher = MyApp.getRefWatcher(this);
//        refWatcher.watch(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        basePresent.unsubscriber();
    }
}
