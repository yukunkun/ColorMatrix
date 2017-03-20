package com.matrix.yukun.matrix.leshi_module.present;

import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.LeShiListActivity;
import com.matrix.yukun.matrix.leshi_module.bean.LeShiListBean;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-16.
 */
public class LeShiPresent implements BasePresentImpl {
    LeShiListActivity mView;
    private CompositeSubscription compositeSubscription=new CompositeSubscription();

    public LeShiPresent(LeShiListActivity mView) {
        this.mView = mView;
    }
    public void getInfo(int index){
        Subscription onCompleted = RetrofitInfo.getList(AppConstants.timestamp, "video.list",index).subscribe(new Subscriber<List<ListBean>>() {
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
                Log.i("LeShiPresent",listBean.toString());
                mView.getInfo(listBean);
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