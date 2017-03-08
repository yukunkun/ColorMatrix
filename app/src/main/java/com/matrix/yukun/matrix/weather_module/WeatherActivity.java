package com.matrix.yukun.matrix.weather_module;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.util.GetCity;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.matrix.yukun.matrix.weather_module.bean.EventDay;
import com.matrix.yukun.matrix.weather_module.fragment.ConfortableFragment;
import com.matrix.yukun.matrix.weather_module.fragment.TodayWeathFrag;
import com.matrix.yukun.matrix.weather_module.fragment.TomorrowWeathFrag;
import com.matrix.yukun.matrix.weather_module.present.WeatherPreImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TimerTask;

import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class WeatherActivity extends AppCompatActivity implements WeatherPreImpl {

    protected Context context;
    private RadioGroup radioGroup;
    private ArrayList<Fragment> fragments;
    private int lastPos=0;
    GestureDetector detector;
    private ImageView imageView;
    private String  city="成都";;
    private FragmentTransaction fragmentTransaction;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
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
        ((RadioButton)findViewById(R.id.today)).setTextSize(18);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void getInfo() {
        fragments = new ArrayList<>();
        TodayWeathFrag todayWeathFrag=TodayWeathFrag.newInstance(city);
        TomorrowWeathFrag tomorrowWeathFrag=TomorrowWeathFrag.newInstance(city);
        ConfortableFragment confortableFragment=ConfortableFragment.newInstance(city);
        fragments.add(todayWeathFrag);
        fragments.add(tomorrowWeathFrag);
        fragments.add(confortableFragment);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contains,todayWeathFrag).commit();
    }

    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==R.id.today){
                        show(0);
                        ((RadioButton)findViewById(R.id.today)).setTextSize(18);
                        ((RadioButton)findViewById(R.id.tomorrow)).setTextSize(15);
                        ((RadioButton)findViewById(R.id.life)).setTextSize(15);
                    }else if(checkedId==R.id.tomorrow){
                        show(1);
                        ((RadioButton)findViewById(R.id.today)).setTextSize(15);
                        ((RadioButton)findViewById(R.id.tomorrow)).setTextSize(18);
                        ((RadioButton)findViewById(R.id.life)).setTextSize(15);
                    }else if(checkedId==R.id.life){
                        show(2);
                        ((RadioButton)findViewById(R.id.today)).setTextSize(15);
                        ((RadioButton)findViewById(R.id.tomorrow)).setTextSize(15);
                        ((RadioButton)findViewById(R.id.life)).setTextSize(18);
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
        if(pos==1){
            finish();
        }else if(pos>=100){
            setBackImage(pos);
        }
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void getDay(EventDay eventDay){
        String fragmentTag = eventDay.day;

        if(fragmentTag.equals("tomorrow")){
            ((RadioButton)findViewById(R.id.tomorrow)).setChecked(true);
        }else if(fragmentTag.equals("life")){
            ((RadioButton)findViewById(R.id.life)).setChecked(true);
        }else if(fragmentTag.equals("today")){
            ((RadioButton)findViewById(R.id.today)).setChecked(true);        }
    }

    private void setBackImage(int pos) {
        if(pos<102) {
            Glide.with(this).load(R.mipmap.wea_chuqing)
                    .into(imageView);
        }else if(pos<=104){
                Glide.with(this).load(R.mipmap.chuntian)
                        .into(imageView);
        }else if(pos<=205){
            Glide.with(this).load(R.mipmap.feng1)
                    .into(imageView);
        }else if(pos<=213){
            Glide.with(this).load(R.mipmap.feng)
                    .into(imageView);
        }else if(pos<=313) {
            Glide.with(this).load(R.mipmap.yu2)
                    .into(imageView);
        }else if(pos<=402){
            Glide.with(this).load(R.mipmap.xue)
                    .into(imageView);
        }else if(pos<=406){
            Glide.with(this).load(R.mipmap.xue1)
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
                    EventBus.getDefault().post(new OnEventpos(2));
                }else if(endY-beginY>60&&Math.abs(velocityY)>0){   //下滑
                    EventBus.getDefault().post(new OnEventpos(3));

                }
            }
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        showPopu();
    }

    private void showPopu() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.popuwindow_layout, null);
        popupWindow =new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setContentView(inflate);
        inflate.findViewById(R.id.tuichu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inflate.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        popupWindow.showAtLocation(this.findViewById(R.id.group), Gravity.CENTER,0,0);
    }

    //获取虚拟菜单高度
    public int getBottomStatusHeight(Context context){
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(this);

        return totalHeight  - contentHeight;
    }

    public int getDpi(Context context){
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi=displayMetrics.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dpi;
    }

    public  int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
