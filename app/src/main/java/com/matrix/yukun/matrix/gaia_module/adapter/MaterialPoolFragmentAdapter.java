package com.matrix.yukun.matrix.gaia_module.adapter;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.fragment.MaterialPoolFragment;


/**
 * Created by haiyang-lu on 16-4-18.
 * 作品池的片段适配器
 */
public class MaterialPoolFragmentAdapter extends FragmentStatePagerAdapter {
    private Activity mActivity;
    private Fragment[] mWorkPoolFragments;
    private String[] strings;

    public MaterialPoolFragmentAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        mActivity = activity;
        strings = mActivity.getResources().getStringArray(R.array.material_video);
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
            mWorkPoolFragments[position] = MaterialPoolFragment.getInstance(position);
        }
        return mWorkPoolFragments[position];
    }

}
