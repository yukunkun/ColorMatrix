package com.matrix.yukun.matrix.leshi_module.present;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.LeShiListActivity;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-16.
 */
public class PlayListPresent implements BasePresentImpl {
    LeShiActivity mView;
    private CompositeSubscription compositeSubscription=new CompositeSubscription();

    public PlayListPresent(LeShiActivity mView) {
        this.mView = mView;
    }
    public void getInfo(){
        Subscription onCompleted = RetrofitInfo.getList(AppConstants.timestamp, "video.list").subscribe(new Subscriber<List<ListBean>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialogs();
            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.toString());
            }

            @Override
            public void onNext(List<ListBean> listBean) {
                mView.getListInfo(listBean);
            }
        });
        compositeSubscription.add(onCompleted);
    }

    @Override
    public void onsubscriber() {

    }

    @Override
    public void unsubscriber() {
        compositeSubscription.unsubscribe();
    }
}