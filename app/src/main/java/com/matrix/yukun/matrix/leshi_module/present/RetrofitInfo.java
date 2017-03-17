package com.matrix.yukun.matrix.leshi_module.present;

import android.util.Log;

import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshi_module.bean.LeShiListBean;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshi_module.bean.ListBeanJson;
import com.matrix.yukun.matrix.leshi_module.bean.VideoBean;
import com.matrix.yukun.matrix.movie_module.util.ApiException;
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
 * Created by yukun on 17-3-16.
 */
public class RetrofitInfo {

    public static Observable<List<ListBean>> getList(String timestamp, String api) {
        Retrofit retrofit = RetrofitApi.getInstance().retrofitLeShiUtil();
        String sign=null;
        String str="apivideo.listformat"+AppConstants.format+"index1size50timestamp"+timestamp+"user_unique"+AppConstants.user_unique+"ver"+AppConstants.ver;
        try {
            sign= MD5Encoder.encode(str+AppConstants.Secret_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit.create(MovieService.class).getLeShiList(AppConstants.user_unique,timestamp,api,"json","2.0",1,50,sign)
                .filter(new Func1<LeShiListBean<ListBean>, Boolean>() {
                    @Override
                    public Boolean call(LeShiListBean<ListBean> listBeanLeShiListBean) {
                        if(listBeanLeShiListBean.getTotal()==0){
                            throw new ApiException(0);
                        }
                        else {
                            return true;
                        }
                    }
                }).flatMap(new Func1<LeShiListBean<ListBean>, Observable<List<ListBean>>>() {
                    @Override
                    public Observable<List<ListBean>> call(LeShiListBean<ListBean> listBeanLeShiListBean) {
                        final List<ListBean> date = listBeanLeShiListBean.getDate();
                        return Observable.create(new Observable.OnSubscribe<List<ListBean>>() {
                            @Override
                            public void call(Subscriber<? super List<ListBean>> subscriber) {
                                subscriber.onNext(date);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<VideoBean> getLeShiMov(String timestamp, String api, int video_id) {
        Retrofit retrofit = RetrofitApi.getInstance().retrofitLeShiUtil();
        String sign=null;
        String str="apivideo.getformat"+AppConstants.format+"timestamp"+timestamp+"user_unique"+AppConstants.user_unique+"ver"+AppConstants.ver+"video_id"+video_id;
        try {
            sign= MD5Encoder.encode(str+AppConstants.Secret_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit.create(MovieService.class).getLeShiMov(AppConstants.user_unique,timestamp,api,"json","2.0",video_id,sign)
                .filter(new Func1<ListBeanJson<VideoBean>, Boolean>() {
                    @Override
                    public Boolean call(ListBeanJson<VideoBean> listBeanLeShiListBean) {
                        Log.i("----",listBeanLeShiListBean.toString());
                        if(listBeanLeShiListBean.getTotal()==0){
                            throw new ApiException(0);
                        }
                        else {
                            return true;
                        }
                    }
                }).flatMap(new Func1<ListBeanJson<VideoBean>, Observable<VideoBean>>() {
                    @Override
                    public Observable<VideoBean> call(ListBeanJson<VideoBean> listBeanLeShiListBean) {
                        final VideoBean data = listBeanLeShiListBean.getData();
                        return Observable.create(new Observable.OnSubscribe<VideoBean>() {
                            @Override
                            public void call(Subscriber<? super VideoBean> subscriber) {
                                subscriber.onNext(data);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
