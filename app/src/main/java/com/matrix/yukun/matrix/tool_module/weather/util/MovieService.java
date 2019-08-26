package com.matrix.yukun.matrix.tool_module.weather.util;

import com.matrix.yukun.matrix.tool_module.weather.bean.WeaHours;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaLifePoint;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaNow;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaTomorrow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yukun on 17-2-15.
 */
public interface MovieService {
    @GET("weather/forecast")
    Call<WeaTomorrow> getTomorrows(@Query("location") String city, @Query("key") String key);
    @GET("weather/now")
    Call<WeaNow> getNows(@Query("location") String city, @Query("key") String key);
    @GET("weather/hourly")
    Call<WeaHours> getHourlys(@Query("location") String city, @Query("key") String key);
    @GET("weather/lifestyle")
    Call<WeaLifePoint> getLifes(@Query("location") String city, @Query("key") String key);

}
