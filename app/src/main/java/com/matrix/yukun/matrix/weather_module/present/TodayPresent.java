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
    String city;
    @Override
    public void getInfo() {
        Subscription subscription = WeatherNet.getNow(city).subscribe(new Subscriber<WeaNow>() {
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
        Subscription subscribe = WeatherNet.getDestory(city).subscribe(new Subscriber<WeaDestory>() {
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
        Subscription subscription = WeatherNet.getHours(city).subscribe(new Subscriber<WeaHours>() {
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

    public TodayPresent(TodayWeathFrag mView,String city) {
        this.mView = mView;
        this.city=city;
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
