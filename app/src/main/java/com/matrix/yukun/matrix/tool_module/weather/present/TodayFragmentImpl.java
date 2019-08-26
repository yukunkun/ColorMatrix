package com.matrix.yukun.matrix.tool_module.weather.present;


import android.view.View;

import com.matrix.yukun.matrix.tool_module.weather.bean.WeaDestory;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaHours;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaNow;

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
