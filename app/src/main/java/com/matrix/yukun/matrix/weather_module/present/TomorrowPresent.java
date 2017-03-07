package com.matrix.yukun.matrix.weather_module.present;

import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;
import com.matrix.yukun.matrix.weather_module.fragment.TomorrowWeathFrag;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-6.
 */
public class TomorrowPresent implements TomorrowPresentImpl {
    TomorrowWeathFrag mView;
    String city;
    CompositeSubscription compositeSubscription=new CompositeSubscription();

    public TomorrowPresent(TomorrowWeathFrag tomorrowWeathFrag,String city) {
        this.mView = tomorrowWeathFrag;
        this.city=city;
    }

    @Override
    public void getTomorrow(String city) {
        Subscription subscribe = WeatherNet.getTomorrow(city).subscribe(new Subscriber<WeaTomorrow>() {
            @Override
            public void onCompleted() {
                mView.dismissDialogs();
            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.toString());
            }

            @Override
            public void onNext(WeaTomorrow weaTomorrow) {
                mView.getInfo(weaTomorrow);
            }
        });
        compositeSubscription.add(subscribe);
    }

    @Override
    public void onsubscriber() {
        getTomorrow(city);
    }

    @Override
    public void unsubscriber() {
        compositeSubscription.unsubscribe();
    }
}
