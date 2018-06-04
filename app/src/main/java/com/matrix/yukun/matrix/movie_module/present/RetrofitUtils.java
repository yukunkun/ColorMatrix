package com.matrix.yukun.matrix.movie_module.present;



import com.matrix.yukun.matrix.movie_module.bean.HttpResult;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.movie_module.util.ApiException;
import com.matrix.yukun.matrix.movie_module.util.MovieService;
import com.matrix.yukun.matrix.movie_module.util.RetrofitApi;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yukun on 17-2-15.
 */
public class RetrofitUtils {

//    public  static Observable<List<Subjects>> getMovie(int start, int count){
//
//        Retrofit retrofituil = RetrofitApi.getInstance().retrofituil();
//
//        return retrofituil.create(MovieService.class).getTopMovie(start, count)
//
//                .filter(new Func1<HttpResult<Subjects>, Boolean>() {
//                    @Override
//                    public Boolean call(HttpResult<Subjects> subjectHttpResult) {
//                        if (subjectHttpResult.getCount() > 5) { //测试 filter 的返回值 可以用于判断返回成功
//                            return true; //表示继续
//                        } else {
//                            //抛出异常
//                            throw new ApiException(subjectHttpResult.getCount());
////                            return false; //表示在这里断开
//                        }
//                    }
//                }).flatMap(new Func1<HttpResult<Subjects>, Observable<List<Subjects>>>() { //转化成我们想要的集合
//                    @Override
//                    public Observable<List<Subjects>> call(final HttpResult<Subjects> subjectHttpResult) {
//
//                        return Observable.create(new Observable.OnSubscribe<List<Subjects>>() {
//                            @Override
//                            public void call(Subscriber<? super List<Subjects>> subscriber) {
//                                subscriber.onNext(subjectHttpResult.getSubjects());
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public  static Observable<List<Subjects>> getNewMovie(int start, int count){
//
//        Retrofit retrofituil = RetrofitApi.getInstance().retrofituil();
//
//        return retrofituil.create(MovieService.class).getNewMovie(start, count)
//
//                .filter(new Func1<HttpResult<Subjects>, Boolean>() {
//                    @Override
//                    public Boolean call(HttpResult<Subjects> subjectHttpResult) {
//                        if (subjectHttpResult.getCount() > 5) { //测试 filter 的返回值 可以用于判断返回成功
//                            return true; //表示继续
//                        } else {
//                            //抛出异常
//                            throw new ApiException(subjectHttpResult.getCount());
////                            return false; //表示在这里断开
//                        }
//                    }
//                }).flatMap(new Func1<HttpResult<Subjects>, Observable<List<Subjects>>>() { //转化成我们想要的集合
//                    @Override
//                    public Observable<List<Subjects>> call(final HttpResult<Subjects> subjectHttpResult) {
//
//                        return Observable.create(new Observable.OnSubscribe<List<Subjects>>() {
//                            @Override
//                            public void call(Subscriber<? super List<Subjects>> subscriber) {
//                                subscriber.onNext(subjectHttpResult.getSubjects());
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//    public  static Observable<List<Subjects>> getSoonMovie(int start, int count){
//
//        Retrofit retrofituil = RetrofitApi.getInstance().retrofituil();
//
//        return retrofituil.create(MovieService.class).getSoonMovie(start, count)
//
//                .filter(new Func1<HttpResult<Subjects>, Boolean>() {
//                    @Override
//                    public Boolean call(HttpResult<Subjects> subjectHttpResult) {
//                        if (subjectHttpResult.getCount() > 5) { //测试 filter 的返回值 可以用于判断返回成功
//                            return true; //表示继续
//                        } else {
//                            //抛出异常
//                            throw new ApiException(subjectHttpResult.getCount());
////                            return false; //表示在这里断开
//                        }
//                    }
//                }).flatMap(new Func1<HttpResult<Subjects>, Observable<List<Subjects>>>() { //转化成我们想要的集合
//                    @Override
//                    public Observable<List<Subjects>> call(final HttpResult<Subjects> subjectHttpResult) {
//
//                        return Observable.create(new Observable.OnSubscribe<List<Subjects>>() {
//                            @Override
//                            public void call(Subscriber<? super List<Subjects>> subscriber) {
//                                subscriber.onNext(subjectHttpResult.getSubjects());
//                                subscriber.onCompleted();
//                            }
//                        });
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

}
