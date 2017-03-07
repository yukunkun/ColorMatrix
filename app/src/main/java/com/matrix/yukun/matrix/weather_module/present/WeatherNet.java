package com.matrix.yukun.matrix.weather_module.present;

import android.support.annotation.MainThread;
import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.movie_module.util.ApiException;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yukun on 17-3-6.
 */
public class WeatherNet {

    public  static Observable<WeaNow> getNow(String city){

        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
        return retrofit.create(MovieService.class).getNow(city, AppConstants.HEWEATHER_KEY)
                .filter(new Func1<WeaNow, Boolean>() {
                    @Override
                    public Boolean call(WeaNow weaNow) {
                        if(weaNow.getHeWeather5().get(0).getStatus().equals("ok")){
                            return true;
                        }else {
                            throw new ApiException(1);
                        }
                    }
                }).flatMap(new Func1<WeaNow, Observable<WeaNow>>() {
                    @Override
                    public Observable<WeaNow> call(final WeaNow weaNow) {

                        return  Observable.create(new Observable.OnSubscribe<WeaNow>() {
                            @Override
                            public void call(Subscriber<? super WeaNow> subscriber) {
                                subscriber.onNext(weaNow);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());




    }
    public  static Observable<WeaDestory> getDestory(String city){

        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
        return retrofit.create(MovieService.class).getAlarm(city, AppConstants.HEWEATHER_KEY)
                .filter(new Func1<WeaDestory, Boolean>() {
                    @Override
                    public Boolean call(WeaDestory weaDestory) {
                        if(weaDestory.getHeWeather5().get(0).getStatus().equals("ok")){
                            return true;
                        }else {
                            throw new ApiException(1);
                        }
                    }
                }).flatMap(new Func1<WeaDestory, Observable<WeaDestory>>() {
                    @Override
                    public Observable<WeaDestory> call(final WeaDestory weaDestory) {
                        return Observable.create(new Observable.OnSubscribe<WeaDestory>() {
                            @Override
                            public void call(Subscriber<? super WeaDestory> subscriber) {
                                subscriber.onNext(weaDestory);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());




    }
}
