package com.matrix.yukun.matrix.weather_module.present;

import android.view.View;

import com.matrix.yukun.matrix.movie_module.present.BasePresentImpl;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;

/**
 * Created by yukun on 17-3-7.
 */
public interface ConfortablePresentImpl extends BasePresentImpl {
    void getInfo(String city);

}
