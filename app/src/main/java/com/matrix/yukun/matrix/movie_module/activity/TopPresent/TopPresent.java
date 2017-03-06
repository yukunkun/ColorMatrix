package com.matrix.yukun.matrix.movie_module.activity.TopPresent;

import android.app.Activity;

import com.matrix.yukun.matrix.movie_module.activity.TopDetailActivity;

/**
 * Created by yukun on 17-3-3.
 */
public class TopPresent implements PersentImpl {
    TopDetailActivity mView;

    public TopPresent(Activity activity) {
        mView=(TopDetailActivity) activity;
    }


    @Override
    public void onsubscriber() {

    }

    @Override
    public void unsubscriber() {

    }

    @Override
    public void setWebUri(int pos) {
        mView.getInfo(pos);
    }
}
