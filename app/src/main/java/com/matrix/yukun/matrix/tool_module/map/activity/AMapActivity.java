package com.matrix.yukun.matrix.tool_module.map.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.selfview.SegmentedGroup;
import com.matrix.yukun.matrix.tool_module.map.MapWeaDialog;
import com.matrix.yukun.matrix.tool_module.weather.activity.SearchResultAdapter;
import com.matrix.yukun.matrix.tool_module.map.maputil.AMapInit;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

public class AMapActivity extends BaseActivity implements AMap.OnPOIClickListener, AMap.OnMarkerClickListener, LocationSource, AMapLocationListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_leavel)
    ImageView ivLea;
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
    @BindView(R.id.iv_search)
    ImageView ivSearch;

    private AMap mMap;
    private AMapInit mAMapInit;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private boolean followMove=true;
    private String[] items = {"住宅区", "学校", "楼宇", "商场"};
    private String mCurrentType = items[0];
    private BottomSheetDialog mSheetDialog;
    private LatLng mLatLng;
    private int currentPage=0;
    private boolean isWeather = true;
    private ListView mListView;
    private SearchResultAdapter mSearchResultAdapter;
    private PoiItem mCurrentPoiItem;
    private BottomSheetDialog mSheetSearchDialog;
    private MapWeaDialog mMapWeaDialog;
    private ArrayList<PoiItem> mPoisList = new ArrayList<>();
    private LatLng mCurrentLatLng;

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
        tvWeather.setSelected(true);
        tvLine.setSelected(true);
        initSetting();
        initLocat();
    }

    @Override
    public void initDate() {
        mCurrentLatLng = MyApp.getLatLng();
        mAMapInit.changeMapCenter(mCurrentLatLng);
    }

    @Override
    public void initListener() {
        mMap.setOnPOIClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                ToastUtils.showToast(cameraPosition.toString()+"");
            }
        });

        AMapLocationClient mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        mLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        //解析定位结果
                        SPUtils.getInstance().saveString("city", aMapLocation.getCity());
                        SPUtils.getInstance().saveString("CityCode", aMapLocation.getCityCode());
                    } else {
                        ToastUtils.showToast("请确保定位已开启");
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @OnClick({R.id.iv_back, R.id.iv_leavel, R.id.tv_weather, R.id.tv_near, R.id.tv_line, R.id.iv_map_normal, R.id.iv_map_plant, R.id.iv_map_night, R.id.iv_map_traffic
            , R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_leavel:
                if (cvMapLeavel.getVisibility() == View.VISIBLE) {
                    cvMapLeavel.setVisibility(View.GONE);
                } else {
                    cvMapLeavel.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_weather:
                tvNear.setSelected(false);
                tvWeather.setSelected(true);
                isWeather = true;
                break;
            case R.id.tv_near:
                tvNear.setSelected(true);
                tvWeather.setSelected(false);
                isWeather = false;
                showNearDialog();
                if (mLatLng != null) {
                    searchNear(new LatLonPoint(mLatLng.longitude, mLatLng.latitude));
                } else {
                    ToastUtils.showToast("定位失败");
                }
                break;
            case R.id.iv_search:
                MapSearchActivity.start(this, ivSearch);
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
        if (mSheetDialog == null || !mSheetDialog.isShowing()) {
            mSheetDialog = new BottomSheetDialog(this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.sheet_dialog_layout, null, false);
            initViewSheet(inflate);
            mSheetDialog.setContentView(inflate);
            mSheetDialog.setCanceledOnTouchOutside(true);
            mSheetDialog.setCancelable(true);
            mSheetDialog.getWindow().setDimAmount(0); //背景透明
            mSheetDialog.show();
        } else {
            mSheetDialog.dismiss();
        }
    }

    private void initViewSheet(View inflate) {
        SegmentedGroup segmentedGroup = inflate.findViewById(R.id.segmented_group);
        mListView = inflate.findViewById(R.id.lv_near);
        mSearchResultAdapter = new SearchResultAdapter(this);
        mListView.setAdapter(mSearchResultAdapter);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mCurrentType = items[0];
                switch (checkedId) {
                    case R.id.radio0:
                        mCurrentType = items[0];
                        break;
                    case R.id.radio1:
                        mCurrentType = items[1];
                        break;
                    case R.id.radio2:
                        mCurrentType = items[2];
                        break;
                    case R.id.radio3:
                        mCurrentType = items[3];
                        break;
                }
                searchNear(mCurrentPoiItem.getLatLonPoint());
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismissSheetNearDialog();
                if (mPoisList != null) {
                    for (int i = 0; i < mPoisList.size(); i++) {
                        LatLonPoint latLonPoint = mPoisList.get(i).getLatLonPoint();
                        mAMapInit.addMarkersToMap(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()), mPoisList.get(i), new AMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                ToastUtils.showToast(marker.getTitle());
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapSearchActivity.request && resultCode == MapSearchActivity.result) {
            Bundle bundle = data.getBundleExtra("bundle");
            Tip tip = bundle.getParcelable("tip");
            if(tip.getPoint()!=null){
                LatLng latLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(18).bearing(0).tilt(30).build();
                AMapOptions aOptions = new AMapOptions();
                aOptions.camera(cameraPosition);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mAMapInit.addMarkerInScreen(latLng);
                showSearchSheetDialog(tip);
            }else {
                ToastUtils.showToast("搜索出错");
            }
        }
    }

    private void showSearchSheetDialog(Tip tip) {
        dismissWeaDialog();
        dismissSheetNearDialog();
        if (mSheetSearchDialog == null || !mSheetSearchDialog.isShowing()) {
            mSheetSearchDialog = new BottomSheetDialog(this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.sheet_search_layout, null, false);
            initViewSearchSheet(inflate, tip);
            mSheetSearchDialog.setContentView(inflate);
            mSheetSearchDialog.setCanceledOnTouchOutside(true);
            mSheetSearchDialog.setCancelable(true);
            mSheetSearchDialog.getWindow().setDimAmount(0); //背景透明
            mSheetSearchDialog.show();
        } else {
            mSheetSearchDialog.dismiss();
        }
    }

    private void initViewSearchSheet(View inflate, Tip tip) {
        TextView tvDistance = inflate.findViewById(R.id.tv_distance);
        TextView tvDetailDistance = inflate.findViewById(R.id.tv_detail_place);
        TextView tvPlace = inflate.findViewById(R.id.tv_place);
        RelativeLayout rlWeal = inflate.findViewById(R.id.rl_wea);
        RelativeLayout rlNear = inflate.findViewById(R.id.rl_near);
        RelativeLayout rlLine = inflate.findViewById(R.id.rl_line);
        tvPlace.setText(tip.getName());
        tvDetailDistance.setText(tip.getDistrict() + "-" + tip.getAddress());
        LatLng latLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
        if (mLatLng != null) {
            float distance = AMapUtils.calculateLineDistance(latLng, mLatLng);
            tvDistance.setText(distance <= 2000 ? "距离：" + String.format("%.2f", distance) + " 米" : "距离：" + String.format("%.3f", distance / 1000) + " 千米");
        }
        rlWeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetSearchDialog.dismiss();
                mMapWeaDialog = MapWeaDialog.getInstance(tip, mLatLng);
                mMapWeaDialog.show(getSupportFragmentManager(), "");
            }
        });
        rlNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetSearchDialog.dismiss();
                showNearDialog();
                searchNear(new LatLonPoint(latLng.longitude, latLng.latitude));
            }
        });
        rlLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetSearchDialog.dismiss();
                AMapInit.skipTpMapNav(AMapActivity.this,latLng,tip.getName());
            }
        });
    }


    //点击
    @Override
    public void onPOIClick(Poi poi) {
        mMap.clear();
        mAMapInit.addGrowMarker(poi.getCoordinate());
        mAMapInit.changePoiItem(poi, new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                mCurrentPoiItem = poiItem;
                if (!isWeather) {
                    mCurrentType = items[0];
                    showNearDialog();
                    searchNear(poiItem.getLatLonPoint());
                } else {
                    mMapWeaDialog = MapWeaDialog.getInstance(poiItem, mLatLng);
                    mMapWeaDialog.show(getSupportFragmentManager(), "");
                }
            }
        });
    }

    // 搜索建筑
    private void searchNear(LatLonPoint latLonPoint) {
        mAMapInit.searchQuery(latLonPoint, currentPage, mCurrentType, new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                mPoisList.clear();
                mPoisList.addAll(poiResult.getPois());
                if (mSearchResultAdapter != null) {
                    mSearchResultAdapter.setData(mPoisList);
                    mSearchResultAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
    }

    private void dismissWeaDialog() {
        if (mMapWeaDialog != null && !mMapWeaDialog.isHidden()) {
            mMapWeaDialog.dismiss();
        }
    }

    private void dismissSheetNearDialog() {
        if (mSheetDialog != null && mSheetDialog.isShowing()) {
            mSheetDialog.dismiss();
        }
    }

    private void dismissSheetSearchDialog() {
        if (mSheetSearchDialog != null && mSheetSearchDialog.isShowing()) {
            mSheetSearchDialog.dismiss();
        }
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
    private void initSetting() {
        mUiSettings=mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);//是否显示缩放
        mMap.setLocationSource(this);//通过aMap对象设置定位数据源的监听
        mMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        mUiSettings.setScaleControlsEnabled(true); //地图比例
        mUiSettings.setCompassEnabled(false); //指南针
        mUiSettings.setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置
    }

    private void initLocat() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mMap.showIndoorMap(true);
        mMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//                LogUtil.i("=========",followMove+"");
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (followMove) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng));
                }
//                LogUtil.i("=========",location.toString());
            }
        });

        mMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                followMove=false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
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
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
