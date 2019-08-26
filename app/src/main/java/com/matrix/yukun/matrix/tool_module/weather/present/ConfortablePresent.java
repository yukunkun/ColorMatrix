package com.matrix.yukun.matrix.tool_module.weather.present;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaLifePoint;
import com.matrix.yukun.matrix.tool_module.weather.fragment.ConfortableFragment;
import com.matrix.yukun.matrix.tool_module.weather.util.MovieService;
import com.matrix.yukun.matrix.tool_module.weather.util.RetrofitApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
        retrofit.create(MovieService.class).getLifes(city, AppConstant.HEWEATHER_KEY).enqueue(new Callback<WeaLifePoint>() {
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
    }

}
