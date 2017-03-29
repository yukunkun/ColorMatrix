package com.matrix.yukun.matrix.leshilive_module.present;

import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshi_module.present.RetrofitInfo;
import com.matrix.yukun.matrix.leshilive_module.bean.LiveListBean;
import com.matrix.yukun.matrix.leshilive_module.bean.ResponseBean;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;
import com.matrix.yukun.matrix.util.MD5Encoder;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yukun on 17-3-28.
 */
public class LiveInfo {

    public static Observable<List<LiveListBean>> getLiveList(String method,String ver,String timestamp){
        Retrofit retrofit = RetrofitApi.getInstance().retrofitLiveUtil();
        String sign=null;
        String str=/*"fetchSize90"+*/"method"+method+"timestamp"+timestamp+"userid"+AppConstants.UserId+"ver"+ver;
        try {
            sign= MD5Encoder.encode(str+AppConstants.Secret_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit.create(MovieService.class).getLiveList(method,ver,Integer.valueOf(AppConstants.UserId),Long.valueOf(timestamp),sign)
                .filter(new Func1<ResponseBean<LiveListBean>, Boolean>() {
                    @Override
                    public Boolean call(ResponseBean<LiveListBean> o) {
                        return true;
                    }
                }).flatMap(new Func1<ResponseBean<LiveListBean>, Observable<List<LiveListBean>>>() {
                    @Override
                    public Observable<List<LiveListBean>> call(final ResponseBean<LiveListBean> liveListBeanResponseBean) {

                        return Observable.create(new Observable.OnSubscribe<List<LiveListBean>>() {
                            @Override
                            public void call(Subscriber<? super List<LiveListBean>> subscriber) {
                                List<LiveListBean> rows = liveListBeanResponseBean.getRows();
                                subscriber.onNext(rows);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
