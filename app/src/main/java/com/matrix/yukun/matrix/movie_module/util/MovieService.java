package com.matrix.yukun.matrix.movie_module.util;


import com.matrix.yukun.matrix.movie_module.bean.HttpResult;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yukun on 17-2-15.
 */
public interface MovieService {

    @GET("v2/movie/top250")
    Observable<HttpResult<Subjects>> getTopMovie(@Query("start") int start, @Query("count") int count);
    @GET("v2/movie/in_theaters")
    Observable<HttpResult<Subjects>> getNewMovie(@Query("start") int start, @Query("count") int count);
    @GET("v2/movie/coming_soon")
    Observable<HttpResult<Subjects>> getSoonMovie(@Query("start") int start, @Query("count") int count);

    //weather API
    @GET("forecast")
    Observable<WeaTomorrow> getTomorrow(@Query("city") String city, @Query("key") String key);
    @GET("now")
    Observable<WeaNow> getNow(@Query("city") String city, @Query("key") String key);
    @GET("hourly")
    Observable<WeaHours> getHourly(@Query("city") String city, @Query("key") String key);
    @GET("suggestion")
    Observable<WeaLifePoint> getLife(@Query("city") String city, @Query("key") String key);
    @GET("alarm")
    Observable<WeaDestory> getAlarm(@Query("city") String city, @Query("key") String key);

}
