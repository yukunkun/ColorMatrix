package com.matrix.yukun.matrix.movie_module.util;


import com.lecloud.sdk.api.md.entity.vod.cloud.Video;
import com.matrix.yukun.matrix.leshi_module.bean.LeShiListBean;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshi_module.bean.ListBeanJson;
import com.matrix.yukun.matrix.leshi_module.bean.VideoBean;
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
    //leshi API
    @GET("open.php")
    Observable<LeShiListBean<ListBean>> getLeShiList(@Query("user_unique") String user_unique, @Query("timestamp") String timestamp, @Query("api") String api, @Query("format") String formate,
                                                     @Query("ver") String ver, @Query("index") int index, @Query("size") int size, @Query("sign") String sign);
    @GET("open.php")
    Observable<ListBeanJson<VideoBean>> getLeShiMov(@Query("user_unique") String user_unique, @Query("timestamp") String timestamp, @Query("api") String api, @Query("format") String formate,
                                                    @Query("ver") String ver, @Query("video_id") int video_id, @Query("sign") String sign);
    @GET("open.php")
    Observable<ListBeanJson<Object>> getVideoPause(@Query("user_unique") String user_unique, @Query("timestamp") String timestamp, @Query("api") String api, @Query("format") String formate,
                                                    @Query("ver") String ver, @Query("video_id") int video_id, @Query("sign") String sign);
    @GET("open.php")
    Observable<ListBeanJson<Object>> getVideoStart(@Query("user_unique") String user_unique, @Query("timestamp") String timestamp, @Query("api") String api, @Query("format") String formate,
                                                    @Query("ver") String ver, @Query("video_id") int video_id, @Query("sign") String sign);
}
