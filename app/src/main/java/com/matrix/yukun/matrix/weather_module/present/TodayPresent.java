package com.matrix.yukun.matrix.weather_module.present;


import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.fragment.TodayWeathFrag;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-6.
 */
public class TodayPresent implements TodayPresentImpl{
    TodayWeathFrag mView;
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void getInfo() {
        Subscription subscription = WeatherNet.getNow("成都").subscribe(new Subscriber<WeaNow>() {
            @Override
            public void onCompleted() {
                mView.dismissDialogs();
            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.toString());
            }

            @Override
            public void onNext(WeaNow weaNow) {
                mView.getInfo(weaNow);
            }
        });
        compositeSubscription.add(subscription);
    }

    @Override
    public void getDestory() {
        Subscription subscribe = WeatherNet.getDestory("成都").subscribe(new Subscriber<WeaDestory>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.toString());
            }

            @Override
            public void onNext(WeaDestory weaDestory) {
                mView.getDestory(weaDestory);
            }
        });
        compositeSubscription.add(subscribe);

    }

    @Override
    public void getHours() {
        Subscription subscription = WeatherNet.getHours("成都").subscribe(new Subscriber<WeaHours>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.toString());
            }

            @Override
            public void onNext(WeaHours weaHours) {
                mView.getHoursInfo(weaHours);
            }
        });
        compositeSubscription.add(subscription);
    }

    public TodayPresent(TodayWeathFrag mView) {
        this.mView = mView;
    }


    @Override
    public void onsubscriber() {
        getInfo();
        getDestory();
        getHours();
    }

    @Override
    public void unsubscriber() {
        compositeSubscription.unsubscribe();
    }
}
