package com.matrix.yukun.matrix.movie_module;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.adapter.PagerAdapter;
import com.matrix.yukun.matrix.movie_module.fragment.NewMovieFragment;
import com.matrix.yukun.matrix.movie_module.fragment.SoonMoiveFragment;
import com.matrix.yukun.matrix.movie_module.fragment.Top250Fragment;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MovieActivity extends BaseActivity {

//    @BindView(R.id.mTablayout)
    TabLayout mTablayout;
//    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    private PagerAdapter secViewPagerAdapter;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        mTablayout= (TabLayout) findViewById(R.id.mTablayout);
        mViewPager= (ViewPager) findViewById(R.id.mViewPager);
        getInfo();
        setAdapter();
        setListener();
    }

    private void getInfo() {
        strings = getApplicationContext().getResources().getStringArray(R.array.movie_array);
        for (int i = 0; i < strings.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(/*strings[i]*/"Top"));
        }
        Top250Fragment top250Fragment=new Top250Fragment();
        NewMovieFragment newMovieFragment=new NewMovieFragment();
        SoonMoiveFragment soonMoiveFragment=new SoonMoiveFragment();;
        fragments.add(top250Fragment);
        fragments.add(newMovieFragment);
        fragments.add(soonMoiveFragment);
    }

    private void setListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTablayout.setScrollPosition(position,0,false);
            }
        });
    }

    private void setAdapter() {
        secViewPagerAdapter = new PagerAdapter(getSupportFragmentManager(),fragments,strings);
        mViewPager.setAdapter(secViewPagerAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
    }

    public void MovieBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
