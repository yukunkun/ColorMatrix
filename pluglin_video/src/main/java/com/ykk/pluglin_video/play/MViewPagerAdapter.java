package com.ykk.pluglin_video.play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by yukun on 17-11-17.
 */

public class MViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] mTitleList;
    public MViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] mTitleList) {
        super(fm);
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
}
