package com.ykk.pluglin_video;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyf.yflibrary.Permission;
import com.example.lyf.yflibrary.PermissionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class LoadingActivity extends BaseActivity {

    @BindView(R2.id.iv_bg)
    ImageView mIvBg;
    @BindView(R2.id.tv_remind)
    TextView mTvRemind;

    @Override
    public int getLayout() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_loading;
    }

    @Override
    public void initView() {
        List<Integer> mImages = new ArrayList<>();
        mImages.add(R.drawable.bg_1);
        mImages.add(R.drawable.bg_2);
        mImages.add(R.drawable.bg_3);
        mImages.add(R.drawable.bg_4);
        mImages.add(R.drawable.bg_5);
        mImages.add(R.drawable.bg_6);
        mImages.add(R.drawable.bg_7);
        mImages.add(R.drawable.bg_8);
        Random random = new Random();
        int ran = random.nextInt(8);
        mIvBg.setBackgroundResource(mImages.get(ran));
        String[] REQUEST_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permission.checkPermisson(this, REQUEST_PERMISSIONS, new PermissionResult() {

            @Override
            public void success() {
                startMainActyivity();
            }

            @Override
            public void fail() {
                finish();
//                new AlertDialog.Builder(LoadingActivity.this)
//                        .setTitle("权限申请")
//                        .setMessage("请给予权限才能使用")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                   finish();
//                            }
//                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                }).show();
            }
        });
        rotateyAnimSet(mTvRemind,mIvBg);
    }


    /**
     * 位移动画
     *
     * @param view
     */
    public void rotateyAnimSet(View view,View viewScale) {
        ObjectAnimator//
                .ofFloat(view, "alpha", 0F, 1F)//
                .setDuration(1500)//
                .start();

        ObjectAnimator
                .ofFloat(view, "translationY", 360F, 0F)//
                .setDuration(3000)//
                .start();
        //放大
        ScaleAnimation animation = new ScaleAnimation(
                1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(4000);
        viewScale.startAnimation(animation);
    }

    public void startMainActyivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Intent intent = new Intent(LoadingActivity.this, ChooseActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
