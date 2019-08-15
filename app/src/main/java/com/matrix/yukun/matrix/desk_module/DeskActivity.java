package com.matrix.yukun.matrix.desk_module;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.desk_module.activity.ClockNormalActivity;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeskActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_internet)
    TextView tvInternet;
    @BindView(R.id.bt_normal)
    Button btNormal;

    @Override
    public int getLayout() {
        return R.layout.activity_desk;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.iv_back, R.id.bt_normal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_normal:
                ClockNormalActivity.start(this);
                break;
        }
    }
}
