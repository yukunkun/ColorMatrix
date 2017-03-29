package com.matrix.yukun.matrix.leshi_module.present;

import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.bean.VideoBean;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-17.
 */
public class PlayPresent implements BasePresentImpl{
    LeShiActivity mView;
    String video_id;
    CompositeSubscription compositeSubscription=new CompositeSubscription();
    public PlayPresent(LeShiActivity mView,String video_id) {
        this.mView = mView;
        this.video_id=video_id;
    }

    public void getInfo(){
        Subscription onCompleted = RetrofitInfo.getLeShiMov(AppConstants.timestamp, "video.get", Integer.valueOf(video_id))
                .subscribe(new Subscriber<VideoBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("---onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onNext(VideoBean videoBean) {
                        Log.i("-----PlayPresent", videoBean.toString());
                        mView.getInfos(videoBean);
                    }
                });
        compositeSubscription.add(onCompleted);
    };

    @Override
    public void onsubscriber() {

    }

    @Override
    public void unsubscriber() {
        compositeSubscription.unsubscribe();
    }

    public void pauseVideo(){
        RetrofitInfo.getVideoPause(AppConstants.timestamp,"video.pause",Integer.valueOf(video_id))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Log.i("-----pauseVideo", o.toString());

                    }
                });
    }

    public void startVideo(){
        RetrofitInfo.getVideoStart(AppConstants.timestamp,"video.restore",Integer.valueOf(video_id))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Log.i("-----startVideo", o.toString());
                    }
                });
    }
}
