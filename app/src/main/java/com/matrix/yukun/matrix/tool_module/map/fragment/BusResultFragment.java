package com.matrix.yukun.matrix.tool_module.map.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.map.adapter.BusResultListAdapter;
import com.matrix.yukun.matrix.tool_module.map.maputil.AMapInit;

import butterknife.BindView;

/**
 * author: kun .
 * date:   On 2019/10/31
 */
public class BusResultFragment extends BaseFragment {

    @BindView(R.id.lv_bus)
    ListView lvBus;
    private AMapInit mAMapInit;
    private BusRouteResult mBusRouteResult;
    private static Context mContext;
    public static BusResultFragment getInstance(Context context) {
        mContext=context;
        BusResultFragment busResultFragment = new BusResultFragment();
        return busResultFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_bus_result;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mAMapInit = AMapInit.instance();
        mAMapInit.init(getContext(),null);
    }

    public void setData(boolean isShow, LatLonPoint mStartLatLonPoint, LatLonPoint mEndLatLonPoint) {
        if(isShow){
            if(mAMapInit==null){
                mAMapInit = AMapInit.instance();
                mAMapInit.init(mContext,null);
            }
            startNav(AMapInit.ROUTE_TYPE_BUS,mStartLatLonPoint,mEndLatLonPoint);
        }
    }

    private void startNav(int type, LatLonPoint mStartLatLonPoint, LatLonPoint mEndLatLonPoint) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartLatLonPoint, mEndLatLonPoint);
        mAMapInit.driveRount(type, fromAndTo, 0, new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult result, int errorCode) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getPaths() != null) {
                        if (result.getPaths().size() > 0) {
                            mBusRouteResult = result;
                            BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(getContext(), mBusRouteResult);
                            lvBus.setAdapter(mBusResultListAdapter);
                        } else if (result != null && result.getPaths() == null) {

                        }
                    } else {
                    }
                }
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

    }
}
