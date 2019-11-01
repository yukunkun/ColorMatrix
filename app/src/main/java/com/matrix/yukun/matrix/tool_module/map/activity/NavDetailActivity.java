package com.matrix.yukun.matrix.tool_module.map.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.map.maputil.TTSController;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NavDetailActivity extends BaseActivity implements AMapNaviViewListener, AMapNaviListener {

    @BindView(R.id.navi_view)
    AMapNaviView mAMapNaviView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private AMapNavi mAMapNavi;
    private TTSController mTtsManager;
    private boolean mIsGps;

    public static void start(Context context,NaviLatLng startLatitude,NaviLatLng endLatitude) {
        Intent intent = new Intent(context, NavDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("gps", false);
        bundle.putParcelable("start", startLatitude);
        bundle.putParcelable("end", endLatitude);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAMapNaviView.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_nav_detail;
    }

    @Override
    public void initView() {
//        mTtsManager = TTSController.getInstance(getApplicationContext());
//        mTtsManager.init();
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
//        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setEmulatorNaviSpeed(60);
        getNaviParam();
    }

    private void getNaviParam() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        Bundle bundle = intent.getBundleExtra("bundle");
        mIsGps = bundle.getBoolean("gps", false);
        NaviLatLng start = bundle.getParcelable("start");
        NaviLatLng end = bundle.getParcelable("end");
        calculateDriveRoute(start, end);
    }

    private void calculateDriveRoute(NaviLatLng start, NaviLatLng end) {
        int strategyFlag = 0;
        List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
        /**
         * 途径点坐标集合
         */
        List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
        /**
         * 终点坐标集合［建议就一个终点］
         */
        List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
        try {
            strategyFlag = mAMapNavi.strategyConvert(true, false, false, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startList.add(start);
        endList.add(end);
        mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);


    }

    @Override
    public void initDate() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        LogUtil.i("=======",""+i);
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        if (mIsGps) {
            mAMapNavi.startNavi(NaviType.GPS);
        } else {
            mAMapNavi.startNavi(NaviType.EMULATOR);
        }
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
        LogUtil.i("=======",aMapCalcRouteResult.getErrorDetail());
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }
}
