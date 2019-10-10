package com.matrix.yukun.matrix.tool_module.weather.activity;

/**
 * author: kun .
 * date:   On 2019/10/10
 */
public class CitySortBean {
    private String sortLetters;
    private CityBean mCityBean;

    public CitySortBean(String sortLetters, CityBean cityBean) {
        this.sortLetters = sortLetters;
        mCityBean = cityBean;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public CityBean getCityBean() {
        return mCityBean;
    }

    public void setCityBean(CityBean cityBean) {
        mCityBean = cityBean;
    }

    @Override
    public String toString() {
        return "CitySortBean{" +
                "sortLetters='" + sortLetters + '\'' +
                ", mCityBean=" + mCityBean +
                '}';
    }
}
