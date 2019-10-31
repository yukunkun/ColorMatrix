package com.matrix.yukun.matrix.tool_module.map.fragment;

import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.SupportMapFragment;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;

/**
 * author: kun .
 * date:   On 2019/10/31
 */
public class DriveResultFragment extends BaseFragment {

    private AMap mMap;
    public static DriveResultFragment getInstance(){
        DriveResultFragment workDriveResultFragment=new DriveResultFragment();
        Bundle bundle=new Bundle();
        workDriveResultFragment.setArguments(bundle);
        return workDriveResultFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_drive_result;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        setUpMapIfNeeded();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
    }
}
