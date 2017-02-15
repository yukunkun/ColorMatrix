package com.matrix.yukun.matrix;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity/* implements SplashADListener */{

    private RelativeLayout relativeLayout;
    private String appId="1105962710";
    private String adId="6000411838414184";
    private boolean conJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        getPermiss();
    }


    private void init() {
        relativeLayout = (RelativeLayout) findViewById(R.id.container);
    }

    private void getPermiss() {
        List<String> permissingList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissingList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissingList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissingList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(permissingList.size()>0){
            String[] permissings=permissingList.toArray(new String[permissingList.size()]);
            ActivityCompat.requestPermissions(SplashActivity.this,permissings,1);
        }else {
            requestAds();
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
                                    .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！且可能被强制退出登录")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
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
        new SplashAD(this, relativeLayout, appId, adId, new SplashADListener() {
            @Override
            public void onADDismissed() {
                //显示完毕
                Log.i("---ads","onADDismissed");
                conJump=true;
                forward();
            }

            @Override
            public void onNoAD(int i) {
                //加载失败
                Log.i("---ads","onNoAD:"+i);
                forward();
            }

            @Override
            public void onADPresent() {
                Log.i("---ads","onADPresent");

            }

            @Override
            public void onADClicked() {
                Log.i("---ads","onADClicked");

            }

            @Override
            public void onADTick(long l) {
                Log.i("---ads","onADTick");
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
        if(conJump){
            forward();
        }
        conJump=true;
    }

    private void forward() {
        Log.i("---conJump",conJump+"");
        if(conJump){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            conJump=false;
        }
    }



}
