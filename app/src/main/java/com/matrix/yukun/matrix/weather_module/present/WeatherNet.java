package com.matrix.yukun.matrix.weather_module.present;

import android.support.annotation.MainThread;
import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.movie_module.util.ApiException;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;

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
    //now天气
//    public  static Observable<WeaNow> getNow(String city){
//
//        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
//        return retrofit.create(MovieService.class).getNow(city, AppConstants.HEWEATHER_KEY)
//                .filter(new Func1<WeaNow, Boolean>() {
//                    @Override
//                    public Boolean call(WeaNow weaNow) {
//                        if(weaNow.getHeWeather6().get(0).getStatus().equals("ok")){
//                            return true;
//                        }else {
//                            throw new ApiException(1);
//                        }
//                    }
//                }).flatMap(new Func1<WeaNow, Observable<WeaNow>>() {
//                    @Override
//                    public Observable<WeaNow> call(final WeaNow weaNow) {
//
//                        return  Observable.create(new Observable.OnSubscribe<WeaNow>() {
//                            @Override
//                            public void call(Subscriber<? super WeaNow> subscriber) {
//                                subscriber.onNext(weaNow);
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
    //自然灾害
//    public  static Observable<WeaDestory> getDestory(String city){
        // 该接口变为付费接口了,暂时不显示
//        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
//        return retrofit.create(MovieService.class).getAlarm(city, AppConstants.HEWEATHER_KEY)
//                .filter(new Func1<WeaDestory, Boolean>() {
//                    @Override
//                    public Boolean call(WeaDestory weaDestory) {
//                        if(weaDestory.getHeWeather5().get(0).getStatus().equals("ok")){
//                            return true;
//                        }else {
//                            //此处显示付费
////                            throw new ApiException(1);
//                            return false;
//                        }
//                    }
//                }).flatMap(new Func1<WeaDestory, Observable<WeaDestory>>() {
//                    @Override
//                    public Observable<WeaDestory> call(final WeaDestory weaDestory) {
//                        return Observable.create(new Observable.OnSubscribe<WeaDestory>() {
//                            @Override
//                            public void call(Subscriber<? super WeaDestory> subscriber) {
//                                subscriber.onNext(weaDestory);
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    //实时天气
//    public  static Observable<WeaHours> getHours(String city){
//
//        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
//        return retrofit.create(MovieService.class).getHourly(city, AppConstants.HEWEATHER_KEY)
//                .filter(new Func1<WeaHours, Boolean>() {
//                    @Override
//                    public Boolean call(WeaHours weaHours) {
//                        if(weaHours.getHeWeather5().get(0).getStatus().equals("ok")){
//                            return true;
//                        }else {
//                            throw new ApiException(1);
//                        }
//                    }
//                }).flatMap(new Func1<WeaHours, Observable<WeaHours>>() {
//                    @Override
//                    public Observable<WeaHours> call(final WeaHours weaHours) {
//                        return Observable.create(new Observable.OnSubscribe<WeaHours>() {
//                            @Override
//                            public void call(Subscriber<? super WeaHours> subscriber) {
//                                subscriber.onNext(weaHours);
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    //未来几日天气
//    public  static Observable<WeaTomorrow> getTomorrow(String city){
//
//        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
//        return retrofit.create(MovieService.class).getTomorrow(city, AppConstants.HEWEATHER_KEY)
//                .filter(new Func1<WeaTomorrow, Boolean>() {
//                    @Override
//                    public Boolean call(WeaTomorrow weaHours) {
//                        if(weaHours.getHeWeather5().get(0).getStatus().equals("ok")){
//                            return true;
//                        }else {
//                            throw new ApiException(1);
//                        }
//                    }
//                }).flatMap(new Func1<WeaTomorrow, Observable<WeaTomorrow>>() {
//                    @Override
//                    public Observable<WeaTomorrow> call(final WeaTomorrow weaHours) {
//                        return Observable.create(new Observable.OnSubscribe<WeaTomorrow>() {
//                            @Override
//                            public void call(Subscriber<? super WeaTomorrow> subscriber) {
//                                subscriber.onNext(weaHours);
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//    //人体舒适度
//    public  static Observable<WeaLifePoint> getConfortable(String city){
//
//        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
//        return retrofit.create(MovieService.class).getLife(city, AppConstants.HEWEATHER_KEY)
//        .filter(new Func1<WeaLifePoint, Boolean>() {
//                    @Override
//                    public Boolean call(WeaLifePoint weaHours) {
//
//                        if(weaHours.getHeWeather5().get(0).getStatus().equals("ok")){
//                            return true;
//                        }else {
//                            throw new ApiException(1);
//                        }
//                    }
//                }).flatMap(new Func1<WeaLifePoint, Observable<WeaLifePoint>>() {
//                    @Override
//                    public Observable<WeaLifePoint> call(final WeaLifePoint weaHours) {
//                        return Observable.create(new Observable.OnSubscribe<WeaLifePoint>() {
//                            @Override
//                            public void call(Subscriber<? super WeaLifePoint> subscriber) {
//                                subscriber.onNext(weaHours);
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
