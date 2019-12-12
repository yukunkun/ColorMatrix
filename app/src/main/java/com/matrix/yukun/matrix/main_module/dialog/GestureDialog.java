package com.matrix.yukun.matrix.main_module.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.tool_module.gesture.GestureActivity;

/**
 * author: kun .
 * date:   On 2018/12/11
 */
public class GestureDialog extends DialogFragment implements View.OnClickListener {


    private ImageView mIvSure;
    private ImageView mIvcancel;

    public static GestureDialog getInstance(){
        return new GestureDialog();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        View view=inflater.inflate(R.layout.gesture_dialog,null);
        mIvSure = view.findViewById(R.id.iv_sure);
        mIvcancel = view.findViewById(R.id.iv_cancel);
        mIvSure.setOnClickListener(this);
        mIvcancel.setOnClickListener(this);
        return view;
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
            params.dimAmount =0.6f;
            //修改gravity
            params.gravity = Gravity.CENTER;
            params.width =width*6/10;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.iv_cancel){
            getDialog().dismiss();
        }
        if(id== R.id.iv_sure){
            getDialog().dismiss();
            Intent intent=new Intent(getContext(), GestureActivity.class);
            getContext().startActivity(intent);
        }
        SPUtils.getInstance().saveBoolean("first",true);
    }
}
