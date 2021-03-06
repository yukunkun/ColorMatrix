package com.matrix.yukun.matrix.tool_module.gesture;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by yukun on 16-7-14.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments;
    String[] strings;
    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] strings) {
        super(fm);
        this.fragments=fragments;
        this.strings=strings;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position % strings.length];
    }
}
