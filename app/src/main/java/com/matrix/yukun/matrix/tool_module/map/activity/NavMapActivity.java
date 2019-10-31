package com.matrix.yukun.matrix.tool_module.map.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.map.adapter.NavMapAdapter;
import com.matrix.yukun.matrix.tool_module.map.fragment.BusResultFragment;
import com.matrix.yukun.matrix.tool_module.map.fragment.DriveResultFragment;
import com.matrix.yukun.matrix.tool_module.map.fragment.WalkResultFragment;
import com.matrix.yukun.matrix.tool_module.map.maputil.AMapInit;
import com.matrix.yukun.matrix.tool_module.map.maputil.AMapUtil;
import com.matrix.yukun.matrix.tool_module.map.overlay.DrivingRouteOverlay;
import com.matrix.yukun.matrix.tool_module.map.overlay.WalkRouteOverlay;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavMapActivity extends BaseActivity implements Inputtips.InputtipsListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_enter)
    LinearLayout llEnter;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.fl_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.in_start)
    ImageView inStart;
    @BindView(R.id.et_start)
    EditText etStart;
    @BindView(R.id.iv_start_del)
    ImageView ivStartDel;
    @BindView(R.id.iv_end)
    ImageView ivEnd;
    @BindView(R.id.et_end)
    EditText etEnd;
    @BindView(R.id.iv_end_del)
    ImageView ivEndDel;
    @BindView(R.id.lv_search)
    ListView lvSearch;
    private LatLng mCurrentLatLng;
    private List<String> mMapNavs;
    private String mCityCode;
    private boolean isStart;
    private boolean startSelect = true;
    private boolean endSelect = true;
    private NavMapAdapter mNavMapAdapter;
    private List<Tip> mTips = new ArrayList<>();
    private LatLonPoint mStartLatLonPoint;
    private LatLonPoint mEndLatLonPoint;
    private int[] mIconUnselectIds = {
            R.mipmap.tool_icon, R.mipmap.tool_icon,
            R.mipmap.tool_icon, R.mipmap.tool_icon};
    /*选择时的icon*/
    private int[] mIconSelectIds = {
            R.mipmap.tool_icon, R.mipmap.tool_icon,
            R.mipmap.tool_icon, R.mipmap.tool_icon};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private WalkResultFragment mWalkResultFragment;
    private BusResultFragment mBusResultFragment;
    private int lastPos;
    private DriveResultFragment mDriveResultFragment;
    private int currentSelect;
    public static void start(Context context) {
        Intent intent = new Intent(context, NavMapActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_nav_map;
    }

    @Override
    public void initView() {
        mNavMapAdapter = new NavMapAdapter(this);
        lvSearch.setAdapter(mNavMapAdapter);
        mWalkResultFragment = WalkResultFragment.getInstance();
        mDriveResultFragment = DriveResultFragment.getInstance();
        mBusResultFragment = BusResultFragment.getInstance();
        mFragmentList.add(mWalkResultFragment);
        mFragmentList.add(mDriveResultFragment);
        mFragmentList.add(mBusResultFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_layout, mFragmentList.get(0)).commit();
    }

    @Override
    public void initDate() {
        mMapNavs = (Arrays.asList(getResources().getStringArray(R.array.map_nav)));
        mCurrentLatLng = MyApp.getLatLng();
        mCityCode = SPUtils.getInstance().getString("CityCode");
        initViewData();
    }

    private void initViewData() {
        for (int i = 0; i < mMapNavs.size(); i++) {
            mTabEntities.add(new TabEntity(mMapNavs.get(i), mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
    }

    @Override
    public void initListener() {
        tabLayout.setCurrentTab(0);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentSelect=position;
                showFragment(position,position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        etStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && startSelect) {
                    isStart = true;
                    searchResule(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && endSelect) {
                    isStart = false;
                    searchResule(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvSearch.setVisibility(View.GONE);
                if(isStart){
                    startSelect=false;
                    etStart.setEnabled(false);
                    etStart.setText(mTips.get(position).getName());
                    mStartLatLonPoint = mTips.get(position).getPoint();
                }else {
                    endSelect=false;
                    etEnd.setEnabled(false);
                    etEnd.setText(mTips.get(position).getName());
                    mEndLatLonPoint = mTips.get(position).getPoint();
                }
                if(!startSelect&&!endSelect){
                    ivSearch.setImageResource(R.mipmap.icon_search_skip_checked);

                }
            }
        });
    }

    private void showFragment(int position,int select) {
        position=(position==1)?0:position;
        Fragment fragment = mFragmentList.get(position);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(mFragmentList.get(lastPos));
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
            if(fragment instanceof WalkResultFragment){
                ((WalkResultFragment) fragment).setType(!startSelect && !endSelect,select, mStartLatLonPoint, mEndLatLonPoint);
            }else {
                ((BusResultFragment) fragment).setData(true,mStartLatLonPoint,mEndLatLonPoint);
            }
        } else {
            fragmentTransaction.add(R.id.fl_layout, fragment);
        }
        fragmentTransaction.commit();
        lastPos = position;
    }

    private void searchResule(String text) {
        InputtipsQuery inputquery = new InputtipsQuery(text, mCityCode);
        inputquery.setCityLimit(true);//限制在当前城市
        Inputtips inputTips = new Inputtips(this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @OnClick({R.id.iv_back, R.id.iv_search, R.id.iv_start_del, R.id.iv_end_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                if (!startSelect && !endSelect) {
                        if(currentSelect==0||currentSelect==1){
                            mWalkResultFragment.setType(true,currentSelect,mStartLatLonPoint,mEndLatLonPoint);
                        }else {
                            mBusResultFragment.setData(true,mStartLatLonPoint,mEndLatLonPoint);
                        }
//                    startNav(mAMapInit.ROUTE_TYPE_BUS, mStartLatLonPoint, mEndLatLonPoint);
                } else {
                    ToastUtils.showToast(getResources().getString(R.string.chose_local));
                }
                break;
            case R.id.iv_start_del:
                startSelect = true;
                etStart.setEnabled(true);
                etStart.setText("");
                ivSearch.setImageResource(R.mipmap.icon_search_skip_unchecked);
                break;
            case R.id.iv_end_del:
                endSelect = true;
                etEnd.setEnabled(true);
                etEnd.setText("");
                ivSearch.setImageResource(R.mipmap.icon_search_skip_unchecked);
                break;
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (list.size() > 0) {
            lvSearch.setVisibility(View.VISIBLE);
            mTips.clear();
            mTips.addAll(list);
            mNavMapAdapter.setData(mTips);
        }else {
            ToastUtils.showToast("搜索失败");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class TabEntity implements CustomTabEntity {
        public String title;
        public int selectedIcon;
        public int unSelectedIcon;

        public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }
}
