package com.matrix.yukun.matrix.tool_module.weather.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.Cn2Spell;
import com.matrix.yukun.matrix.main_module.utils.ISideBarSelectCallBack;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.main_module.views.SideBar;
import com.matrix.yukun.matrix.main_module.views.TagLayout;
import com.matrix.yukun.matrix.tool_module.videorecord.ViewUtils;
import com.matrix.yukun.matrix.tool_module.weather.adapter.LVCityAdapter;
import com.matrix.yukun.matrix.tool_module.weather.adapter.LVCitySearchAdapter;
import com.matrix.yukun.matrix.util.AssetsUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchCityActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_local)
    TextView tvLocal;
    @BindView(R.id.iv_loc)
    ImageView ivLoc;
    @BindView(R.id.lv_date_list)
    ListView lvDateList;
    @BindView(R.id.bar)
    SideBar bar;
    @BindView(R.id.lv_list)
    ListView lvList;
    @BindView(R.id.rl_list)
    RelativeLayout rlList;
    @BindView(R.id.rl_local)
    RelativeLayout rlLocal;
    @BindView(R.id.et_city)
    EditText etCity;
    private AMapLocationClient mLocationClient;
    private String mCity;
    private Gson gson = new Gson();
    private List<CityBean> mCityList;
    private List<CitySortBean> mSortCityModule;
    public static int RESULT = 1001;
    private List<CityBean.CitiesBean.CountiesBean> mCities = new ArrayList<>();
    private TagLayout mTagLayout;
    private List<String> mHotCountry=new ArrayList<>();
    public static void start(Context context) {
        Intent intent = new Intent(context, SearchCityActivity.class);
        ((Activity) context).startActivityForResult(intent, RESULT);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_search_city;
    }

    @Override
    public void initView() {
        mHotCountry = Arrays.asList(getResources().getStringArray(R.array.city_hot));

    }

    @Override
    public void initDate() {
        String string = SPUtils.getInstance().getString("city");
        if (!TextUtils.isEmpty(string)) {
            mCity = string;
            tvLocal.setText("当前城市：" + mCity);
            etCity.setText(mCity);
        } else {
            updatePosition();
        }
        String json = AssetsUtils.readText(this, "city.json");
        mCityList = gson.fromJson(json, new TypeToken<List<CityBean>>() {}.getType());
        mSortCityModule = getSortModule();
        LVCityAdapter lvCityAdapter = new LVCityAdapter(mSortCityModule, this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.city_header, null);
        mTagLayout = inflate.findViewById(R.id.tag_layout);
        lvDateList.addHeaderView(inflate);
        lvDateList.setAdapter(lvCityAdapter);
        initTagLayout();
    }

    private void initTagLayout() {
        for (int i = 0; i < mHotCountry.size(); i++) {
            TextView textView= ViewUtils.getHotCityTextView(this);
            textView.setText(mHotCountry.get(i));
            mTagLayout.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBack(textView.getText().toString());
                }
            });
        }
    }

    private List<CitySortBean> getSortModule() {
        List<CitySortBean> filterDateList = new ArrayList<>();
        for (int i = 0; i < mCityList.size(); i++) {
            String pinYinFirstLetter = Cn2Spell.getPinYinFirstLetter(mCityList.get(i).getAreaName());
            CitySortBean citySortBean = new CitySortBean(pinYinFirstLetter.toUpperCase().charAt(0) + "", mCityList.get(i));
            filterDateList.add(citySortBean);
        }
        Collections.sort(filterDateList, new PinyinCityComparator());
        return filterDateList;
    }

    @Override
    public void initListener() {
        lvDateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityBean cityBean = mSortCityModule.get(position).getCityBean();
                etCity.setText(cityBean.getAreaName());
                if (cityBean.getCities() != null && cityBean.getCities().size() > 0) {
                    mCities.clear();
                    CityBean.CitiesBean.CountiesBean countiesBean=new CityBean.CitiesBean.CountiesBean();
                    countiesBean.setAreaName(cityBean.getAreaName());
                    mCities.add(countiesBean);
                    mCities.addAll(cityBean.getCities().get(0).getCounties());
                    rlList.setVisibility(View.VISIBLE);
                    LVCitySearchAdapter lvCitySearchAdapter = new LVCitySearchAdapter(mCities, SearchCityActivity.this);
                    lvList.setAdapter(lvCitySearchAdapter);
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goBack(mCities.get(position).getAreaName());
            }
        });

        bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                if (mSortCityModule == null) {
                    return;
                }
                for (int i = 0; i < mSortCityModule.size(); i++) {
                    if (mSortCityModule.get(i).getSortLetters().equals(selectStr)) {
                        lvDateList.setSelection(i+1);
                        return;
                    }
                }
            }

            @Override
            public void onSelectEnd() {

            }

            @Override
            public void onSelectStart() {

            }
        });
    }

    private void goBack(String city) {
        Intent intent = new Intent();
        intent.putExtra("result", city);
        setResult(RESULT, intent);
        finish();
    }

    private void updatePosition() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        SPUtils.getInstance().saveString("city", aMapLocation.getCity());
                        mCity = aMapLocation.getCity();
                        tvLocal.setText("当前城市：" + mCity);
                        etCity.setText(mCity);
                    } else {
                        ToastUtils.showToast("请确保定位已开启");
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    @OnClick({R.id.iv_back, R.id.iv_search, R.id.iv_loc, R.id.rl_local,R.id.rl_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                if(!TextUtils.isEmpty(etCity.getText().toString())){
                    goBack(etCity.getText().toString());
                }else {
                    ToastUtils.showToast("请输入城市");
                }
                break;
            case R.id.iv_loc:
                updatePosition();
                ToastUtils.showToast("定位中。。。");
                break;
            case R.id.rl_local:
                if(!TextUtils.isEmpty(etCity.getText().toString())){
                    goBack(etCity.getText().toString());
                }else {
                    ToastUtils.showToast("请输入城市");
                }
                break;
            case R.id.rl_list:
                rlList.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onBackPressed() {
            if (rlList.getVisibility() == View.VISIBLE) {
                rlList.setVisibility(View.GONE);
                return;
            } else {
                finish();
            }
        }
    }
