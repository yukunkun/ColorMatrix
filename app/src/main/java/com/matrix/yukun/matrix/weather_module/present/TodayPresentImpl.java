package com.matrix.yukun.matrix.weather_module.present;

import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;

/**
 * Created by yukun on 17-3-6.
 */
public interface TodayPresentImpl extends BasePresentImpl {
    void getInfo(String city);
    void getDestory();
    void getHours();

}
