package com.matrix.yukun.matrix.movie_module.util;


import com.matrix.yukun.matrix.chat_module.ChatInfo;
import com.matrix.yukun.matrix.movie_module.bean.HttpResult;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yukun on 17-2-15.
 */
public interface MovieService {

//    @GET("v2/movie/top250")
//    Observable<HttpResult<Subjects>> getTopMovie(@Query("start") int start, @Query("count") int count);
//    @GET("v2/movie/in_theaters")
//    Observable<HttpResult<Subjects>> getNewMovie(@Query("start") int start, @Query("count") int count);
//    @GET("v2/movie/coming_soon")
//    Observable<HttpResult<Subjects>> getSoonMovie(@Query("start") int start, @Query("count") int count);
    @GET("v2/movie/top250")
    Call<HttpResult<Subjects>> getTopMovie(@Query("start") int start, @Query("count") int count);
    @GET("v2/movie/in_theaters")
    Call<HttpResult<Subjects>> getNewMovie(@Query("start") int start, @Query("count") int count);
    @GET("v2/movie/coming_soon")
    Call<HttpResult<Subjects>> getSoonMovie(@Query("start") int start, @Query("count") int count);

//    @GET("weather/now")
//    Observable<WeaNow> getNow(@Query("location") String city, @Query("key") String key);
    //    @GET("s6/weather/hourly")
//    Observable<WeaHours> getHourly(@Query("location") String city, @Query("key") String key);
    //weather API
//    @GET("s6/weather/forecast")
//    Observable<WeaTomorrow> getTomorrow(@Query("location") String city, @Query("key") String key);
    //    @GET("weather/suggestion")
//    Observable<WeaLifePoint> getLife(@Query("location") String city, @Query("key") String key);
//    @GET("weather/suggestion")
//    Call<WeaLifePoint> getLife(@Query("location") String city, @Query("key") String key);
    @GET("weather/forecast")
    Call<WeaTomorrow> getTomorrows(@Query("location") String city, @Query("key") String key);
    @GET("weather/now")
    Call<WeaNow> getNows(@Query("location") String city, @Query("key") String key);
    @GET("weather/hourly")
    Call<WeaHours> getHourlys(@Query("location") String city, @Query("key") String key);
    @GET("weather/lifestyle")
    Call<WeaLifePoint> getLifes(@Query("location") String city, @Query("key") String key);
    @GET("weather/alarm")
    Observable<WeaDestory> getAlarm(@Query("location") String city, @Query("key") String key);

    //chat API
    @GET("robot/index")
    Call<ChatInfo> getChat(@Query("info") String info, @Query("key") String key);
}
