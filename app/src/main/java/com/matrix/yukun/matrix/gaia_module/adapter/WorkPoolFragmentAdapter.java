package com.matrix.yukun.matrix.gaia_module.adapter;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.fragment.ProductPoolFragment;


/**
 * Created by haiyang-lu on 16-4-18.
 * 作品池的片段适配器
 */
public class WorkPoolFragmentAdapter extends FragmentStatePagerAdapter {
    private Activity mActivity;
    private Fragment[] mWorkPoolFragments;
    private String[] strings;

    public WorkPoolFragmentAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        mActivity = activity;
        strings = mActivity.getResources().getStringArray(R.array.work_pool);
        mWorkPoolFragments = new Fragment[strings.length];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position];
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (mWorkPoolFragments[position]==null) {
            mWorkPoolFragments[position] = ProductPoolFragment.getInstance(position);
        }
        return mWorkPoolFragments[position];
    }

}
