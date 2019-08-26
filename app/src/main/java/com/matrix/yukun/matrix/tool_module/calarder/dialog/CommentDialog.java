package com.matrix.yukun.matrix.tool_module.calarder.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.util.FileUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/19.
 */

public class CommentDialog extends BaseCenterDialog {

    @BindView(R.id.iv_erweima)
    ImageView mIvErweima;
    @BindView(R.id.rl_layout)
    RelativeLayout mRlLayout;

    public static CommentDialog getInstance(){
        CommentDialog commentDialog=new CommentDialog();
        return commentDialog;
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {

    }

    @Override
    public int setLayout() {
        return R.layout.dialog_commend_layout;
    }

    @OnClick({R.id.iv_erweima, R.id.rl_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_erweima:
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_erweima );
                FileUtil.loadImage(bitmap,"erweima.jpg");
                Toast.makeText(getContext(), "下载成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_layout:
                this.dismiss();
                break;
        }
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
