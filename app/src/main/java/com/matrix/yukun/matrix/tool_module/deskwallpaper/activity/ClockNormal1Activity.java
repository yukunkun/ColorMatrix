package com.matrix.yukun.matrix.tool_module.deskwallpaper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.deskwallpaper.view.ClockNormalView1;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class ClockNormal1Activity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_internet)
    TextView tvInternet;
    @BindView(R.id.clock_normal1)
    ClockNormalView1 clockNormal;
    private Timer mTimer;

    public static void start(Context context) {
        Intent intent = new Intent(context, ClockNormal1Activity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_clock_normal1;
    }

    @Override
    public void initView() {
        mTimer =new  Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        clockNormal.doInvalidate();
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

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
