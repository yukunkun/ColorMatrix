package com.matrix.yukun.matrix.desk_module.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.desk_module.view.ClockNormalView;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClockNormalActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_internet)
    TextView tvInternet;
    @BindView(R.id.clock_normal)
    ClockNormalView clockNormal;

    public static void start(Context context) {
        Intent intent = new Intent(context, ClockNormalActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_clock_normal;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
