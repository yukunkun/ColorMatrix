package com.matrix.yukun.matrix.video_module;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.matrix.yukun.matrix.util.ScreenUtils;

import butterknife.ButterKnife;

/**
 * Created by yukun on 17-7-25.
 */

public abstract class BaseCenterDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        View inflate = inflater.inflate(setLayout(), null);
        ButterKnife.bind(this, inflate);
        initView(inflate, savedInstanceState);
        return inflate;
    }

    protected abstract void initView(View inflate, Bundle savedInstanceState);

    public abstract int setLayout();

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            int width = ScreenUtils.instance().getWidth(getContext());
            int height = ScreenUtils.instance().getHeight(getContext());
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount =0f;
            //修改gravity
            params.gravity = Gravity.CENTER;
            params.width =width*4/5;
            params.height = height*3/7;
            window.setAttributes(params);
        }
    }
}
