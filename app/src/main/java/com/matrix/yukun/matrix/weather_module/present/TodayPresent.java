package com.matrix.yukun.matrix.weather_module.present;


import android.util.Log;

import com.google.gson.Gson;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.fragment.TodayWeathFrag;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Arrays;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
    public void getInfo(String city) {
        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
        retrofit.create(MovieService.class).getNows(city,AppConstants.HEWEATHER_KEY).enqueue(new Callback<WeaNow>() {
            @Override
            public void onResponse(retrofit2.Call<WeaNow> call, Response<WeaNow> response) {
                mView.dismissDialogs();
                mView.getInfo(response.body());
            }

            @Override
            public void onFailure(retrofit2.Call<WeaNow> call, Throwable t) {
                mView.showMessage(t.toString());
            }
        });

//        Subscription subscription = WeatherNet.getNow(city).subscribe(new Subscriber<WeaNow>() {
//            @Override
//            public void onCompleted() {
//                mView.dismissDialogs();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i("------",e.toString());
//                mView.showMessage(e.toString());
//            }
//
//            @Override
//            public void onNext(WeaNow weaNow) {
//                mView.getInfo(weaNow);
//            }
//        });
//        compositeSubscription.add(subscription);
    }

//    @Override
//    public void getDestory(String city) {
//        Subscription subscribe = WeatherNet.getDestory(city).subscribe(new Subscriber<WeaDestory>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mView.showMessage(e.toString());
//            }
//
//            @Override
//            public void onNext(WeaDestory weaDestory) {
//                mView.getDestory(weaDestory);
//            }
//        });
//        compositeSubscription.add(subscribe);
//
//    }

    @Override
    public void getHours(String city) {
        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
        retrofit.create(MovieService.class).getHourlys(city,AppConstants.HEWEATHER_KEY).enqueue(new Callback<WeaHours>() {
            @Override
            public void onResponse(retrofit2.Call<WeaHours> call, Response<WeaHours> response) {
                mView.dismissDialogs();
                mView.getHoursInfo(response.body());
            }

            @Override
            public void onFailure(retrofit2.Call<WeaHours> call, Throwable t) {
                mView.showMessage(t.toString());
            }
        });
//        Subscription subscription = WeatherNet.getHours(city).subscribe(new Subscriber<WeaHours>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mView.showMessage(e.toString());
//            }
//
//            @Override
//            public void onNext(WeaHours weaHours) {
//                mView.getHoursInfo(weaHours);
//            }
//        });
//        compositeSubscription.add(subscription);
    }

    public TodayPresent(TodayWeathFrag mView,String city) {
        this.mView = mView;
        this.city=city;
    }

    @Override
    public void onsubscriber() {
        getInfo(city);
//        getDestory(city);
        getHours(city);
    }

    @Override
    public void unsubscriber() {
        compositeSubscription.unsubscribe();
    }
}
