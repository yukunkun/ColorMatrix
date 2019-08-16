package com.matrix.yukun.matrix.desk_module.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.desk_module.view.ClockRectView;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class ClockRectActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_internet)
    TextView tvInternet;
    @BindView(R.id.clock_rect)
    ClockRectView clockRect;
    private Timer mTimer;

    public static void start(Context context) {
        Intent intent = new Intent(context, ClockRectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_clock_rect;
    }

    @Override
    public void initView() {
        mTimer =new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        clockRect.doInvalidate();
                    }
                });
            }
        },1000,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @OnClick({R.id.iv_back, R.id.clock_rect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.clock_rect:
                break;
        }
    }

}
