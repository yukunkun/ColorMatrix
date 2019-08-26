package com.matrix.yukun.matrix.tool_module.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.matrix.yukun.matrix.tool_module.weather.util.BasePresentImpl;


/**
 * Created by yukun on 17-3-3.
 */
public class BaseFrag extends Fragment {

    public BasePresentImpl basePresent;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basePresent.onsubscriber();
//        RefWatcher refWatcher = MyApp.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        basePresent.unsubscriber();
    }
}
