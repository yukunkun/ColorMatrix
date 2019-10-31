package com.matrix.yukun.matrix.tool_module.map.fragment;

import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;

/**
 * author: kun .
 * date:   On 2019/10/31
 */
public class BusResultFragment extends BaseFragment {

    public static BusResultFragment getInstance(){
        BusResultFragment busResultFragment=new BusResultFragment();
        return busResultFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_bus_result;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

    }
}
