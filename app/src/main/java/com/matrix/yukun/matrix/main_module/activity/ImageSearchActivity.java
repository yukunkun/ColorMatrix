package com.matrix.yukun.matrix.main_module.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.fragment.SearchImageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageSearchActivity extends BaseActivity {
    @BindView(R2.id.tablayout)
    TabLayout mTablayout;
    @BindView(R2.id.iv_back)
    ImageView mIvClose;
    @BindView(R2.id.viewpager)
    ViewPager mViewpager;
    private String[] mStringArray;
    List<Fragment> mFragments = new ArrayList<>();
    private MViewPagerAdapter mMViewPagerAdapter;
    public static void start(Context context){
        Intent intent=new Intent(context,ImageSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_image_search;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDate() {
        mStringArray = getResources().getStringArray(R.array.search);
        for (int i = 0; i < mStringArray.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mStringArray[i]));
            SearchImageFragment mInstance5= SearchImageFragment.getInstance(mStringArray[i]);
            mFragments.add(mInstance5);
        }
        setAdapter();
    }

    @Override
    public void initListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                mTablayout.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTablayout.setupWithViewPager(mViewpager);
    }

    private void setAdapter() {
        mMViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(), mFragments, mStringArray);
        mViewpager.setAdapter(mMViewPagerAdapter);
        mViewpager.setOffscreenPageLimit(5);
    }
    @OnClick({R2.id.iv_back})
    public void onClick(View view) {
        int id = view.getId();
        if(id== R.id.iv_back){
            finish();
        }
    }
}
