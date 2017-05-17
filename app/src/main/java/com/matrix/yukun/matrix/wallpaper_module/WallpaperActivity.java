package com.matrix.yukun.matrix.wallpaper_module;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {

    private Context mContext;
    private ViewPager mViewPager;
    private List<Integer> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        mContext = this;
        findViewById(R.id.bt_sure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkSelfPermission();
                    }
                });
        setInfo();
    }

    private void setInfo() {
        mList = new ArrayList<>();
        mList.add(R.drawable.tou1);
        mList.add(R.drawable.tou2);
        mList.add(R.drawable.tou3);
        mList.add(R.drawable.tou4);
        mList.add(R.drawable.tou5);
        mViewPager = (ViewPager) findViewById(R.id.viewpagers);
        PagerAdapters pagerAdapters=new PagerAdapters(this, mList);
        mViewPager.setAdapter(pagerAdapters);
        mViewPager.setPageTransformer(true,new DepthpagerTransformer());
    }

    /**
     * 检查权限
     */
    void checkSelfPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
        } else {
//            setTransparentWallpaper();
            startWallpaper();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    setTransparentWallpaper();
                    startWallpaper();

                } else {
                    Toast.makeText(mContext, "Please open permissions", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    /**
     * 选择壁纸
     */
    void startWallpaper() {
        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
        Intent chooser = Intent.createChooser(pickWallpaper, "选择壁纸");
        startActivity(chooser);
    }

    /**
     * 不需要手动启动服务
     */
    void setTransparentWallpaper() {
        startService(new Intent(mContext, CameraLiveWallpaper.class));
    }

    public void WallBack(View view) {
        finish();
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }
}
