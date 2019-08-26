package com.matrix.yukun.matrix.tool_module.weather.present;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaTomorrow;
import com.matrix.yukun.matrix.tool_module.weather.fragment.TomorrowWeathFrag;
import com.matrix.yukun.matrix.tool_module.weather.util.MovieService;
import com.matrix.yukun.matrix.tool_module.weather.util.RetrofitApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-3-6.
 */
public class TomorrowPresent implements TomorrowPresentImpl {
    TomorrowWeathFrag mView;
    String city;
    CompositeSubscription compositeSubscription=new CompositeSubscription();

    public TomorrowPresent(TomorrowWeathFrag tomorrowWeathFrag, String city) {
        this.mView = tomorrowWeathFrag;
        this.city=city;
    }

    @Override
    public void getTomorrow(String city) {
        Retrofit retrofit = RetrofitApi.getInstance().retrofitWeaUil();
        retrofit.create(MovieService.class).getTomorrows(city, AppConstant.HEWEATHER_KEY)
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
