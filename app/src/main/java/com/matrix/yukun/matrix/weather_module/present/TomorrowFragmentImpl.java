package com.matrix.yukun.matrix.weather_module.present;

import android.view.View;

import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;

/**
 * Created by yukun on 17-3-7.
 */
public interface TomorrowFragmentImpl {
    void showMessage(String msg);
    void getInfo(WeaTomorrow weaTomorrow);
    void dismissDialogs();
    void setListener();
}
