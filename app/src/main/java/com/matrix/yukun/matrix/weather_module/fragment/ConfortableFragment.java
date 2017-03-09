package com.matrix.yukun.matrix.weather_module.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.matrix.yukun.matrix.selfview.MyListView;
import com.matrix.yukun.matrix.weather_module.bean.EventDay;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.present.ConforableFragImpl;
import com.matrix.yukun.matrix.weather_module.present.ConfortablePresent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by yukun on 17-3-7.
 */
public class ConfortableFragment extends BaseFrag implements ConforableFragImpl {

    @BindView(R.id.tomorrow_back)
    LinearLayout tomorrowBack;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.conf_title)
    RelativeLayout confTitle;
    @BindView(R.id.today_city)
    TextView todayCity;
    @BindView(R.id.today_refresh)
    ImageView todayRefresh;
    @BindView(R.id.today_refreshs)
    TextView todayRefreshs;
    @BindView(R.id.today_time)
    TextView todayTime;
    @BindView(R.id.today_tomorrow)
    TextView todayTomorrow;
    @BindView(R.id.today_life)
    TextView todayLife;
    @BindView(R.id.today_destory)
    TextView todayDestory;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.mylistview)
    MyListView mylistview;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.conf_air)
    TextView confAir;
    private ConfortablePresent mPresent;
    private String city;
    private ProgressDialog progressDialog;
    private MyListAdapter myListAdapter;
    private Animation operatingAnim;
    private Animation operatingAnim1;
    private Animation operatingRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("刷新中...");
        progressDialog.show();
        city = getArguments().getString("city");
        mPresent = new ConfortablePresent(this, city);
        this.basePresent = mPresent;
        super.onCreate(savedInstanceState);
    }

    public static ConfortableFragment newInstance(String arg) {
        ConfortableFragment fragment = new ConfortableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wea_conforable, null);
        ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(this);
        todayDestory.setVisibility(View.GONE);
        todayTomorrow.setText("今日天气");
        todayLife.setText("明日天气");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        // Horizontal
        setListener();
        return inflate;
    }

    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void dismissDialogs() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setListener() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getColor(OnEventpos onEventpos) {
        int pos = onEventpos.pos;
        if (pos == 2) {
            setBackAnim();
        } else if (pos == 3) {
            setBackAnimBack();
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

    @Override
    public void getLifeInfo(WeaLifePoint weaLifePoint) {
        confAir.setText(weaLifePoint.getHeWeather5().get(0).getSuggestion().getAir().getBrf());
        todayTime.setText(weaLifePoint.getHeWeather5().get(0).getBasic().getUpdate().getLoc());
        todayCity.setText(weaLifePoint.getHeWeather5().get(0).getBasic().getCity());
        myListAdapter = new MyListAdapter(getContext(), weaLifePoint.getHeWeather5().get(0).getSuggestion());
        mylistview.setAdapter(myListAdapter);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @OnClick({R.id.tomorrow_back,R.id.today_refresh, R.id.today_refreshs, R.id.today_tomorrow, R.id.today_life,R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.today_refresh:
                mPresent.getInfo(city);
                if (progressDialog != null) {
                    progressDialog.show();
                }
                break;
            case R.id.today_refreshs:
                mPresent.getInfo(city);
                if (progressDialog != null) {
                    progressDialog.show();
                }
                break;
            case R.id.today_tomorrow:
                EventBus.getDefault().post(new EventDay("today"));
                break;
            case R.id.today_life:
                EventBus.getDefault().post(new EventDay("tomorrow"));
                break;
            case R.id.search:
                getSearchCity();
                break;
            case R.id.tomorrow_back:
                EventBus.getDefault().post(new OnEventpos(1));
                break;
        }
    }
    private void getSearchCity() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.search, null);
        final EditText editText= (EditText) inflate.findViewById(R.id.search);
        new AlertDialog.Builder(getContext())
                .setTitle("城市搜索:")
                .setView(inflate)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editText.getText().toString()!=null){
                            city=editText.getText().toString().trim();
                            mPresent.getInfo(city);
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
}
