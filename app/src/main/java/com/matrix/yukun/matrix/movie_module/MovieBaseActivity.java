package com.matrix.yukun.matrix.movie_module;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

import org.greenrobot.eventbus.EventBus;

public class MovieBaseActivity<T extends BasePresentImpl> extends AppCompatActivity {

    public T basePresent;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        RefWatcher refWatcher = MyApp.getRefWatcher(this);
//        refWatcher.watch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        basePresent.onsubscriber();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        basePresent.unsubscriber();
    }
}
