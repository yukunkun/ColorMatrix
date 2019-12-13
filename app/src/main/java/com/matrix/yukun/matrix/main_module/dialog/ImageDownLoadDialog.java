package com.matrix.yukun.matrix.main_module.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.DownLoadUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yukun on 17-12-26.
 */

public class ImageDownLoadDialog extends DialogFragment {

    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.ll_download)
    RelativeLayout mLlDownload;
    @BindView(R.id.ll_share)
    RelativeLayout mLlShare;
    @BindView(R.id.ll_copy)
    RelativeLayout mLlCopy;
    @BindView(R.id.ll_internet)
    RelativeLayout mLlInternet;
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

    @OnClick({R.id.iv_close, R.id.ll_call, R.id.ll_download, R.id.ll_share,R.id.ll_copy,R.id.ll_internet})
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
        }else if (i == R.id.ll_copy) {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) ((getActivity()).getSystemService(Context.CLIPBOARD_SERVICE));
            //创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", mUrl);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            ToastUtils.showToast(getString(R.string.copy_to_save));
            getDialog().dismiss();
        }else if (i == R.id.ll_internet) {
            Uri uri = Uri.parse(mUrl);//要跳转的网址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            getDialog().dismiss();
        }
    }
}
