package com.matrix.yukun.matrix.weather_module.present;

import android.view.View;

import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;

import java.util.List;

/**
 * Created by yukun on 17-3-6.
 */
public interface TodayFragmentImpl {

    void getInfo(WeaNow weatherNet);
    void showMessage(String msg);
    void dismissDialogs();
    void setListener();
    void getViews(View view);
    void getDestory(WeaDestory weaDestory);
    void getHoursInfo(WeaHours weaHours);

}
