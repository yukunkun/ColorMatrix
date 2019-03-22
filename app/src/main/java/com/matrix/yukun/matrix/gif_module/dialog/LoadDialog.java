package com.matrix.yukun.matrix.gif_module.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.matrix.yukun.matrix.video_module.BaseCenterDialog;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * author: kun .
 * date:   On 2019/2/15
 */
public class LoadDialog extends BaseCenterDialog {

    public static LoadDialog getInstance(){
        LoadDialog loadDialog=new LoadDialog();
        return loadDialog;
    }
    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
//        AVLoadingIndicatorView avLoadingIndicatorView=inflate.findViewById(R.id.av_load);
//        avLoadingIndicatorView.setIndicator("BallTrianglePathIndicator");
//        avLoadingIndicatorView.show();
//        avLoadingIndicatorView.smoothToShow();
    }

//    public void dismiss(){
//        getDialog().dismiss();
//    }

    @Override
    public int setLayout() {
        return R.layout.load_dialog;
    }

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
            params.width =width;
            params.height = height;
            window.setAttributes(params);
        }
    }
}
