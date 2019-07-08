package com.matrix.yukun.matrix.gaia_module.fragment;

import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseFragment;

/**
 * author: kun .
 * date:   On 2019/7/5
 */
public class WorkSearchFragment extends BaseFragment {

    public static WorkSearchFragment getInstance(){
        WorkSearchFragment workSearchFragment=new WorkSearchFragment();
        return workSearchFragment;
    }
    @Override
    public int getLayout() {
        return R.layout.work_search_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

    }
}
