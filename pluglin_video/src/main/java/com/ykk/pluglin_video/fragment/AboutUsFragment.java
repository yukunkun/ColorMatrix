package com.ykk.pluglin_video.fragment;

import android.animation.ObjectAnimator;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ykk.pluglin_video.BaseFragment;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsFragment extends BaseFragment {

    @BindView(R2.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R2.id.tv_version)
    TextView mTvVersion;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;

    public static AboutUsFragment getInstance(){
        return new AboutUsFragment();
    }
    @Override
    public int getLayout() {
        return R.layout.activity_about_us_video;
    }


    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        try {
            PackageManager pm = getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            String versionName = pi.versionName;
            mTvVersion.setText("V " + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startAmim(mIvIcon);
        mIvBack.setVisibility(View.GONE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
            }
        });
    }

    private void startAmim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", 360, 0);
        animator.setDuration(4000);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    @OnClick(R2.id.iv_back)
    public void onClick() {

    }

}
