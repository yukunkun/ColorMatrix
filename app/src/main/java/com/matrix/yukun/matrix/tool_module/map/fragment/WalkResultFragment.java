package com.matrix.yukun.matrix.tool_module.map.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.map.adapter.BusResultListAdapter;
import com.matrix.yukun.matrix.tool_module.map.maputil.AMapInit;
import com.matrix.yukun.matrix.tool_module.map.maputil.AMapUtil;
import com.matrix.yukun.matrix.tool_module.map.overlay.BusRouteOverlay;
import com.matrix.yukun.matrix.tool_module.map.overlay.DrivingRouteOverlay;
import com.matrix.yukun.matrix.tool_module.map.overlay.WalkRouteOverlay;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: kun .
 * date:   On 2019/10/31
 */
public class WalkResultFragment extends BaseFragment implements LocationSource, AMapLocationListener, AMap.OnMapLoadedListener {

    private AMap mAMap;
    private DriveRouteResult mDriveRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private BusRouteResult mBusRouteResult;
    private AMapInit mAMapInit;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private boolean followMove = true;
    private LatLng mCurrentLatLng;
    private String mCityCode;
    private boolean isShow;
    private int type;
    private int navType;
    private BottomSheetDialog mSheetDialog;
    private TextView mTvTime;
    private TextView mTvTaxi;
    private RelativeLayout mRlDetail;
    private RelativeLayout mRlNav;
    private ListView mLvBus;
    private TextView mTvNormal;
    private BusResultListAdapter mBusResultListAdapter;
    private BusRouteOverlay mBusrouteOverlay;
    private  String startPlace;
    private  String endPlace;;
    LatLonPoint startLatLonPoint;
    LatLonPoint endLatLonPoint;
    public static WalkResultFragment getInstance() {
        WalkResultFragment workDriveResultFragment = new WalkResultFragment();
        Bundle bundle = new Bundle();
        workDriveResultFragment.setArguments(bundle);
        return workDriveResultFragment;
    }

    public void setType(boolean isShow,int type, LatLonPoint startLatLonPoint, LatLonPoint endLatLonPoint,String start,String end) {
        this.isShow=isShow;
        this.type=type;
        this.startPlace=start;
        this.endPlace=end;
        this.startLatLonPoint=startLatLonPoint;
        this.endLatLonPoint=endLatLonPoint;
        navType=devideNavType(type);
        if(isShow){
            startNav(navType,startLatLonPoint,endLatLonPoint);
        }
    }

    private int devideNavType(int type) {
        switch (type){
            case 0:
                return AMapInit.ROUTE_TYPE_WALK;
            case 1:
                return AMapInit.ROUTE_TYPE_DRIVE;
            case 2:
                return AMapInit.ROUTE_TYPE_BUS;
        }
        return 0;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_work_drive_result;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        setUpMapIfNeeded();
        mAMapInit = AMapInit.instance();
        mAMapInit.init(getContext(), mAMap);
        mCityCode = SPUtils.getInstance().getString("CityCode");
        mCurrentLatLng = MyApp.getLatLng();
        mAMapInit.changeMapCenter(mCurrentLatLng);
        initViewData();
        mAMap.setOnMapLoadedListener(this);
    }

