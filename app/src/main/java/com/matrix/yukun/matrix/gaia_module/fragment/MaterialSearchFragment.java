package com.matrix.yukun.matrix.gaia_module.fragment;

import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseFragment;

/**
 * author: kun .
 * date:   On 2019/7/5
 */
public class MaterialSearchFragment extends BaseFragment {

    public static MaterialSearchFragment getInstance(){
        MaterialSearchFragment materialSearchFragment=new MaterialSearchFragment();
        return materialSearchFragment;
    }
    @Override
    public int getLayout() {
        return R.layout.work_material_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

    }
}
