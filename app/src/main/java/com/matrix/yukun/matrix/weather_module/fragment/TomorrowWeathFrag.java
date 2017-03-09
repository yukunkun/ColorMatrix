package com.matrix.yukun.matrix.weather_module.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.matrix.yukun.matrix.movie_module.BaseFrag;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.weather_module.bean.WeaTomorrow;
import com.matrix.yukun.matrix.weather_module.present.TomorrowFragmentImpl;
import com.matrix.yukun.matrix.weather_module.present.TomorrowPresent;

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
    private TomorrowPresent tomorrowPresent;
    private String city;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerTomorrowAdapter recyclerTomorrowAdapter;
    private Animation operatingAnim;
    private Animation operatingAnim1;
    private Animation operatingRefresh;

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
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        // Horizontal
        setListener();
        return inflate;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getColor(OnEventpos onEventpos) {
        int pos = onEventpos.pos;
        if (pos == 2) {
            setBackAnim();
            tomorrowTitle.setBackgroundColor(getResources().getColor(R.color.color_82181818));
        } else if (pos == 3) {
            setBackAnimBack();
            tomorrowTitle.setBackgroundColor(getResources().getColor(R.color.color_00ffffff));
        }
    }
    private void setBackAnim() {
        operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.back_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim.setFillAfter(true);
        tomorrowBack.startAnimation(operatingAnim);
    }
    private void setBackAnimBack() {
        operatingAnim1 = AnimationUtils.loadAnimation(getContext(), R.anim.back_anim_back);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim1.setInterpolator(lin);
        operatingAnim1.setFillAfter(true);
        tomorrowBack.startAnimation(operatingAnim1);
    }
    private void setFreshAnimBack() {
        operatingRefresh = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingRefresh.setInterpolator(lin);
        operatingRefresh.setFillAfter(true);
        todayRefresh.startAnimation(operatingRefresh);
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
        if(operatingAnim!=null){
            operatingAnim.cancel();
        }
        if(operatingAnim1!=null){
            operatingAnim1.cancel();
        }
//        operatingRefresh.cancel();
    }

    @Override
    public void setListener() {

    }

}
