package com.matrix.yukun.matrix.tool_module.weather.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.tool_module.barrage.dialog.BaseBottomDialog;

/**
 * author: kun .
 * date:   On 2019/10/23
 */
public class MapWeaDialog extends BaseBottomDialog {

    public static MapWeaDialog getInstance(){

        return new MapWeaDialog();
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public int setLayout() {
        return R.layout.map_dialog_fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount =0f;
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.height = ScreenUtil.getDisplayHeight()*4/10;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }
}
