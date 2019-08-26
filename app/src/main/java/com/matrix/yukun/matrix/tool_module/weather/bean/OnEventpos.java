package com.matrix.yukun.matrix.tool_module.weather.bean;

/**
 * Created by Administrator on 2017/3/5.
 */
public class OnEventpos {
    public int pos;
    public String city;

    public OnEventpos(String city) {
        this.city = city;
    }

    public OnEventpos(int pos) {
        this.pos = pos;
    }
}
