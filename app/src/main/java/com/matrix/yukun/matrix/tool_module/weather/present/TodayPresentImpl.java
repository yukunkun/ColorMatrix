package com.matrix.yukun.matrix.tool_module.weather.present;


import com.matrix.yukun.matrix.tool_module.weather.util.BasePresentImpl;

/**
 * Created by yukun on 17-3-6.
 */
public interface TodayPresentImpl extends BasePresentImpl {
    void getInfo(String city);
    void getHours(String city);

}
