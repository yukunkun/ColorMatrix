package com.matrix.yukun.matrix.weather_module.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.BaseFrag;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.weather_module.bean.WeaDestory;
import com.matrix.yukun.matrix.weather_module.bean.WeaNow;
import com.matrix.yukun.matrix.weather_module.present.TodayFragmentImpl;
import com.matrix.yukun.matrix.weather_module.present.TodayPresent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yukun on 17-3-6.
 */
public class TodayWeathFrag extends BaseFrag implements TodayFragmentImpl, View.OnClickListener {

    @BindView(R.id.today_back)
    LinearLayout todayBack;
    @BindView(R.id.today_titile)
    RelativeLayout todayTitile;
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
    @BindView(R.id.today_wendu)
    TextView todayWendu;
    @BindView(R.id.today_tomorrow)
    TextView todayTomorrow;
    @BindView(R.id.today_life)
    TextView todayLife;
    @BindView(R.id.today_destory)
    TextView todayDestory;
    @BindView(R.id.today_1)
    TextView today1;
    @BindView(R.id.today_2)
    TextView today2;
    @BindView(R.id.today_3)
    TextView today3;
    @BindView(R.id.today_4)
    TextView today4;
    @BindView(R.id.today_5)
    TextView today5;
    @BindView(R.id.today_6)
    TextView today6;
    @BindView(R.id.today_power_1)
    TextView todayPower1;
    @BindView(R.id.today_power_2)
    TextView todayPower2;
    @BindView(R.id.today_power_3)
    TextView todayPower3;
    @BindView(R.id.today_destory_alarm)
    TextView todayDestoryAlarm;
    @BindView(R.id.today_destory_text)
    TextView todayDestoryText;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    private TodayPresent topPresent;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        topPresent = new TodayPresent(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("刷新中...");
        progressDialog.show();
        this.basePresent = topPresent;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wea_today, null);
        ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(this);
        getViews(inflate);
        setListener();
        return inflate;

    }
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void getColor(OnEventpos onEventpos){
        int pos = onEventpos.pos;
        if(pos==1){
            todayTitile.setBackgroundColor(getResources().getColor(R.color.color_82181818));
        }else if(pos==2){
            todayTitile.setBackgroundColor(getResources().getColor(R.color.color_00ffffff));
        }
    }



    @Override
    public void getViews(View view) {

    }

    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void getDestory(WeaDestory weaDestory) {
        List<WeaDestory.HeWeather5Bean.AlarmsBean> alarms = weaDestory.getHeWeather5().get(0).getAlarms();
        if(alarms!=null){
            todayDestoryAlarm.setText("type:"+alarms.get(0).getLevel()+","+alarms.get(0).getTitle());
            todayDestoryText.setText("预警内容:"+alarms.get(0).getTxt());
        }else {
            todayDestoryAlarm.setText("无任何自然灾害预警");
            todayDestoryText.setText("预警内容:无");
        }
    }

    @Override
    public void getInfo(WeaNow weaNow) {
        WeaNow.HeWeather5Bean.BasicBean basic = weaNow.getHeWeather5().get(0).getBasic();
        WeaNow.HeWeather5Bean.NowBean now = weaNow.getHeWeather5().get(0).getNow();
        todayCity.setText(/*basic.getProv()+*/basic.getCity());
        String loc = basic.getUpdate().getLoc();
        //时间
        todayTime.setText(loc);
        //天气
        todayClass.setText(now.getCond().getTxt());
        todayWendu.setText(now.getFl()+"℃");
        today1.setText("体感温度:"+now.getFl()+"℃");
        today2.setText("相对湿度:"+now.getHum()+"%");
        today3.setText("降水量:"+now.getPcpn()+"mm");
        today4.setText("气压:"+now.getPres());
        today5.setText("温度:"+now.getTmp());
        today6.setText("能见度:"+now.getVis()+"km");
        todayPower1.setText("风向:"+now.getWind().getDir());
        todayPower2.setText("风力:"+now.getWind().getSc());
        todayPower3.setText("风速:"+now.getWind().getSpd()+"kmph");

    }

    @Override
    public void dismissDialogs() {
        progressDialog.dismiss();
    }

    @Override
    public void setListener() {
        todayRefreshs.setOnClickListener(this);
        todayRefresh.setOnClickListener(this);
        todayTomorrow.setOnClickListener(this);
        todayDestory.setOnClickListener(this);
        todayLife.setOnClickListener(this);
        todayBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.today_refresh:
                if(progressDialog!=null){
                    progressDialog.show();
                }
                topPresent.getInfo();
                break;
            case R.id.today_refreshs:
                if(progressDialog!=null){
                    progressDialog.show();
                }
                topPresent.getInfo();
                break;
            case R.id.today_tomorrow:
                break;
            case R.id.today_destory:
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            //生活指数
            case R.id.today_life:
                scrollview.smoothScrollTo(0, 0);
                scrollview.clearFocus();
                break;
            case R.id.today_back:
                EventBus.getDefault().post(new OnEventpos(0));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
