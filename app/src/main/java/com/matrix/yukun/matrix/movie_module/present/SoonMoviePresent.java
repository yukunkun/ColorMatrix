package com.matrix.yukun.matrix.movie_module.present;

import com.matrix.yukun.matrix.movie_module.bean.HttpResult;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.movie_module.fragment.SoonMoiveFragment;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by yukun on 17-2-20.
 */
public class SoonMoviePresent implements BasePresentImpl {
    SoonMoiveFragment mView;
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public SoonMoviePresent(SoonMoiveFragment mainActivity) {
        this.mView=mainActivity;
    }

    public void getInfo(int start){
        Retrofit retrofituil = RetrofitApi.getInstance().retrofituil();
        retrofituil.create(MovieService.class).getSoonMovie(start,10).enqueue(new Callback<HttpResult<Subjects>>() {
            @Override
            public void onResponse(Call<HttpResult<Subjects>> call, Response<HttpResult<Subjects>> response) {
                mView.dismissDialogs();
                mView.getInfo(response.body().getSubjects());
            }

            @Override
            public void onFailure(Call<HttpResult<Subjects>> call, Throwable t) {
                mView.showMessage(t.toString());
            }
        });
//        Subscription subscribe = RetrofitUtils.getSoonMovie(start, 10).subscribe(new Subscriber<List<Subjects>>() {
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
//            public void onNext(List<Subjects> subjects) {
//                mView.getInfo(subjects);
//            }
//         });
//        compositeSubscription.addAll(subscribe);
    }

    @Override
    public void onsubscriber() {
        getInfo(0);
    }

    //生命周期的某个时刻取消订阅
    @Override
    public void unsubscriber() {
        // 一旦你调用了 CompositeSubscription.unsubscribe()，
        // 这个CompositeSubscription对象就不可用了, 如果你还想使用CompositeSubscription，就必须在创建一个新的对象了。
        compositeSubscription.unsubscribe();
    }
}
