package com.matrix.yukun.matrix.weather_module.present;

import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.fragment.ConfortableFragment;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-7.
 */
public class ConfortablePresent  implements ConfortablePresentImpl {
    ConfortableFragment mView;
    String city;
    CompositeSubscription compositeSubscription=new CompositeSubscription();
    public ConfortablePresent(ConfortableFragment mView, String city) {
        this.mView = mView;
        this.city = city;
    }

    @Override
    public void onsubscriber() {
        getInfo(city);
    }

    @Override
    public void unsubscriber() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void getInfo(String city) {
        Subscription subscribe = WeatherNet.getConfortable(city).subscribe(new Subscriber<WeaLifePoint>() {
            @Override
            public void onCompleted() {
                mView.dismissDialogs();
            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.toString());
            }

            @Override
            public void onNext(WeaLifePoint weaLifePoint) {
                mView.getLifeInfo(weaLifePoint);
            }
        });
        compositeSubscription.add(subscribe);
    }

}
