package com.matrix.yukun.matrix.tool_module.weather.amap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.weather.activity.MapWeaDialog;
import com.matrix.yukun.matrix.util.log.LogUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class AMapActivity extends BaseActivity implements AMap.OnPOIClickListener, AMap.OnMarkerClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_leavel)
    ImageView ivSearch;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_near)
    TextView tvNear;
    @BindView(R.id.tv_line)
    TextView tvLine;
    @BindView(R.id.iv_map_normal)
    ImageView ivMapNormal;
    @BindView(R.id.iv_map_plant)
    ImageView ivMapPlant;
    @BindView(R.id.iv_map_night)
    ImageView ivMapNight;
    @BindView(R.id.iv_map_traffic)
    ImageView ivMapTraffic;
    @BindView(R.id.cv_map_leavel)
    CardView cvMapLeavel;
    private AMap mMap;
    private AMapInit mAMapInit;
    private LatLng mClickLat;
    private BottomSheetDialog mSheetDialog ;


    public static void start(Context context) {
        Intent intent = new Intent(context, AMapActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void createMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_amap;
    }

    @Override
    public void initView() {
        mMap = mMapView.getMap();
        mAMapInit = AMapInit.instance();
        mAMapInit.init(this, mMap);
    }

    @Override
    public void initDate() {

        mMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                ToastUtils.showToast(cameraPosition.toString()+"");
            }
        });
    }

    @Override
    public void initListener() {
        mMap.setOnPOIClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        release();
    }

    private void release() {
        if (mAMapInit != null) {
            mAMapInit.onDestory();
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @OnClick({R.id.iv_back, R.id.iv_leavel,R.id.tv_weather, R.id.tv_near, R.id.tv_line, R.id.iv_map_normal, R.id.iv_map_plant, R.id.iv_map_night, R.id.iv_map_traffic
            })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                release();
                break;
            case R.id.iv_leavel:
                if(cvMapLeavel.getVisibility()==View.VISIBLE){
                    cvMapLeavel.setVisibility(View.GONE);
                }else {
                    cvMapLeavel.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_weather:
                mAMapInit.seatherSearchQuery("成都", new WeatherSearch.OnWeatherSearchListener() {
                    @Override
                    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                        LogUtil.i("==========",localWeatherLiveResult.toString());
                    }

                    @Override
                    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                        LogUtil.i("==========",localWeatherForecastResult.toString());
                    }
                });
                break;
            case R.id.tv_near:
                showNearDialog();
                break;
            case R.id.tv_line:
                NavMapActivity.start(this);
                break;
            case R.id.iv_map_normal:
                mMap.setMapType(AMap.MAP_TYPE_NORMAL);
                break;
            case R.id.iv_map_plant:
                mMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.iv_map_night:
                mMap.setMapType(AMap.MAP_TYPE_NIGHT);
                break;
            case R.id.iv_map_traffic:
                mMap.setMapType(AMap.MAP_TYPE_BUS);
                break;
        }
    }

    private void showNearDialog() {
        if(mSheetDialog==null||!mSheetDialog.isShowing()){
            mSheetDialog = new BottomSheetDialog(this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.map_dialog_fragment,  null, false);
            mSheetDialog.setContentView(inflate);
            mSheetDialog.setCanceledOnTouchOutside(true);
            mSheetDialog.setCancelable(true);
            mSheetDialog.getWindow().setDimAmount(0); //背景透明
            mSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
            mSheetDialog.show();
        }else {
            mSheetDialog.dismiss();
        }

    }

    //点击
    @Override
    public void onPOIClick(Poi poi) {
        mMap.clear();
        mAMapInit.addGrowMarker(poi.getCoordinate());
        PoiSearch poiSearch = new PoiSearch(this, null);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                MapWeaDialog instance = MapWeaDialog.getInstance();
                instance.show(getSupportFragmentManager(),"");
            }
        });
        poiSearch.searchPOIIdAsyn(poi.getPoiId());// 异步搜索

//        MarkerOptions markOptiopns = new MarkerOptions();
//        markOptiopns.position(poi.getCoordinate());
//        TextView textView = new TextView(getApplicationContext());
//        textView.setText("到"+poi.getName()+"去");
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.BLACK);
//        textView.setBackgroundResource(R.drawable.balloon_l_pressed);
//        markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
//        mMap.addMarker(markOptiopns);
    }

    //marker
    @Override
    public boolean onMarkerClick(Marker marker) {
        // 构造导航参数
//        NaviPara naviPara = new NaviPara();
//        // 设置终点位置
//        naviPara.setTargetPoint(marker.getPosition());
//        // 设置导航策略，这里是避免拥堵
//        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
//        try {
//            // 调起高德地图导航
//            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
//        } catch (com.amap.api.maps.AMapException e) {
//            // 如果没安装会进入异常，调起下载页面
//            AMapUtils.getLatestAMapApp(getApplicationContext());
//        }
//        mMap.clear();
        return false;
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
//        mMapView.onSaveInstanceState(outState);
//    }
}
