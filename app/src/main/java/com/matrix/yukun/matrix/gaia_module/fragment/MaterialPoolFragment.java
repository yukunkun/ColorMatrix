package com.matrix.yukun.matrix.gaia_module.fragment;

import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseFragment;

/**
 * author: kun .
 * date:   On 2019/7/5
 */
public class MaterialPoolFragment extends BaseFragment {

    public static int pos;
    public static MaterialPoolFragment getInstance(int position) {
        pos=position;
        MaterialPoolFragment workSearchFragment = new MaterialPoolFragment();
        return workSearchFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.matrial_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

    }
}
