package com.matrix.yukun.matrix.tool_module.calarder.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.tool_module.calarder.contant.CalandarBean;
import com.matrix.yukun.matrix.tool_module.calarder.contant.TypeBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/5.
 */

public class CalandarDetailFragment extends DialogFragment {
    CalandarBean calandarBean;
    @BindView(R.id.tv_categray)
    TextView mTvCategray;
    @BindView(R.id.tv_default)
    TextView mTvDefault;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_title)
    TextView mEtTitle;
    @BindView(R.id.tvs_time)
    TextView mTvsTime;
    @BindView(R.id.tv_real_time)
    TextView mTvRealTime;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_please)
    TextView mTvPlease;
    @BindView(R.id.tvs_please)
    TextView mTvsPlease;
    @BindView(R.id.et_content)
    TextView mEtContent;


    public static CalandarDetailFragment getInstance(CalandarBean calandarBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("calandar", calandarBean);
        CalandarDetailFragment calandarDetailFragment = new CalandarDetailFragment();
        calandarDetailFragment.setArguments(bundle);
        return calandarDetailFragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        View view = inflater.from(getContext()).inflate(R.layout.calandar_detail_dialog, null);
        ButterKnife.bind(this, view);
        calandarBean = (CalandarBean) getArguments().getSerializable("calandar");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(200, 0, 0, 0)));//设置背景透明
        updateView();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateView() {
        mEtTitle.setText(calandarBean.getCalandarTitle());
        mTvTime.setText(calandarBean.getCreateTime());
        mTvsPlease.setText(calandarBean.getPleace());
        mEtContent.setText(calandarBean.getCalandarContent());
        if (calandarBean.getCategray() == TypeBean.TYPE_DEFEAT) {
            mTvDefault.setText(getContext().getResources().getString(R.string.moren));
        }
        if (calandarBean.getCategray() == TypeBean.TYPE_LIFE) {
            mTvDefault.setText(getContext().getResources().getString(R.string.life));
        }
        if (calandarBean.getCategray() == TypeBean.TYPE_WORK) {
            mTvDefault.setText(getContext().getResources().getString(R.string.work));
        }
        if (calandarBean.getCategray() == TypeBean.TYPE_OTHER) {
            mTvDefault.setText(getContext().getResources().getString(R.string.other));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            // 设置弹出框布局参数，宽度铺满全屏，底部。
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = (int) (ScreenUtils.instance().getWidth(getContext()) * 0.8);
            wlp.height = (int) (ScreenUtils.instance().getHeight(getContext()) * 0.6);
            window.setAttributes(wlp);
        }
    }
}
