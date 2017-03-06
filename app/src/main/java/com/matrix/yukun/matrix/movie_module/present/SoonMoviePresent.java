package com.matrix.yukun.matrix.movie_module.present;

import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.movie_module.fragment.SoonMoiveFragment;

import java.util.List;

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
        Subscription subscribe = RetrofitUtils.getSoonMovie(start, 10).subscribe(new Subscriber<List<Subjects>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialogs();
            }

            @Override
            public void onError(Throwable e) {
                mView.showMessage(e.toString());
            }

            @Override
            public void onNext(List<Subjects> subjects) {
                mView.getInfo(subjects);
            }
         });
        compositeSubscription.addAll(subscribe);
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
