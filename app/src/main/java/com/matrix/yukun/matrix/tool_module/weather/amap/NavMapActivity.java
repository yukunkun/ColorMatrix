package com.matrix.yukun.matrix.tool_module.weather.amap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiSearch;
import com.googlecode.mp4parser.authoring.tracks.Amf0Track;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.log.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavMapActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_enter)
    LinearLayout llEnter;
    @BindView(R.id.in_change)
    ImageView inChange;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.lv_search)
    ListView lvSearch;
    private AMap mAMap;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private boolean followMove=true;
    private AMapInit mAMapInit;
    private LatLng mCurrentLatLng;

    public static void start(Context context) {
        Intent intent = new Intent(context, NavMapActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void createMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_nav_map;
    }

    @Override
    public void initView() {
        mAMap = mMapView.getMap();
        mAMapInit = AMapInit.instance();
        mAMapInit.init(this,mAMap);
    }

    @Override
    public void initDate() {
        init();
        mCurrentLatLng = MyApp.getLatLng();
        mAMapInit.changeMapCenter(mCurrentLatLng);
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_back, R.id.in_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.in_change:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        release();
    }

    private void release() {
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

    private void init() {
        mUiSettings = mAMap.getUiSettings();
        initSetting();
        initLocat();
    }

    private void initLocat() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mAMap.showIndoorMap(true);
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//                LogUtil.i("=========",followMove+"");
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (followMove) {
                    mAMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng));
                }
//                LogUtil.i("=========",location.toString());
            }
        });

        mAMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                followMove=false;
            }
        });
    }

    private void initSetting() {
        mUiSettings.setZoomControlsEnabled(true);//是否显示缩放
        mAMap.setLocationSource(this);//通过aMap对象设置定位数据源的监听
        mAMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        mUiSettings.setScaleControlsEnabled(true); //地图比例
        mUiSettings.setCompassEnabled(false); //指南针
        mUiSettings.setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置
    }

    public void changeMapCenter(){
        LatLng latLng = MyApp.getLatLng();
        if(latLng!=null){
            mAMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                LogUtil.i("================", errText);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

}
