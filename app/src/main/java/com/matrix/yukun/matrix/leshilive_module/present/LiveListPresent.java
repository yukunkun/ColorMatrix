package com.matrix.yukun.matrix.leshilive_module.present;

import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshilive_module.LiveListActivity;
import com.matrix.yukun.matrix.leshilive_module.bean.LiveListBean;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-28.
 */
public class LiveListPresent implements BasePresentImpl {
    private LiveListActivity mView;
    private final CompositeSubscription compositeSubscription;

    public LiveListPresent(LiveListActivity mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
    }

    public void getInfo() {
        Subscription subscribe = LiveInfo.getLiveList("lecloud.cloudlive.activity.search", "4.1", AppConstants.timestamp)
                .subscribe(new Subscriber<List<LiveListBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.dismissDialogs();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onNext(List<LiveListBean> liveListBeen) {
                        mView.getliveInfo(liveListBeen);
                    }
                });
        compositeSubscription.add(subscribe);
    }

    @Override
    public void onsubscriber() {
        getInfo();
    }

    @Override
    public void unsubscriber() {
        compositeSubscription.unsubscribe();

    }
}