    private void initViewData() {
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
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (followMove) {
                    mAMap.animateCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng));
                }
            }
        });

        mAMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                followMove = false;
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

    private void startNav(int type, LatLonPoint mStartLatLonPoint, LatLonPoint mEndLatLonPoint) {
//        mAMapInit.setfromandtoMarker(mStartLatLonPoint, mEndLatLonPoint);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartLatLonPoint, mEndLatLonPoint);
        mAMapInit.driveRount(type, fromAndTo, 0, new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult result, int errorCode) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getPaths() != null) {
                        if (result.getPaths().size() > 0) {
                            mBusRouteResult = result;
                            mBusResultListAdapter = new BusResultListAdapter(getContext(), mBusRouteResult);
                            BusPath busPath = mBusRouteResult.getPaths().get(0);
                            mSheetDialog.dismiss();
                            showSheetDialog();
                            mLvBus.setAdapter(mBusResultListAdapter);
                            updateSheetView(busPath);
                        } else if (result != null && result.getPaths() == null) {

                        }
                    } else {
                    }
                }
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
                mAMap.clear();// 清理地图上的所有覆盖物
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getPaths() != null) {
                        if (result.getPaths().size() > 0) {
                            mDriveRouteResult = result;
                            final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                            if (drivePath == null) {
                                return;
                            }
                            mSheetDialog.dismiss();
                            showSheetDialog();
                            updateSheetView(drivePath);

                        } else if (result != null && result.getPaths() == null) {
                            ToastUtils.showToast("error");
                        }

                    } else {
                        ToastUtils.showToast("error");
                    }
                } else {
                    ToastUtils.showToast(errorCode + "");
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
                mAMap.clear();// 清理地图上的所有覆盖物
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getPaths() != null) {
                        if (result.getPaths().size() > 0) {
                            mWalkRouteResult = result;
                            final WalkPath walkPath = mWalkRouteResult.getPaths()
                                    .get(0);
                            if (walkPath == null) {
                                return;
                            }
                            if(mSheetDialog!=null){
                                mSheetDialog.dismiss();
                            }
                            showSheetDialog();
                            updateSheetView(walkPath);

                        } else if (result != null && result.getPaths() == null) {
                            ToastUtils.showToast("error");
                        }

                    } else {
                        ToastUtils.showToast("error");
                    }
                } else {
                    ToastUtils.showToast(errorCode + "");
                }
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
    }

    private void updateSheetView(DrivePath drivePath) {
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                getContext(), mAMap, drivePath,
                mDriveRouteResult.getStartPos(),
                mDriveRouteResult.getTargetPos(), null);
        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
        drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();

        String dur = AMapUtil.getFriendlyTime((int) drivePath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) drivePath.getDistance());
        mTvTime.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mDriveRouteResult.getTaxiCost();
        mTvTaxi.setText("打车约"+taxiCost+"元");
    }

    private void updateSheetView(WalkPath walkPath) {
        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                getContext(), mAMap, walkPath,
                mWalkRouteResult.getStartPos(),
                mWalkRouteResult.getTargetPos());

        walkRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
        walkRouteOverlay.removeFromMap();
        walkRouteOverlay.addToMap();
        walkRouteOverlay.zoomToSpan();

        String dur = AMapUtil.getFriendlyTime((int) walkPath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) walkPath.getDistance());
        mTvTime.setText(dur + "(" + dis + ")");
//        int taxiCost = (int) mWalkRouteResult.getWalkQuery().;
//        mTvTaxi.setText("打车约"+taxiCost+"元");
    }

    private void updateSheetView(BusPath busPath) {
        mAMap.clear();
        mBusrouteOverlay = new BusRouteOverlay(getContext(), mAMap,
                busPath, mBusRouteResult.getStartPos(),
                mBusRouteResult.getTargetPos());
        mBusrouteOverlay.removeFromMap();
        mBusrouteOverlay.addToMap();
        mBusrouteOverlay.zoomToSpan();

        String dur = AMapUtil.getFriendlyTime((int) busPath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) busPath.getDistance());
        mTvTime.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mBusRouteResult.getTaxiCost();
        mTvTaxi.setText("打车约"+taxiCost+"元");
    }

    private void showSheetDialog() {
        if (mSheetDialog == null || !mSheetDialog.isShowing()) {
            mSheetDialog = new BottomSheetDialog(getContext());
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.sheet_search_result_layout, null, false);
            initViewSheet(inflate);
            initSheetListener();
//            mLvBus.setVisibility(View.GONE);
            mSheetDialog.setContentView(inflate);
            mSheetDialog.setCanceledOnTouchOutside(true);
            mSheetDialog.setCancelable(true);
            mSheetDialog.getWindow().setDimAmount(0); //背景透明
            mSheetDialog.show();
        } else {
            mSheetDialog.dismiss();
        }
    }

    private void initSheetListener() {
        mRlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLvBus.getVisibility() ==View.VISIBLE){
                    mLvBus.setVisibility(View.GONE);
                }else {
                    mLvBus.setVisibility(View.VISIBLE);
                }
            }
        });

        mRlNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMapInit.skipToMap(getContext(),new LatLng(startLatLonPoint.getLatitude(),startLatLonPoint.getLongitude()),
                        new LatLng(endLatLonPoint.getLatitude(),endLatLonPoint.getLongitude()),startPlace,endPlace);
            }
        });

        mLvBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showToast("pos:"+position);
                BusPath item = (BusPath) mBusResultListAdapter.getItem(position);
                updateSheetView(item);
            }
        });
    }

    private void initViewSheet(View inflate) {
        mTvTime = inflate.findViewById(R.id.tv_time);
        mTvTaxi = inflate.findViewById(R.id.tv_taxi);
        mRlDetail = inflate.findViewById(R.id.rl_detail);
        mRlNav = inflate.findViewById(R.id.rl_nav);
        mLvBus = inflate.findViewById(R.id.lv_bus);
        mTvNormal = inflate.findViewById(R.id.tv_normal);
        mRlDetail.setVisibility(type==2?View.VISIBLE:View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mAMap == null) {
            mAMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(getContext());
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
    public void onMapLoaded() {

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
}
