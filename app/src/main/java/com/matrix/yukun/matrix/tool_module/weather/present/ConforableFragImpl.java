package com.matrix.yukun.matrix.tool_module.weather.present;


import com.matrix.yukun.matrix.tool_module.weather.bean.WeaLifePoint;

/**
 * Created by yukun on 17-3-7.
 */
public interface ConforableFragImpl {
    void showMessage(String msg);
    void dismissDialogs();
    void setListener();
    void getLifeInfo(WeaLifePoint weaLifePoint);
}
