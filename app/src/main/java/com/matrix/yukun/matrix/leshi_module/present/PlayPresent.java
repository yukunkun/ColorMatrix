package com.matrix.yukun.matrix.leshi_module.present;

import android.text.LoginFilter;
import android.util.Log;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.bean.ListBeanJson;
import com.matrix.yukun.matrix.leshi_module.bean.VideoBean;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

import rx.Subscriber;

/**
 * Created by yukun on 17-3-17.
 */
public class PlayPresent implements BasePresentImpl{
    LeShiActivity mView;
    String video_id;
    public PlayPresent(LeShiActivity mView,String video_id) {
        this.mView = mView;
        this.video_id=video_id;
    }

    public void getInfo(){
        RetrofitInfo.getLeShiMov(AppConstants.timestamp,"video.get",Integer.valueOf(video_id))
                .subscribe(new Subscriber<VideoBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("---onCompleted","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onNext(VideoBean videoBean) {
                        Log.i("-----PlayPresent",videoBean.toString());
                        mView.getInfos(videoBean);
                    }
                });
    };

    @Override
    public void onsubscriber() {
        getInfo();
    }

    @Override
    public void unsubscriber() {

    }
}
