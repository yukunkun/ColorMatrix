package com.matrix.yukun.matrix.tool_module.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.NoScrollListView;
import com.matrix.yukun.matrix.selfview.NoScrollRecyclerView;
import com.matrix.yukun.matrix.selfview.SunriseView;
import com.matrix.yukun.matrix.tool_module.weather.adapter.ConfAdapter;
import com.matrix.yukun.matrix.tool_module.weather.adapter.RVFutureAdapter;
import com.matrix.yukun.matrix.tool_module.weather.adapter.RVPosizonAdapter;
import com.matrix.yukun.matrix.tool_module.weather.bean.OnEventpos;
import com.matrix.yukun.matrix.util.AnimUtils;
import com.matrix.yukun.matrix.util.Notifications;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowStation;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

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
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.ll_bg)
    RelativeLayout llBg;
    @BindView(R.id.recyclerview)
    RecyclerView mRVFuture;
    @BindView(R.id.iv_future)
    ImageView ivFuture;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.scrollListView)
    NoScrollListView scrollListView;
    @BindView(R.id.av_load)
    AVLoadingIndicatorView avLoad;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_neb)
    TextView tvNeb;
    @BindView(R.id.recycler_neber)
    NoScrollRecyclerView recyclerNeber;
    @BindView(R.id.tv_qua)
    TextView tvQua;
    @BindView(R.id.tv_today_pm)
    TextView tvTodayPm;
    @BindView(R.id.sunrise)
    SunriseView sunrise;
    @BindView(R.id.moondown)
    SunriseView moondown;
    @BindView(R.id.tv_future)
    TextView tvFuture;
    private GestureDetector detector;
    private boolean animTag;
    private LinearLayoutManager linearLayoutManager;
    private String mCity = "成都";
    private String url = "http://m.weathercn.com/index.do?partner=1000001041_hfaw&id=106774&p_source=searchbrowser&p_type=jump";
    private List<ForecastBase> mForecastBase = new ArrayList<>();
    private List<LifestyleBase> mLifestyleBases = new ArrayList<>();
    private List<AirNowStation> mAirNowStations = new ArrayList<>();
    private RVFutureAdapter mRvFutureAdapter;
    private ConfAdapter mConfAdapter;
    private RVPosizonAdapter mRvPosizonAdapter;

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
        detector = new GestureDetector(this, new listener());
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvFutureAdapter = new RVFutureAdapter(this, mForecastBase);
        mRVFuture.setLayoutManager(linearLayoutManager);
        mRVFuture.setAdapter(mRvFutureAdapter);
        mConfAdapter = new ConfAdapter(this, mLifestyleBases);
        scrollListView.setAdapter(mConfAdapter);
        mRvPosizonAdapter = new RVPosizonAdapter(this, R.layout.item_weather_posizion, mAirNowStations);
        recyclerNeber.setLayoutManager(new LinearLayoutManager(this));
        recyclerNeber.setAdapter(mRvPosizonAdapter);
        recyclerNeber.setHasFixedSize(true);
        recyclerNeber.setNestedScrollingEnabled(false);
        OverScrollDecoratorHelper.setUpOverScroll(mRVFuture, LinearLayoutManager.VERTICAL);
        ivBg.post(new Runnable() {
            @Override
            public void run() {
                AnimUtils.setWeatherBG(ivBg);
            }
        });
    }

    @Override
    public void initDate() {
        todayCity.setText(mCity);
        HeWeather.getWeatherNow(this, mCity, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                LogUtil.i("=======throwable", throwable.toString());
            }

            @Override
            public void onSuccess(Now now) {
                avLoad.hide();
                LogUtil.i("=======now", new Gson().toJson(now));
                if (Code.OK.getCode().equalsIgnoreCase(now.getStatus())) {
                    NowBase nowNow = now.getNow();
                    String loc = now.getUpdate().getLoc();
                    todayTime.setText(loc);
                    todayClass.setText(nowNow.getCond_txt());
                    if (nowNow.getCond_txt().length() <= 4 && nowNow.getCond_txt().length() >= 3) {
                        todayClass.setTextSize(34);
                    } else if (nowNow.getCond_txt().length() > 4) {
                        todayClass.setTextSize(30);
                    }
                    tvTemperature.setText(nowNow.getTmp() + "℃");
                    today1.setText("体感温度:" + nowNow.getTmp() + "℃");
                    today2.setText("相对湿度:" + nowNow.getHum() + "%");
                    today3.setText("降水量:" + nowNow.getPcpn() + "mm");
                    today4.setText("气压:" + nowNow.getPres());
                    today5.setText("温度:" + nowNow.getTmp());
                    today6.setText("能见度:" + nowNow.getVis() + "km");
                    todayPower1.setText("风向:" + nowNow.getWind_dir());
                    todayPower2.setText("风力:" + nowNow.getWind_deg());
                    todayPower3.setText("风速:" + nowNow.getWind_spd() + "kmph");
                    String code = nowNow.getCond_code();
                    EventBus.getDefault().post(new OnEventpos(Integer.valueOf(code)));
                    updateBg(code);
                    Notifications.start(HeWeatherActivity.this, mCity + ":" + nowNow.getFl() + "℃");
                }
            }
        });

