package com.matrix.yukun.matrix.weather_module.present;

import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.fragment.ConfortableFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
        retrofit.create(MovieService.class).getLifes(city, AppConstants.HEWEATHER_KEY).enqueue(new Callback<WeaLifePoint>() {
            @Override
            public void onResponse(Call<WeaLifePoint> call, Response<WeaLifePoint> response) {
                mView.dismissDialogs();
                mView.getLifeInfo(response.body());

            }

            @Override
            public void onFailure(Call<WeaLifePoint> call, Throwable t) {
                mView.showMessage(t.toString());
            }
        });
//        Subscription subscribe = WeatherNet.getConfortable(city).subscribe(new Subscriber<WeaLifePoint>() {
//            @Override
//            public void onCompleted() {
//                mView.dismissDialogs();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i("------e",e.toString());
//                mView.showMessage(e.toString());
//            }
//
//            @Override
//            public void onNext(WeaLifePoint weaLifePoint) {
//                Log.i("------bb",weaLifePoint.toString());
//
//                mView.getLifeInfo(weaLifePoint);
//            }
//        });
//        compositeSubscription.add(subscribe);
    }

}
