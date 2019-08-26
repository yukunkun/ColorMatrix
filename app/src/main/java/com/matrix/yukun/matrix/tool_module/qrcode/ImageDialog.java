package com.matrix.yukun.matrix.tool_module.qrcode;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;

import uk.co.senab.photoview.PhotoView;

/**
 * author: kun .
 * date:   On 2018/11/13
 */
public class ImageDialog extends DialogFragment implements View.OnClickListener {

    private static Bitmap mBitmap;
    public static ImageDialog getInstance(Bitmap bitmap) {
        mBitmap = bitmap;
        ImageDialog imageDialog=new ImageDialog();
        return imageDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        View inflate = inflater.inflate(R.layout.image_dialog, null);
        PhotoView photoView = inflate.findViewById(R.id.photo_view);
        ImageView ivBack=inflate.findViewById(R.id.iv_back);
        photoView.setImageBitmap(mBitmap);
        photoView.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        return inflate;
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
            params.gravity = Gravity.CENTER;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo_view:
                getDialog().dismiss();
                break;
            case R.id.iv_back:
                dismiss();
                break;
        }

    }
}
