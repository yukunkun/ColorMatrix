package com.matrix.yukun.matrix.mine_module.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.MainActivity;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.mine_module.entity.EventClose;
import com.matrix.yukun.matrix.mine_module.entity.WebType;
import com.matrix.yukun.matrix.mine_module.view.FankuiDialog;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.tv_secret)
    TextView mTvSecret;
    @BindView(R.id.cb_play)
    CheckBox mCbPlay;
    @BindView(R.id.cb_brief)
    CheckBox mCbBrief;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.cb_night)
    CheckBox cbNight;
    private boolean isNight;
    private boolean lastModule;
    private boolean currentModule;
    private int count;
    @Override
    public int getLayout() {
        return R.layout.activity_video_setting;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        mTvVersion.setText("版本号(" + getVersion() + ")");
        OverScrollDecoratorHelper.setUpOverScroll(mScrollView);
        mCbPlay.setChecked(SPUtils.getInstance().getBoolean("iswifi"));
        mCbBrief.setChecked(SPUtils.getInstance().getBoolean("isbrief"));
        lastModule = MyApp.getNight();
        currentModule=SPUtils.getInstance().getBoolean("isNight");
        cbNight.setChecked(currentModule);
    }

    @Override
    public void initListener() {
        mCbPlay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SPUtils.getInstance().saveBoolean("iswifi", isChecked);
            } else {
                SPUtils.getInstance().saveBoolean("iswifi", isChecked);
            }
        });
        mCbBrief.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ToastUtils.showToast("重新打开APP将是简版应用");
                SPUtils.getInstance().saveBoolean("isbrief", isChecked);
            } else {
                ToastUtils.showToast("取消简版应用");
                SPUtils.getInstance().saveBoolean("isbrief", isChecked);
            }
        });

        cbNight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setNightMode();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        if (lastModule!=currentModule) {  //  如果改变了夜间模式，则重启MainActivity
            EventBus.getDefault().post(new EventClose(currentModule));
            MainActivity.start(this);
        }
        finish();
    }

    @OnClick({R.id.iv_back, R.id.tv_clear, R.id.tv_update, R.id.tv_introduce, R.id.tv_about, R.id.tv_sugg, R.id.tv_mark_update,R.id.tv_version,R.id.tv_secret})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            goBack();
            return;
        } else if (id == R.id.tv_clear) {
            ToastUtils.showToast("清除完成");
            return;
        } else if (id == R.id.tv_update) {
            Beta.checkUpgrade();
            return;
        } else if (id == R.id.tv_introduce) {
            ResponsbilityActivity.start(this, WebType.INTRODUCE.getType());
            return;
        } else if (id == R.id.tv_about) {
            ResponsbilityActivity.start(this, WebType.AGREEMENT.getType());
            return;
        } else if (id == R.id.tv_sugg) {
            FankuiDialog noteCommentDialog = FankuiDialog.newInstance(0);
            noteCommentDialog.show(getSupportFragmentManager(), "SettingActivity");
            return;
        } else if (id == R.id.tv_mark_update) {
                Beta.checkUpgrade();
//            Uri uri = Uri.parse(AppConstant.APP_STORE);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
            return;
        }else if(id == R.id.tv_secret){
            ResponsbilityActivity.start(this, WebType.SECRET.getType());
        } else if (id == R.id.tv_version) {
            if(count<10){
                count++;
            }else {
                count=0;
            }
            if(count==10){
                boolean show_gaia = SPUtils.getInstance().getBoolean("show_gaia");
                ToastUtils.showToast("show_gaia:"+!show_gaia);
                SPUtils.getInstance().saveBoolean("show_gaia",!show_gaia);
            }
            return;
        }
    }

    private String getVersion() {
        String mVersionName = null;
        try {
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            mVersionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mVersionName;
    }

    public void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == 32) {
            isNight = true;
        } else {
            isNight = false;
        }
        currentModule=!isNight;
        getDelegate().setDefaultNightMode(isNight ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        saveModule(currentModule);
        //  重启Activity
        finish();
        startActivity(new Intent( this, this.getClass()));
        overridePendingTransition(0, 0);
    }

    private void saveModule(boolean isNight) {
        SPUtils.getInstance().saveBoolean("isNight",isNight);
    }
}
