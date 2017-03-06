package com.matrix.yukun.matrix.weather_module;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.util.GetCity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WeatherActivity extends AppCompatActivity/* implements LocationListener*/ {

    protected Context context;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
//        EventBus.getDefault().register(this);
//        GetCity.getInstance(this).getCity();
        textView = (TextView) findViewById(R.id.textview);

    }
//    @Subscribe(threadMode=ThreadMode.MAIN)
//    public void getCity(OnEventpos onEventpos){
//        String city = onEventpos.city;
//        textView.setText(city+"");
//        GetCity.getInstance(this).endGPS();
//    }

    public void WeaBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
