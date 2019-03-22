package com.matrix.yukun.matrix.movie_module.util;


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
/**
 * Created by yukun on 17-2-15.
 */
public interface MovieService {
    @GET("v2/movie/top250")
    Call<HttpResult<Subjects>> getTopMovie(@Query("start") int start, @Query("count") int count);
    @GET("v2/movie/in_theaters")
    Call<HttpResult<Subjects>> getNewMovie(@Query("start") int start, @Query("count") int count);
    @GET("v2/movie/coming_soon")
    Call<HttpResult<Subjects>> getSoonMovie(@Query("start") int start, @Query("count") int count);
    @GET("weather/forecast")
    Call<WeaTomorrow> getTomorrows(@Query("location") String city, @Query("key") String key);
    @GET("weather/now")
    Call<WeaNow> getNows(@Query("location") String city, @Query("key") String key);
    @GET("weather/hourly")
    Call<WeaHours> getHourlys(@Query("location") String city, @Query("key") String key);
    @GET("weather/lifestyle")
    Call<WeaLifePoint> getLifes(@Query("location") String city, @Query("key") String key);

}
