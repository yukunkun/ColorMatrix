package com.matrix.yukun.matrix.main_module;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.search.DBSearchInfo;
import com.matrix.yukun.matrix.util.PermissionUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.matrix.yukun.matrix.video_module.entity.UserInfo;
import com.matrix.yukun.matrix.video_module.play.BriefVersionActivity;
import com.matrix.yukun.matrix.video_module.play.PlayMainActivity;
import com.matrix.yukun.matrix.video_module.utils.SPUtils;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity/* implements SplashADListener */{

    private RelativeLayout relativeLayout;
    private String appId="1105962710";
    private String adId="6000411838414184";
    private boolean conJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        getPermiss();
    }

    private void init() {
        relativeLayout = (RelativeLayout) findViewById(R.id.container);
        //删除100天以前的历史数据
        DataSupport.deleteAllAsync(DBSearchInfo.class,"timeStamp < ? ", System.currentTimeMillis()-100*24*60*60*1000+"");
    }

    private void getPermiss() {
        final List<String> permissingList = new ArrayList<String>();
        permissingList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissingList.add(Manifest.permission.CAMERA);
        permissingList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissingList.add(Manifest.permission.READ_PHONE_STATE);
        permissingList.add(Manifest.permission.READ_CONTACTS);
        permissingList.add(Manifest.permission.RECORD_AUDIO);
        final PermissionUtils permissionUtils = PermissionUtils.getInstance(this);
        List<String> list = permissionUtils.setPermission(permissingList);
        if(list.size()==0){
            requestAds();
        }else {
            permissionUtils.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 ) {
                    //6.0权限访问
                    for (int result : grantResults) {
                        if(result!= PackageManager.PERMISSION_GRANTED){
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

    //光告接入
    private void requestAds() {
        //获取到userinfo
        List<UserInfo> all = DataSupport.findAll(UserInfo.class);
        if(all.size()>0){
            MyApplication.setUserInfo(all.get(0));
        }
        new SplashAD(this, relativeLayout, appId, adId, new SplashADListener() {
            @Override
            public void onADDismissed() {
                //显示完毕
                LogUtil.i("---ads","onADDismissed");
                conJump=true;
                forward();
            }


            @Override
            public void onNoAD(AdError adError) {
                //加载失败
                LogUtil.i("---adError",adError.getErrorMsg());
                conJump=true;
                forward();
            }

            @Override
            public void onADPresent() {
                LogUtil.i("---ads","onADPresent");
            }

            @Override
            public void onADClicked() {
                LogUtil.i("---ads","onADClicked");
            }

            @Override
            public void onADTick(long l) {
                Log.i("---ads","onADTick");
            }

            @Override
            public void onADExposure() {

            }
        },0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        conJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void forward() {
        if(conJump){
            Intent intent;
            if(!isFace().equals("a")){
                intent=new Intent(this,FaceActivity.class);
            }else if(istrue()){
                intent=new Intent(this,LockActivity.class);
            }else {
                if(SPUtils.getInstance().getBoolean("isbrief")){
                    intent=new Intent(this, BriefVersionActivity.class);
                }else {
                    intent=new Intent(this, PlayMainActivity.class);
                }
            }
            startActivity(intent);
            finish();
        }
    }

    private boolean istrue(){
        SharedPreferences preferences=getSharedPreferences("gesture", Context.MODE_PRIVATE);
        boolean result = preferences.getBoolean("gesture",false);
        return result;
    }

    private String isFace(){
        SharedPreferences preferences=getSharedPreferences("mAuthId", Context.MODE_PRIVATE);
        String result = preferences.getString("mAuthId","a");
        return result;
    }
}
