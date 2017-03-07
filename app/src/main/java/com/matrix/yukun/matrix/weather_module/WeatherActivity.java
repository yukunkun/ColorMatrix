package com.matrix.yukun.matrix.weather_module;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.util.GetCity;
import com.matrix.yukun.matrix.weather_module.fragment.TodayWeathFrag;
import com.matrix.yukun.matrix.weather_module.fragment.TomorrowWeathFrag;
import com.matrix.yukun.matrix.weather_module.present.WeatherPreImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class WeatherActivity extends AppCompatActivity implements WeatherPreImpl {

    protected Context context;
    private RadioGroup radioGroup;
    private ArrayList<Fragment> fragments;
    private int lastPos=0;
    GestureDetector detector;
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        EventBus.getDefault().register(this);
//        GetCity.getInstance(this).getCity();
        init();
        getInfo();
        detector = new GestureDetector(this, new listener());
        setListener();
    }

    @Override
    public void init() {
        radioGroup = (RadioGroup) findViewById(R.id.group);
        imageView=(ImageView) findViewById(R.id.back_image);
        ((RadioButton)(radioGroup.getChildAt(0))).setChecked(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void getInfo() {
        fragments = new ArrayList<>();
        TodayWeathFrag todayWeathFrag=new TodayWeathFrag();
        TomorrowWeathFrag tomorrowWeathFrag=new TomorrowWeathFrag();
        fragments.add(todayWeathFrag);
        fragments.add(tomorrowWeathFrag);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contains,todayWeathFrag).commit();
    }

    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==R.id.today){
                        show(0);
                    }else if(checkedId==R.id.tomorrow){
                        show(1);
                    }
            }
        });
    }
    //fragment
    @Override
    public void show(int pos) {
        Fragment fragment=fragments.get(pos);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments.get(lastPos));
        if(fragment.isAdded()){
            fragmentTransaction.show(fragment);
        }else {
            fragmentTransaction.add(R.id.contains,fragment);
        }
        fragmentTransaction.commit();
        lastPos=pos;
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getCity(OnEventpos onEventpos){

        int pos = onEventpos.pos;
        if(pos==0){
            finish();
        }else if(pos>=100){
            setBackImage(pos);
        }
    }

    private void setBackImage(int pos) {
        if(pos<102) {
            Glide.with(this).load(R.mipmap.wea_chuqing)
                    .into(imageView);
        }else if(pos<=103){
                Glide.with(this).load(R.mipmap.chuntian)
                        .into(imageView);
        }/*else if(pos<=206){
            Glide.with(this).load(R.mipmap.wea_chuqing)
                    .into(imageView);
        }*/else if(pos<=213){
            Glide.with(this).load(R.mipmap.feng)
                    .into(imageView);
        }else if(pos<=313) {
            Glide.with(this).load(R.mipmap.yu2)
                    .into(imageView);
        }else if(pos<=406){
            Glide.with(this).load(R.mipmap.xue2)
                    .into(imageView);
        }else if(pos<=406){
            Glide.with(this).load(R.mipmap.xue2)
                    .into(imageView);
        }else if(pos<=502){
            Glide.with(this).load(R.mipmap.wea_wu)
                    .into(imageView);
        }else {
            Glide.with(this).load(R.mipmap.wea_chuqing)
                    .into(imageView);
        }
    }

    class listener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1!=null){
                float beginY = e1.getY();
                float endY = e2.getY();
                if(beginY-endY>60&&Math.abs(velocityY)>0){   //上滑
                    EventBus.getDefault().post(new OnEventpos(1));
                }else if(endY-beginY>60&&Math.abs(velocityY)>0){   //下滑
                    EventBus.getDefault().post(new OnEventpos(2));

                }
            }
            return false;
        }
    }

}
