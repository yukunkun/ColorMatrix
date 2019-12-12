package com.matrix.yukun.matrix.main_module.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yukun on 17-11-17.
 */

public class MViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private String[] mTitleList;
    FragmentManager mFragmentManager;
    public MViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] mTitleList) {
        super(fm);
        this.mFragmentManager=fm;
        this.fragments=fragments;
        this.mTitleList=mTitleList;
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitleList.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList[position];//页卡标题
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.mFragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragments.get(position);
        mFragmentManager.beginTransaction().hide(fragment).commit();
    }
}
