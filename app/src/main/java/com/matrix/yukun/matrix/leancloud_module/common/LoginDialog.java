package com.matrix.yukun.matrix.leancloud_module.common;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.LoginActivity;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.tool_module.calarder.dialog.BaseCenterDialog;

/**
 * author: kun .
 * date:   On 2019/12/16
 */
public class LoginDialog extends BaseCenterDialog {

    public static LoginDialog start(){
        LoginDialog loginDialog=new LoginDialog();
        return loginDialog;
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
//        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_log_dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            int width = ScreenUtils.instance().getWidth(getContext());
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount =0.2f;
            //修改gravity
            params.gravity = Gravity.CENTER;
            params.width =width;
            window.setAttributes(params);
        }
    }
}
