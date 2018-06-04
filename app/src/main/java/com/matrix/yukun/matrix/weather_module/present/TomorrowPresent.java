package com.matrix.yukun.matrix.weather_module.present;

import com.google.gson.Gson;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;
import com.matrix.yukun.matrix.movie_module.present.RetrofitUtils;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;
import com.matrix.yukun.matrix.weather_module.fragment.TomorrowWeathFrag;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
        retrofit.create(MovieService.class).getTomorrows(city, AppConstants.HEWEATHER_KEY)
                .enqueue(new Callback<WeaTomorrow>() {
                    @Override
                    public void onResponse(Call<WeaTomorrow> call, Response<WeaTomorrow> response) {
                        mView.dismissDialogs();
                        mView.getInfo(response.body());
                    }

                    @Override
                    public void onFailure(Call<WeaTomorrow> call, Throwable t) {
                        mView.showMessage(t.toString());
                    }
                });
//        Subscription subscribe = WeatherNet.getTomorrow(city).subscribe(new Subscriber<WeaTomorrow>() {
//            @Override
//            public void onCompleted() {
//                mView.dismissDialogs();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mView.showMessage(e.toString());
//            }
//
//            @Override
//            public void onNext(WeaTomorrow weaTomorrow) {
//                mView.getInfo(weaTomorrow);
//            }
//        });
//        compositeSubscription.add(subscribe);
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
