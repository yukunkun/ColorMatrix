package com.matrix.yukun.matrix.weather_module.present;

import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaHours;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;

/**
 * Created by yukun on 17-3-7.
 */
public interface ConforableFragImpl {
    void showMessage(String msg);
    void dismissDialogs();
    void setListener();
    void getLifeInfo(WeaLifePoint weaLifePoint);
}
