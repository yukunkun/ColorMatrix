package com.matrix.yukun.matrix.main_module.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;

import com.google.gson.Gson;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.BriefVersionActivity;
import com.matrix.yukun.matrix.main_module.activity.LoginActivity;
import com.matrix.yukun.matrix.main_module.activity.MainActivity;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.search.DBSearchInfo;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.util.PermissionUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.skip_view)
    TextView skipView;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.title)
    TextView title;

    @Override
    public int getLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        getPermiss();
        //删除100天以前的历史数据
        DataSupport.deleteAllAsync(DBSearchInfo.class, "timeStamp < ? ", System.currentTimeMillis() - 100 * 24 * 60 * 60 * 1000 + "");
        if (MyApp.getNight()) {
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        animation();
        isAutoLogin();
    }

    private void animation() {
        ViewCompat.animate(icon)
                .translationX(50)
                .setDuration(500).setInterpolator(new DecelerateInterpolator(1.2f))
                .start();
    }

    private void getPermiss() {
        final List<String> permissingList = new ArrayList<String>();
        permissingList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissingList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissingList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissingList.add(Manifest.permission.READ_PHONE_STATE);
        permissingList.add(Manifest.permission.READ_CONTACTS);
        permissingList.add(Manifest.permission.RECORD_AUDIO);
        permissingList.add(Manifest.permission.CAMERA);

        PermissionUtils permissionUtils = PermissionUtils.getInstance();
        permissionUtils.setContext(this);
        List<String> list = permissionUtils.setPermission(permissingList);
        if (list.size() == 0) {
            forward();
        } else {
            permissionUtils.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    //6.0权限访问
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            AlertDialog dialog = new AlertDialog.Builder(this)
                                    .setMessage("需要赋予权限，不开启将无法正常工作！且可能被强制退出登录")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            finish();
                                            requestAds();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    }
                }
                requestAds();
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void requestAds() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void forward() {
        Intent intent = null;
        if (!SPUtils.getInstance().getBoolean("first") && TextUtils.isEmpty(SPUtils.getInstance().getString("user"))) {
            intent = new Intent(this, LoginActivity.class);
        } else if (istrue()) {
            intent = new Intent(this, LockActivity.class);
        } else {
            if (SPUtils.getInstance().getBoolean("isbrief")) {
                intent = new Intent(this, BriefVersionActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }
        }
        startActivity(intent);
        finish();

    }

    private boolean istrue() {
        SharedPreferences preferences = getSharedPreferences("gesture", Context.MODE_PRIVATE);
        boolean result = preferences.getBoolean("gesture", false);
        return result;
    }

    private void isAutoLogin() {
        String user = SPUtils.getInstance().getString("user");
        if(!TextUtils.isEmpty(user)){
            Gson gson=new Gson();
            UserInfoBMob userInfoBMob = gson.fromJson(user, UserInfoBMob.class);
            if(userInfoBMob!=null){
                LogUtil.i(userInfoBMob.toString());
                MyApp.setUserInfo(userInfoBMob);
            }
        }
    }

}