//        HeWeather.getWeatherHourly(this, mCity, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherHourlyBeanListener() {
//            @Override
//            public void onError(Throwable throwable) {
//                LogUtil.i("=======throwable", throwable.toString());
//            }
//
//            @Override
//            public void onSuccess(Hourly hourly) {
//                LogUtil.i("=======hourly", new Gson().toJson(hourly));
//            }
//        });
        HeWeather.getWeatherForecast(this, mCity, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                LogUtil.i("=======throwable", throwable.toString());
            }

            @Override
            public void onSuccess(Forecast forecast) {
                if (Code.OK.getCode().equalsIgnoreCase(forecast.getStatus())) {
                    mForecastBase.clear();
                    mForecastBase.addAll(forecast.getDaily_forecast());
                    mRvFutureAdapter.notifyDataSetChanged();
                    ForecastBase forecastBase = forecast.getDaily_forecast().get(0);
                    updateSunMoon(forecastBase);
                }
                LogUtil.i("=======forecast", new Gson().toJson(forecast));
            }
        });
        HeWeather.getWeatherLifeStyle(this, mCity, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                LogUtil.i("=======throwable", throwable.toString());
            }

            @Override
            public void onSuccess(Lifestyle lifestyle) {
                if (Code.OK.getCode().equalsIgnoreCase(lifestyle.getStatus())) {
                    mLifestyleBases.clear();
                    mLifestyleBases.addAll(lifestyle.getLifestyle());
                    mConfAdapter.notifyDataSetChanged();
                }
                LogUtil.i("=======lifestyle", new Gson().toJson(lifestyle));
            }
        });

        HeWeather.getAirNow(this, mCity, new HeWeather.OnResultAirNowBeansListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(AirNow airNow) {
                if (Code.OK.getCode().equalsIgnoreCase(airNow.getStatus())) {
                    List<AirNowStation> airNowStation = airNow.getAir_now_station();
                    mAirNowStations.clear();
                    mAirNowStations.addAll(airNowStation);
                    mRvPosizonAdapter.notifyDataSetChanged();
                    String qlty = airNow.getAir_now_city().getQlty();
                    if (qlty.equals("优")) {
                        tvQua.setTextColor(getResources().getColor(R.color.color_44fc2c));
                    } else if (qlty.equals("良")) {
                        tvQua.setTextColor(getResources().getColor(R.color.color_2299ee));
                    } else {
                        tvQua.setTextColor(getResources().getColor(R.color.color_fc2c5d));
                    }
                    tvQua.setText(qlty);
                    tvTodayPm.setText("PM2.5: " + airNow.getAir_now_city().getPm25());
                }
                LogUtil.i("=======airNow", new Gson().toJson(airNow));

            }
        });
    }

    private void updateSunMoon(ForecastBase forecastBase) {
        sunrise.setHeadText("日出 "+forecastBase.getSr());
        sunrise.setBackText("日落 "+forecastBase.getSs());
        moondown.setIcon(R.mipmap.icon_weather_sun);
        moondown.setHeadText("月出 "+forecastBase.getMr());
        moondown.setBackText("月落 "+forecastBase.getMs());
        moondown.setIcon(R.mipmap.icon_weather_moon);
        sunrise.doAnimation();
        moondown.doAnimation();
    }

    private void updateBg(String code) {
        int pos = Integer.valueOf(code);
        if (pos < 102) {
            Glide.with(this).load(R.mipmap.wea_chuqing)
                    .into(ivBg);
        } else if (pos <= 104) {
            Glide.with(this).load(R.mipmap.wea_ying)
                    .into(ivBg);
        } else if (pos <= 213) {
            Glide.with(this).load(R.mipmap.wea_cloud)
                    .into(ivBg);
        } else if (pos <= 313) {
            Glide.with(this).load(R.mipmap.wea_rain)
                    .into(ivBg);
        } else if (pos <= 406) {
            Glide.with(this).load(R.mipmap.wea_snow)
                    .into(ivBg);
        } else if (pos <= 502) {
            Glide.with(this).load(R.mipmap.wea_wu)
                    .into(ivBg);
        } else {
            Glide.with(this).load(R.mipmap.wea_chuqing)
                    .into(ivBg);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SearchCityActivity.RESULT && resultCode == SearchCityActivity.RESULT) {
            String result = data.getStringExtra("result");
            mCity = result;
            avLoad.show();
            initDate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class listener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 != null) {
                float beginY = e1.getY();
                float endY = e2.getY();
                if (beginY - endY > 60 && Math.abs(velocityY) > 0) {   //上滑
                    if (animTag) {
                        animTag = false;
                        AnimUtils.setTitleUp(HeWeatherActivity.this, rlTitle);
                    }
                } else if (endY - beginY > 60 && Math.abs(velocityY) > 0) {   //下滑
                    if (animTag == false) {
                        animTag = true;
                        AnimUtils.setTitleDown(HeWeatherActivity.this, rlTitle);
                    }
                }
            }
            return false;
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_main, R.id.iv_search, R.id.iv_future, R.id.tv_future})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_main:

                break;
            case R.id.iv_future:
            case R.id.tv_future:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.iv_search:
                SearchCityActivity.start(this);
                break;
        }
    }
}
