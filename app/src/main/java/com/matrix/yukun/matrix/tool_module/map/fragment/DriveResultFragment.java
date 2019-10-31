package com.matrix.yukun.matrix.tool_module.map.fragment;

import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;

/**
 * author: kun .
 * date:   On 2019/10/31
 */
public class DriveResultFragment extends BaseFragment {

    public static DriveResultFragment getInstance(){
        DriveResultFragment workDriveResultFragment=new DriveResultFragment();
        Bundle bundle=new Bundle();
        workDriveResultFragment.setArguments(bundle);
        return workDriveResultFragment;
    }

    public void setType(){

    }
    @Override
    public int getLayout() {
        return R.layout.fragment_drive_result;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

    }
}
