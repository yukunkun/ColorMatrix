package com.matrix.yukun.matrix.mine_module.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.mine_module.entity.WebType;
import com.matrix.yukun.matrix.mine_module.view.FankuiDialog;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.cb_play)
    CheckBox mCbPlay;
    @BindView(R.id.cb_brief)
    CheckBox mCbBrief;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @Override
    public int getLayout() {
        return R.layout.activity_video_setting;
    }

    @Override
    public void initView() {
        mTvVersion.setText("版本号("+getVersion()+")");
        OverScrollDecoratorHelper.setUpOverScroll(mScrollView);
        mCbPlay.setChecked(SPUtils.getInstance().getBoolean("iswifi"));
        mCbBrief.setChecked(SPUtils.getInstance().getBoolean("isbrief"));

    }

    @Override
    public void initListener() {
        mCbPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SPUtils.getInstance().saveBoolean("iswifi",isChecked);
                }else {
                    SPUtils.getInstance().saveBoolean("iswifi",isChecked);
                }
            }
        });
        mCbBrief.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ToastUtils.showToast("重新打开APP将是简版应用");
                    SPUtils.getInstance().saveBoolean("isbrief",isChecked);
                }else {
                    ToastUtils.showToast("取消简版应用");
                    SPUtils.getInstance().saveBoolean("isbrief",isChecked);
                }
            }
        });
    }

    @OnClick({R2.id.iv_back, R2.id.tv_clear, R2.id.tv_update, R2.id.tv_introduce, R2.id.tv_about, R2.id.tv_sugg, R2.id.tv_mark_update})
    public void onClick(View view) {
        int id = view.getId();
        Class aClass=null;
        if(id== R.id.iv_back){
            finish();
            return;
        }else if(id== R.id.tv_clear){
            ToastUtils.showToast("清除完成");
            return;
        }else if(id== R.id.tv_update){
            Beta.checkUpgrade();
            return;
        }else if(id== R.id.tv_introduce){
            ResponsbilityActivity.start(this, WebType.INTRODUCE.getType());
            return;
        }else if(id== R.id.tv_about){
            ResponsbilityActivity.start(this, WebType.AGREEMENT.getType());
            return;
        }
        else if(id== R.id.tv_sugg){
            FankuiDialog noteCommentDialog = FankuiDialog.newInstance(0);
            noteCommentDialog.show(getSupportFragmentManager(), "SettingActivity");
            return;
        }
        else if(id== R.id.tv_mark_update){
            Uri uri = Uri.parse(AppConstant.APP_STORE);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return;
        }
        Intent intent=new Intent(this,aClass);
        startActivity(intent);
    }

    private String getVersion() {
        String mVersionName=null;
        try {
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            mVersionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mVersionName;
    }
}
