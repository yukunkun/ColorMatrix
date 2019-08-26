package com.matrix.yukun.matrix.tool_module.weather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.NoScrollListView;
import com.matrix.yukun.matrix.selfview.WaterLoadView;
import com.matrix.yukun.matrix.tool_module.weather.BaseFrag;
import com.matrix.yukun.matrix.tool_module.weather.bean.EventCity;
import com.matrix.yukun.matrix.tool_module.weather.bean.EventDay;
import com.matrix.yukun.matrix.tool_module.weather.bean.OnEventpos;
import com.matrix.yukun.matrix.tool_module.weather.bean.WeaLifePoint;
import com.matrix.yukun.matrix.tool_module.weather.present.ConforableFragImpl;
import com.matrix.yukun.matrix.tool_module.weather.present.ConfortablePresent;
import com.matrix.yukun.matrix.util.AnimUtils;
import com.matrix.yukun.matrix.util.task.AddressInitTask;
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
    NoScrollListView mMylistview;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.conf_air)
    TextView confAir;
    @BindView(R.id.waterload)
    WaterLoadView mWaterload;
    private ConfortablePresent mPresent;
    private String city;
    private MyListAdapter myListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        mWaterload.setVisibility(View.VISIBLE);
        setListener();
//        mPresent.getInfo(city);
        return inflate;
    }

    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void dismissDialogs() {
        mWaterload.setVisibility(View.GONE);
    }

    @Override
    public void setListener() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getColor(OnEventpos onEventpos) {
        int pos = onEventpos.pos;
        if (pos == 2) {
            AnimUtils.setBackUp(getContext(), tomorrowBack);
        } else if (pos == 3) {
            AnimUtils.setBackDown(getContext(), tomorrowBack);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCity(EventCity onEventpos) {
        String pos = onEventpos.city;
        MyApp.showToast(pos);
        mPresent.getInfo(pos);
    }

    @Override
    public void getLifeInfo(WeaLifePoint weaLifePoint) {
        if(!weaLifePoint.getHeWeather6().get(0).getStatus().equals("ok")){
            return;
        }
        confAir.setText(weaLifePoint.getHeWeather6().get(0).getLifestyle().get(0).getBrf());
        todayTime.setText(weaLifePoint.getHeWeather6().get(0).getUpdate().getLoc());
        todayCity.setText(weaLifePoint.getHeWeather6().get(0).getBasic().getLocation());
        myListAdapter = new MyListAdapter(getContext(), weaLifePoint.getHeWeather6().get(0).getLifestyle());
        mMylistview.setAdapter(myListAdapter);
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @OnClick({R.id.tomorrow_back, R.id.today_refresh, R.id.today_refreshs, R.id.today_tomorrow, R.id.today_life, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.today_refresh:
                mPresent.getInfo(city);
                mWaterload.setVisibility(View.VISIBLE);

                break;
            case R.id.today_refreshs:
                mPresent.getInfo(city);
                mWaterload.setVisibility(View.VISIBLE);
                break;
            case R.id.today_tomorrow:
                EventBus.getDefault().post(new EventDay("today"));
                break;
            case R.id.today_life:
                EventBus.getDefault().post(new EventDay("tomorrow"));
                break;
            case R.id.search:
                new AddressInitTask(getActivity(), true).execute("四川省", "成都市", "成华区");
                break;
            case R.id.tomorrow_back:
                EventBus.getDefault().post(new OnEventpos(1));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
