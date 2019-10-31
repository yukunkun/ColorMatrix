package com.matrix.yukun.matrix.tool_module.map.fragment;

import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;

/**
 * author: kun .
 * date:   On 2019/10/31
 */
public class WalkResultFragment extends BaseFragment {

    public static WalkResultFragment getInstance(){
        WalkResultFragment workDriveResultFragment=new WalkResultFragment();
        Bundle bundle=new Bundle();
        workDriveResultFragment.setArguments(bundle);
        return workDriveResultFragment;
    }

    public void setType(){

    }
    @Override
    public int getLayout() {
        return R.layout.fragment_work_drive_result;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

    }
}
