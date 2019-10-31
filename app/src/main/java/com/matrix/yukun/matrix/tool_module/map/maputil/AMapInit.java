package com.matrix.yukun.matrix.tool_module.map.maputil;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.RouteSearch;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;

/**
 * author: kun .
 * date:   On 2019/10/18
 */
public class AMapInit{

    static AMapInit mapInit=new AMapInit();
    private AMap mMap;
    private Context mContext;
    private Marker growMarker = null;
    private Marker screenMarker = null;
    private PoiSearch.Query mQuery;
    private PoiSearch poiSearch;
    private MarkerOptions markerOption;
    private Marker markerPoint;
    private RouteSearch mRouteSearch;
    public static final int ROUTE_TYPE_WALK = 3;
    public static final int ROUTE_TYPE_DRIVE = 2;
    public static final int ROUTE_TYPE_BUS = 1;

    private AMapInit(){

    }

    public static AMapInit instance(){
        if(mapInit==null){
            mapInit=new AMapInit();
        }
        return mapInit;
    }

    public void init(Context context,AMap aMap){
        this.mContext=context;
        this.mMap=aMap;
    }

    public void changeMapCenter(LatLng latLng){
        if(latLng!=null){
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public void searchQuery(LatLonPoint searchLatlonPoint, int currentPage, String searchType, PoiSearch.OnPoiSearchListener searchListener){
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery = new PoiSearch.Query("", searchType, "");
        mQuery.setCityLimit(true);
        mQuery.setPageSize(20);
        mQuery.setPageNum(currentPage);
        if (searchLatlonPoint != null) {
            poiSearch = new PoiSearch(mContext, mQuery);
            poiSearch.setOnPoiSearchListener(searchListener);
            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000, true));//
            poiSearch.searchPOIAsyn();
        }
    }

    public void changePoiItem(Poi poi,PoiSearch.OnPoiSearchListener onPoiSearchListener){
        PoiSearch poiSearch = new PoiSearch(mContext, null);
        poiSearch.setOnPoiSearchListener(onPoiSearchListener);
        poiSearch.searchPOIIdAsyn(poi.getPoiId());// 异步搜索
    }

    public void driveRount(int routeType, RouteSearch.FromAndTo fromAndTo,int drivingMode,RouteSearch.OnRouteSearchListener routeSearchListener){
        mRouteSearch = new RouteSearch(mContext);
        mRouteSearch.setRouteSearchListener(routeSearchListener);
        switch (routeType){
            case ROUTE_TYPE_WALK:
                RouteSearch.WalkRouteQuery walkQuery = new RouteSearch.WalkRouteQuery(fromAndTo);
                mRouteSearch.calculateWalkRouteAsyn(walkQuery);
                break;
            case ROUTE_TYPE_DRIVE:
                RouteSearch.DriveRouteQuery driveQuery = new RouteSearch.DriveRouteQuery(fromAndTo, drivingMode, null, null, "");
                mRouteSearch.calculateDriveRouteAsyn(driveQuery);
                break;
            case ROUTE_TYPE_BUS:
                RouteSearch.BusRouteQuery busQuery = new RouteSearch.BusRouteQuery(fromAndTo, drivingMode, SPUtils.getInstance().getString("city"),drivingMode);
                mRouteSearch.calculateBusRouteAsyn(busQuery);
                break;

        }

    }

    public void setfromandtoMarker(LatLonPoint mStartLatLonPoint,LatLonPoint  mEndLatLonPoint) {
        mMap.addMarker(new MarkerOptions()
                .position(convertToLatLng(mStartLatLonPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_start)));
        mMap.addMarker(new MarkerOptions()
                .position(convertToLatLng(mEndLatLonPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_end)));
    }

    /**
     * 添加一个从地上生长的Marker
     */
    public void  addGrowMarker(LatLng latLng) {
        growMarker = null;
        if(growMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .position(latLng);
            growMarker = mMap.addMarker(markerOptions);
        }
        startGrowAnimation();
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    public void addMarkerInScreen(LatLng latLng) {
        screenMarker=null;
        if(screenMarker==null){
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.purple_pin_local))
                    .position(latLng);
            screenMarker = mMap.addMarker(markerOptions);
        }
        startJumpAnimation();
    }
    /**
     * 在地图上添加marker
     */
    public void addMarkersToMap(LatLng latLng, PoiItem poiItem, AMap.OnInfoWindowClickListener onInfoWindowClickListener) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latLng)
                .title(poiItem.getTitle())
                .snippet(poiItem.getAdName() + poiItem.getSnippet())
                .draggable(true);
        markerPoint = mMap.addMarker(markerOption);
        markerPoint.showInfoWindow();
        mMap.setOnInfoWindowClickListener(onInfoWindowClickListener);

    }

    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation() {
        if(growMarker != null) {
            Animation animation = new ScaleAnimation(0,1,0,1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(500);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }
    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {
        if (screenMarker != null ) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            Point point =  mMap.getProjection().toScreenLocation(latLng);
//            point.y -= ScreenUtil.dip2px(125);
            LatLng target = mMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if(input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e("amap","screenMarker is null");
        }
    }
    public LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }
}
