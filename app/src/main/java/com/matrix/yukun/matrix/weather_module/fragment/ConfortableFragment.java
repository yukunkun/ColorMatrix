package com.matrix.yukun.matrix.weather_module.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.BaseFrag;
import com.matrix.yukun.matrix.selfview.MyListView;
import com.matrix.yukun.matrix.weather_module.bean.EventDay;
import com.matrix.yukun.matrix.weather_module.bean.WeaLifePoint;
import com.matrix.yukun.matrix.weather_module.present.ConforableFragImpl;
import com.matrix.yukun.matrix.weather_module.present.ConfortablePresent;

import org.greenrobot.eventbus.EventBus;

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

    @Override
    public void getLifeInfo(WeaLifePoint weaLifePoint) {
        confAir.setText(weaLifePoint.getHeWeather5().get(0).getSuggestion().getAir().getBrf());
        todayTime.setText(weaLifePoint.getHeWeather5().get(0).getBasic().getUpdate().getLoc());
        todayCity.setText(weaLifePoint.getHeWeather5().get(0).getBasic().getCity());
        myListAdapter = new MyListAdapter(getContext(), weaLifePoint.getHeWeather5().get(0).getSuggestion());
        mylistview.setAdapter(myListAdapter);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @OnClick({R.id.today_refresh, R.id.today_refreshs, R.id.today_tomorrow, R.id.today_life,R.id.search})
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

}
