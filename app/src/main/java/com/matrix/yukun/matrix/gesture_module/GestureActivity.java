package com.matrix.yukun.matrix.gesture_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.adapter.PagerAdapter;
import com.matrix.yukun.matrix.selfview.GestureLockViewGroup;
import com.matrix.yukun.matrix.selfview.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GestureActivity extends AppCompatActivity {


    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_forget)
    TextView mTvForget;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.mViewPager)
    NoScrollViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private PagerAdapter mSecViewPagerAdapter;
    String [] strings=new String[]{"手势密码","人脸识别"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setListener();
        setAdapter();
    }

    private void setListener() {
        mTabLayout.addTab(mTabLayout.newTab().setText("手势密码"));
        mTabLayout.addTab(mTabLayout.newTab().setText("人脸识别"));

        fragments.add(new GestureFragment());
        fragments.add(new FaceFragment());
    }

    private void setAdapter() {
        mSecViewPagerAdapter = new PagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(mSecViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }


    public void GestureBack(View view) {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @OnClick({R.id.tv_forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                showAlterDialog();
                break;
        }
    }

    private void showAlterDialog() {
        new AlertDialog.Builder(this)
                .setTitle("忘记手势密码怎么办?")
                .setMessage("你可以通过数字密码重新进入")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void gettag(OnEventFinish eventFinish){
        int pos = eventFinish.pos;
        if(pos==1){
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

}
