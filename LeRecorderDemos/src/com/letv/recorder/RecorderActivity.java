package com.letv.recorder;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.le.skin.PublisherSkinView;
import com.le.skin.ui.SkinParams;
import com.letv.recorder.util.LeLog;

/**
 * 移动直播推流界面
 * 在移动直播中，推流器只认识推流地址。
 * 这个Activity 需要用户传入三个值 isVertical 是否竖屏录制、streamUrl 推流地址、pushName 推流名称
 */
public class RecorderActivity extends Activity {
    private boolean isVertical;

    private String pushSteamUrl;
    private String pushName;
    private PublisherSkinView skinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isVertical = getIntent().getBooleanExtra("isVertical", false);
        pushSteamUrl = getIntent().getStringExtra("streamUrl");
        pushName = getIntent().getStringExtra("pushName");
        if (TextUtils.isEmpty(pushSteamUrl)) {
            Toast.makeText(this, "不能传入空的推流地址", Toast.LENGTH_SHORT).show();
        }
        LeLog.w("推流地址是:" + pushSteamUrl);
        setContentView(R.layout.activity_stream_recorder);
        skinView = (PublisherSkinView) findViewById(R.id.psv_stream_recorder);
        SkinParams params = skinView.getSkinParams();
        params.setLanscape(!isVertical);
        skinView.initPublish(pushSteamUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        skinView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        skinView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        skinView.onDestroy();
    }
}
