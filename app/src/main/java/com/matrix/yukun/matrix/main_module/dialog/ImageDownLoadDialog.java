package com.matrix.yukun.matrix.main_module.dialog;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.utils.DownLoadUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yukun on 17-12-26.
 */

public class ImageDownLoadDialog extends DialogFragment {

    @BindView(R2.id.iv_close)
    ImageView mIvClose;
    @BindView(R2.id.ll_download)
    LinearLayout mLlDownload;
    @BindView(R2.id.ll_share)
    LinearLayout mLlShare;
    private static String mUrl;

    public static ImageDownLoadDialog getInstance(String url) {
        ImageDownLoadDialog recFragment = new ImageDownLoadDialog();
        mUrl=url;
        return recFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        View inflate = inflater.inflate(R.layout.image_download_layout, null);
        ButterKnife.bind(this, inflate);
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
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height =WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    @OnClick({R2.id.iv_close, R2.id.ll_call, R.id.ll_download, R.id.ll_share})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_close) {
            getDialog().dismiss();
        } else if (i == R.id.ll_download) {
            if(mUrl.startsWith("http")){
                Toast.makeText(getContext(), "Download...", Toast.LENGTH_SHORT).show();
                DownLoadUtils.download(getContext(), mUrl, AppConstant.IMAGEPATH,false);
            }else {
                ToastUtils.showToast("无法下载");
            }
            getDialog().dismiss();
        }else if (i == R.id.ll_call) {
            getDialog().dismiss();
        }
        else if (i == R.id.ll_share) {
            if(mUrl.startsWith("http")){
                ShareDialog shareDialog=ShareDialog.getInstance(getString(R.string.title_content),mUrl,mUrl);
                shareDialog.show(getFragmentManager(),"");
            }else {
                ShareDialog shareDialog = ShareDialog.getImageInstance(mUrl, AppConstant.APP_STORE);
                shareDialog.show(getFragmentManager(),"");
            }
            getDialog().dismiss();
        }
    }
}
