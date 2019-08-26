package com.matrix.yukun.matrix.tool_module.weather.present;

import com.matrix.yukun.matrix.tool_module.weather.bean.WeaTomorrow;

/**
 * Created by yukun on 17-3-7.
 */
public interface TomorrowFragmentImpl {
    void showMessage(String msg);
    void getInfo(WeaTomorrow weaTomorrow);
    void dismissDialogs();
    void setListener();
}
