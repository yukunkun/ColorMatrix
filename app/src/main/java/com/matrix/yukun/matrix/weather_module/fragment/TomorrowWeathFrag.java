package com.matrix.yukun.matrix.weather_module.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.BaseFrag;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.weather_module.animutils.AnimUtils;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;
import com.matrix.yukun.matrix.weather_module.present.TomorrowFragmentImpl;
import com.matrix.yukun.matrix.weather_module.present.TomorrowPresent;
import com.mcxtzhang.pathanimlib.StoreHouseAnimView;
import com.mcxtzhang.pathanimlib.res.StoreHousePath;
import com.mcxtzhang.pathanimlib.utils.PathParserUtils;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by yukun on 17-3-6.
 */
public class TomorrowWeathFrag extends BaseFrag implements TomorrowFragmentImpl {

    @BindView(R.id.tomorrow_back)
    LinearLayout tomorrowBack;
    @BindView(R.id.today_city)
    TextView todayCity;
    @BindView(R.id.today_refresh)
    ImageView todayRefresh;
    @BindView(R.id.today_refreshs)
    TextView todayRefreshs;
    @BindView(R.id.today_time)
    TextView todayTime;
    @BindView(R.id.today_class)
    TextView todayClass;
    @BindView(R.id.today_image)
    ImageView todayImage;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.today_wendu)
    TextView todayWendu;
    @BindView(R.id.tomorrow_ri)
    TextView tomorrowRi;
    @BindView(R.id.tomorrow_yue)
    TextView tomorrowYue;
    @BindView(R.id.tomorrow_bai)
    TextView tomorrowBai;
    @BindView(R.id.tomorrow_hei)
    TextView tomorrowHei;
    @BindView(R.id.today_power_1)
    TextView todayPower1;
    @BindView(R.id.today_power_2)
    TextView todayPower2;
    @BindView(R.id.today_power_3)
    TextView todayPower3;
    @BindView(R.id.scrollview)
    ScrollView scrollView;
    @BindView(R.id.tomorrow_title)
    RelativeLayout tomorrowTitle;
    @BindView(R.id.recyclerViews)
    RecyclerView recyclerViews;
    @BindView(R.id.power_lin)
    LinearLayout powerLin;
    @BindView(R.id.banner)
    RelativeLayout mBanner;
    private TomorrowPresent tomorrowPresent;
    private String city;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerTomorrowAdapter recyclerTomorrowAdapter;
    private BannerView mBannerView;
    private StoreHouseAnimView mAnimView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("刷新中...");
        progressDialog.show();
        city = getArguments().getString("city");
        tomorrowPresent = new TomorrowPresent(this, city);
        this.basePresent = tomorrowPresent;
        super.onCreate(savedInstanceState);
    }

    public static TomorrowWeathFrag newInstance(String arg) {
        TomorrowWeathFrag fragment = new TomorrowWeathFrag();
        Bundle bundle = new Bundle();
        bundle.putString("city", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wea_tomorrow, null);
        ButterKnife.bind(this, inflate);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        EventBus.getDefault().register(this);
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        //动画
        mAnimView = (StoreHouseAnimView) inflate.findViewById(R.id.pathAnimView1);
        mAnimView.setColorBg(Color.GRAY).setColorFg(Color.WHITE);
        mAnimView.setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("tomorrow",0.4f,5)));
        mAnimView.setPathMaxLength(150).setAnimTime(2000).startAnim();
        setListener();
        getBanner();
        return inflate;
    }

    private void getBanner() {
        mBannerView = new BannerView(getActivity(), ADSize.BANNER, AppConstants.APPID,
                AppConstants.BANNER_ADID);
        mBannerView.setRefresh(30);
        mBannerView.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {
                Log.i("---onNoAD",i+"");
            }

            @Override
            public void onADReceiv() {
                Log.i("---onNoAD","onNoAD");

            }
        });
        mBanner.addView(mBannerView);// 把banner加载到容器
        mBannerView.loadAD();
    }
    private boolean animTag=true;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getColor(OnEventpos onEventpos) {
        int pos = onEventpos.pos;
        if (pos == 2) {
            //动画
            if(animTag){
                animTag=false;
                AnimUtils.setTitleUp(getContext(),tomorrowTitle);
            }
            AnimUtils.setBackUp(getContext(),tomorrowBack);
        } else if (pos == 3) {
            //动画
            if(animTag==false){
                animTag=true;
                AnimUtils.setTitleDown(getContext(),tomorrowTitle);
            }
            AnimUtils.setBackDown(getContext(),tomorrowBack);
        }
    }

    @OnClick({R.id.tomorrow_back, R.id.today_refresh, R.id.today_refreshs, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tomorrow_back:
                EventBus.getDefault().post(new OnEventpos(1));
                break;
            case R.id.today_refresh:
                if (progressDialog != null) {
                    progressDialog.show();
                }

                tomorrowPresent.getTomorrow(city);
                break;
            case R.id.today_refreshs:
                if (progressDialog != null) {
                    progressDialog.show();
                }
                tomorrowPresent.getTomorrow(city);
                break;
            case R.id.search:
                getSearchCity();
                break;
        }
    }

    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void getInfo(WeaTomorrow weaTomorrow) {
        WeaTomorrow.HeWeather5Bean.BasicBean basic = weaTomorrow.getHeWeather5().get(0).getBasic();
        WeaTomorrow.HeWeather5Bean.DailyForecastBean castBean = weaTomorrow.getHeWeather5().get(0).getDaily_forecast().get(0);
        todayCity.setText(/*basic.getProv()+*/basic.getCity());
        String loc = basic.getUpdate().getLoc();
        //时间
        todayTime.setText(loc);
        //天气
        todayClass.setText(castBean.getCond().getTxt_d());
        if (castBean.getCond().getTxt_d().length() <= 4 && castBean.getCond().getTxt_d().length() >= 3) {
            todayClass.setTextSize(34);
        } else if (castBean.getCond().getTxt_d().length() > 4) {
            todayClass.setTextSize(30);
        }
        todayWendu.setText((Integer.valueOf(castBean.getTmp().getMax()) + Integer.valueOf(castBean.getTmp().getMin())) / 2 + "℃");
        todayPower1.setText("风向:" + castBean.getWind().getDir());
        todayPower2.setText("风力:" + castBean.getWind().getSc());
        todayPower3.setText("风速:" + castBean.getWind().getSpd() + "kmph");
        String code = castBean.getCond().getCode_d();
        EventBus.getDefault().post(new OnEventpos(Integer.valueOf(code)));
        tomorrowYue.setText("月出:" + castBean.getAstro().getMr() + "h,月落:" + castBean.getAstro().getMs() + "h");
        tomorrowRi.setText("日出:" + castBean.getAstro().getSr() + "h,日落:" + castBean.getAstro().getSs() + "h");
        tomorrowBai.setText("白天:" + castBean.getCond().getTxt_d());
        tomorrowHei.setText("晚上:" + castBean.getCond().getTxt_n());

        recyclerViews.setLayoutManager(linearLayoutManager);
        recyclerTomorrowAdapter = new RecyclerTomorrowAdapter(getContext(), weaTomorrow.getHeWeather5().get(0).getDaily_forecast());
        recyclerViews.setAdapter(recyclerTomorrowAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerViews, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
    }

    private void getSearchCity() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.search, null);
        final EditText editText = (EditText) inflate.findViewById(R.id.search);
        new AlertDialog.Builder(getContext())
                .setTitle("城市搜索:")
                .setView(inflate)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString() != null) {
                            city = editText.getText().toString().trim();
                            tomorrowPresent.getTomorrow(city);
                            if (progressDialog != null) {
                                progressDialog.show();
                            }
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public void dismissDialogs() {
        progressDialog.dismiss();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mAnimView!=null){
            mAnimView.stopAnim();
        }
    }

    @Override
    public void setListener() {

    }

}
