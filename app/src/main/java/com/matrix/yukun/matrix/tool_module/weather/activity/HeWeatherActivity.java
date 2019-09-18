package com.matrix.yukun.matrix.tool_module.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.log.LogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.forecast.AirForecast;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class HeWeatherActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.today_city)
    TextView todayCity;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.rl_city)
    RelativeLayout rlCity;
    @BindView(R.id.today_time)
    TextView todayTime;
    @BindView(R.id.today_class)
    TextView todayClass;
    @BindView(R.id.real)
    RelativeLayout real;
    @BindView(R.id.today_image)
    ImageView todayImage;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.rl_wea)
    RelativeLayout rlWea;
    @BindView(R.id.recyclerViews)
    RecyclerView recyclerViews;
    @BindView(R.id.today_1)
    TextView today1;
    @BindView(R.id.today_2)
    TextView today2;
    @BindView(R.id.today_3)
    TextView today3;
    @BindView(R.id.today_4)
    TextView today4;
    @BindView(R.id.today_5)
    TextView today5;
    @BindView(R.id.today_6)
    TextView today6;
    @BindView(R.id.today_power_1)
    TextView todayPower1;
    @BindView(R.id.today_power_2)
    TextView todayPower2;
    @BindView(R.id.today_power_3)
    TextView todayPower3;
    @BindView(R.id.power_lin)
    LinearLayout powerLin;

    private String mCity = "成都";

    public static void start(Context context) {
        Intent intent = new Intent(context, HeWeatherActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_he_weather;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDate() {

        HeWeather.getWeatherNow(this, mCity, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                LogUtil.i("=======throwable", throwable.toString());
            }

            @Override
            public void onSuccess(Now now) {
                LogUtil.i("=======now", new Gson().toJson(now));
                if(Code.OK.getCode().equalsIgnoreCase(now.getStatus())){
                    NowBase nowNow = now.getNow();
                    tvTemperature.setText(nowNow.getTmp()+"℃");

                }
            }
        });

        HeWeather.getWeatherHourly(this, mCity, Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherHourlyBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                LogUtil.i("=======throwable", throwable.toString());
            }

            @Override
            public void onSuccess(Hourly hourly) {
                LogUtil.i("=======hourly", new Gson().toJson(hourly));
            }
        });

        HeWeather.getWeatherLifeStyle(this, mCity, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                LogUtil.i("=======throwable", throwable.toString());
            }

            @Override
            public void onSuccess(Lifestyle lifestyle) {
                LogUtil.i("=======lifestyle", new Gson().toJson(lifestyle));

            }
        });

        HeWeather.getAirNow(this, mCity, new HeWeather.OnResultAirNowBeansListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(AirNow airNow) {
                LogUtil.i("=======airNow", new Gson().toJson(airNow));
            }
        });
    }

    @Override
    public void initListener() {

    }


    @OnClick({R.id.iv_back, R.id.iv_main, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_main:

                break;
            case R.id.iv_search:

                break;
        }
    }
}
