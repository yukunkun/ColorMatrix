package com.matrix.yukun.matrix.main_module;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.ScreenUtils;
import com.matrix.yukun.matrix.video_module.BaseCenterDialog;

/**
 * author: kun .
 * date:   On 2019/1/30
 */
public class PhotoChooseDialog extends BaseCenterDialog {

    private RelativeLayout mRlCamera;
    private RelativeLayout mRlphoto;

    public static PhotoChooseDialog getInstance(){
        return new PhotoChooseDialog();
    }
    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        mRlCamera = inflate.findViewById(R.id.rl_camera);
        mRlphoto = inflate.findViewById(R.id.rl_photo);
        mRlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openCamera();
                getDialog().dismiss();
            }
        });

        mRlphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).choosePhoto();
                getDialog().dismiss();
            }
        });
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public int setLayout() {
        return R.layout.photo_choose;
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
            params.dimAmount =0.4f;
            //修改gravity
            params.gravity = Gravity.CENTER;
            params.width =width*4/5;
            params.height = height*2/7;
            window.setAttributes(params);
        }
    }
}
