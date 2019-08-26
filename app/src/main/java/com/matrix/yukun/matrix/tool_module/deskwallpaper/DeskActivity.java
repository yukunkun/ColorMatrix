package com.matrix.yukun.matrix.tool_module.deskwallpaper;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.mine_module.activity.ResponsbilityActivity;
import com.matrix.yukun.matrix.mine_module.entity.WebType;
import com.matrix.yukun.matrix.tool_module.deskwallpaper.activity.ClockNormal1Activity;
import com.matrix.yukun.matrix.tool_module.deskwallpaper.activity.ClockNormalActivity;
import com.matrix.yukun.matrix.tool_module.deskwallpaper.activity.ClockRectActivity;
import com.matrix.yukun.matrix.tool_module.deskwallpaper.activity.WallpaperActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class DeskActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bt_normal)
    Button btNormal;
    @BindView(R.id.iv_quest)
    ImageView ivQuest;
    @BindView(R.id.tv_wall)
    TextView tvWall;

    @Override
    public int getLayout() {
        return R.layout.activity_desk;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.iv_back, R.id.bt_normal, R.id.bt_rect, R.id.bt_normal1, R.id.tv_wall, R.id.bt_roata, R.id.iv_quest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_quest:
                ResponsbilityActivity.start(this, WebType.QUESTION.getType());
                break;
            case R.id.bt_normal:
                ClockNormalActivity.start(this);
                break;
            case R.id.bt_normal1:
                ClockNormal1Activity.start(this);
                break;
            case R.id.bt_rect:
                ClockRectActivity.start(this);
                break;
            case R.id.bt_roata:
                WallpaperActivity.start(this);
                break;
            case R.id.tv_wall:
                Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
                Intent chooser = Intent.createChooser(pickWallpaper, "选择壁纸");
                startActivity(chooser);
                break;

        }
    }

}
