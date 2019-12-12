package com.matrix.yukun.matrix.tool_module.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.barrage.dialog.BaseBottomDialog;
import com.matrix.yukun.matrix.tool_module.weather.adapter.RVFutureAdapter;
import com.matrix.yukun.matrix.tool_module.weather.bean.OnEventpos;
import com.matrix.yukun.matrix.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * author: kun .
 * date:   On 2019/10/23
 */
public class MapWeaDialog extends BaseBottomDialog {

    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_detail_place)
    TextView tvDetailPlace;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_quality)
    TextView tvQuality;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
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
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.iv_future)
    ImageView ivFuture;
    @BindView(R.id.tv_future)
    TextView tvFuture;
    @BindView(R.id.rl_tomorrow_title)
    RelativeLayout rlTomorrowTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private String url = "http://m.weathercn.com/index.do?partner=1000001041_hfaw&id=106774&p_source=searchbrowser&p_type=jump";
    private List<ForecastBase> mForecastBase = new ArrayList<>();
    private RVFutureAdapter mRvFutureAdapter;
    private String mCity = "";
    private static PoiItem mPoiItem;
    private static LatLng mLatLng;
    private static Tip mTip;
    public static MapWeaDialog getInstance(PoiItem poiItem, LatLng latLng) {
        mPoiItem=poiItem;
        mLatLng=latLng;
        return new MapWeaDialog();
    }

    public static MapWeaDialog getInstance(Tip tip, LatLng latLng) {
        mTip=tip;
        mLatLng=latLng;
        return new MapWeaDialog();
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        ButterKnife.bind(this,inflate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvFutureAdapter = new RVFutureAdapter(getContext(), mForecastBase);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setAdapter(mRvFutureAdapter);
        if(mPoiItem!=null){
            mCity=mPoiItem.getCityName();
            tvPlace.setText(mPoiItem.getTitle());
            tvDetailPlace.setText(mPoiItem.getCityName()+"-"+mPoiItem.getDirection()+"-"+mPoiItem.getAdName()+"-"+mPoiItem.getSnippet());
            if(mLatLng!=null){
                float distance = AMapUtils.calculateLineDistance(new LatLng(mPoiItem.getLatLonPoint().getLatitude(),mPoiItem.getLatLonPoint().getLongitude()),mLatLng);
                tvDistance.setText(distance<=2000 ?"距离："+String.format("%.2f", distance)+" 米":"距离："+String.format("%.3f", distance/1000)+" 千米");
            }
        }else {
            mCity= SPUtils.getInstance().getString("city");
            tvPlace.setText(mTip.getName());
            tvDetailPlace.setText(mTip.getDistrict()+"-"+mTip.getAddress());
            LatLng latLng = new LatLng(mTip.getPoint().getLatitude(), mTip.getPoint().getLongitude());
            if(mLatLng!=null){
                float distance = AMapUtils.calculateLineDistance(latLng,mLatLng);
                tvDistance.setText(distance<=2000 ?"距离："+String.format("%.2f", distance)+" 米":"距离："+String.format("%.3f", distance/1000)+" 千米");
            }
        }
    }

    @Override
    protected void initData() {
        HeWeather.getWeatherNow(getContext(), mCity, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                LogUtil.i("=======throwable", throwable.toString());
            }

            @Override
            public void onSuccess(Now now) {
                if (Code.OK.getCode().equalsIgnoreCase(now.getStatus())) {
                    NowBase nowNow = now.getNow();
                    String loc = now.getUpdate().getLoc();
                    tvQuality.setText(nowNow.getCond_txt());
                    tvTemp.setText(nowNow.getTmp() + "℃");
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
                }
            }
        });

        HeWeather.getWeatherForecast(getContext(), mCity, new HeWeather.OnResultWeatherForecastBeanListener() {
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
                } else {
                    ToastUtils.showToast("部分地区获取日出数据异常");
                }
//                LogUtil.i("=======forecast", new Gson().toJson(forecast));
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    public int setLayout() {
        return R.layout.map_dialog_fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0f;
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.height = ScreenUtil.getDisplayHeight() * 6 / 10;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    @OnClick({R.id.tv_place, R.id.tv_future})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_place:
                break;
            case R.id.tv_future:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
        }
    }
}
