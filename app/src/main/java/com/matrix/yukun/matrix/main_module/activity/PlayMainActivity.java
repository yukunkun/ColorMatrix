package com.matrix.yukun.matrix.main_module.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.IdRes;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.dialog.GestureDialog;
import com.matrix.yukun.matrix.main_module.entity.EventVideo;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;
import com.matrix.yukun.matrix.main_module.fragment.AboutUsFragment;
import com.matrix.yukun.matrix.main_module.fragment.CircleFragment;
import com.matrix.yukun.matrix.main_module.fragment.GaiaFragment;
import com.matrix.yukun.matrix.main_module.fragment.PlayFragment;
import com.matrix.yukun.matrix.main_module.fragment.ToolFragment;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.mine_module.entity.EventClose;
import com.matrix.yukun.matrix.selfview.floatingview.FloatingViewManager;
import com.matrix.yukun.matrix.util.StatusBarUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayMainActivity extends BaseActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    @BindView(R.id.rg)
    RadioGroup mRg;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.gaia)
    RadioButton gaia;
    private List<Fragment> mFragments = new ArrayList<>();
    private int lastPos = 0;
    private boolean isNight;
    private Button mBtColloct;
    private ToolFragment mToolFragment;
    private VideoView mVideoView;
    private ImageView mCloseVideo;
    private ImageView mPlayVideo;
    private String mNextUrl;
    private EyesInfo mEyesInfo;
    private RelativeLayout mLayout;
    private GaiaFragment mGaiaFragment;
    private PlayFragment mPlayFragment;
    private AboutUsFragment mAboutUsFragment;
    private CircleFragment mCircleFragment;

    public static void start(Context context) {
        Intent intent = new Intent(context, PlayMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Window window = getWindow();
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        return R.layout.activity_play_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Beta.checkUpgrade();
        EventBus.getDefault().register(this);
        if (savedInstanceState != null) {
            //不为空说明缓存视图中有fragment实例，通过tag取出来
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            mPlayFragment = (PlayFragment) supportFragmentManager.findFragmentByTag("0");
            mGaiaFragment = (GaiaFragment) supportFragmentManager.findFragmentByTag("1");
            mCircleFragment = (CircleFragment) supportFragmentManager.findFragmentByTag("2");
            mToolFragment = (ToolFragment) supportFragmentManager.findFragmentByTag("3");
            mAboutUsFragment = (AboutUsFragment) supportFragmentManager.findFragmentByTag("4");
        }else{
            mPlayFragment = PlayFragment.getInstance();
            mGaiaFragment = GaiaFragment.getInstance();
            mCircleFragment = CircleFragment.getInstance();
            mToolFragment = ToolFragment.getInstance();
            mAboutUsFragment = AboutUsFragment.getInstance();
        }

        mFragments.add(mPlayFragment);
        mFragments.add(mGaiaFragment);
        mFragments.add(mCircleFragment);
        mFragments.add(mToolFragment);
        mFragments.add(mAboutUsFragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_layout, mPlayFragment,"0");
        fragmentTransaction.commit();
        if(!SPUtils.getInstance().getBoolean("show_gaia")){
            gaia.setVisibility(View.GONE);
        }
        mBtColloct = (Button) findViewById(R.id.collect);
        ((RadioButton) (mRg.getChildAt(0))).setChecked(true);
        setListener();

        boolean isFirst = SPUtils.getInstance().getBoolean("first");
        if (!isFirst) {
            GestureDialog gestureDialog = GestureDialog.getInstance();
            gestureDialog.show(getSupportFragmentManager(), "");
            SPUtils.getInstance().saveBoolean("first",true);
        }
    }

    @Override
    public void initView() {
    }

    private void setListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.index) {
                    ((RadioButton) (mRg.getChildAt(0))).setChecked(true);
                    bottomViewAnimation(mRg.getChildAt(0));
                    show(0);
                } else if (checkedId == R.id.gaia) {
                    ((RadioButton) (mRg.getChildAt(1))).setChecked(true);
                    bottomViewAnimation(mRg.getChildAt(1));
                    show(1);

                } else if (checkedId == R.id.chat) {
                    ((RadioButton) (mRg.getChildAt(2))).setChecked(true);
                    bottomViewAnimation(mRg.getChildAt(2));
                    show(2);

                } else if (checkedId == R.id.collect) {
                    ((RadioButton) (mRg.getChildAt(3))).setChecked(true);
                    bottomViewAnimation(mRg.getChildAt(3));
                    show(3);

                } else if (checkedId == R.id.me) {
                    ((RadioButton) (mRg.getChildAt(4))).setChecked(true);
                    bottomViewAnimation(mRg.getChildAt(4));
                    show(4);
                }
            }
        });
    }

    public void bottomViewAnimation(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);//动画持续时间
        animationSet.addAnimation(scaleAnimation);//保存动画效果到。。
        animationSet.setFillAfter(false);//结束后保存状态
        view.startAnimation(animationSet);//设置给控件
    }

    public void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //  将是否为夜间模式保存到SharedPreferences
        if (currentNightMode == 32) {
            isNight = true;
        } else {
            isNight = false;
        }

        getDelegate().setDefaultNightMode(isNight ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        //  重启Activity
        recreate();
        //延迟导致重启一个destory的Activity，会失败。
        saveModule(isNight);
    }

    private void saveModule(boolean isNight) {
        SharedPreferences sharedPreferences = getSharedPreferences("module", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean("isNight", isNight);
        editor.commit();//提交修改
    }

    /**
     * fragment 的show和hide
     *
     * @param pos
     */
    public void show(int pos) {
        Fragment fragment = mFragments.get(pos);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(mFragments.get(lastPos));

        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fl_layout, fragment,pos+"");
        }
        fragmentTransaction.commit();
        lastPos = pos;
    }

    private void showSnackbar() {
        Snackbar.make(findViewById(R.id.rl), "亲，再留一会儿吧", Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.color_00ff00))
                .setAction("离开", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showSnackbar();
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateVideo(EventVideo eventVideo) {
        mEyesInfo = eventVideo.mEyesInfo;
        mNextUrl = eventVideo.mNextUrl;
        if (mEyesInfo.getData().getPlayUrl() != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    View view = LayoutInflater.from(this).inflate(R.layout.float_video_item, null);
                    mVideoView = view.findViewById(R.id.jzps_player);
                    mCloseVideo = view.findViewById(R.id.iv_close_video);
                    mPlayVideo = view.findViewById(R.id.iv_play_video);
                    mLayout = view.findViewById(R.id.rl_con);
                    mPlayVideo.setOnClickListener(this);
                    mCloseVideo.setOnClickListener(this);
                    mLayout.setOnClickListener(this);
                    mVideoView.setOnClickListener(this);
                    mVideoView.setVideoURI(Uri.parse(mEyesInfo.getData().getPlayUrl()));
                    mVideoView.setOnPreparedListener(this);
                    FloatingViewManager.Configs configs = new FloatingViewManager.Configs();
                    configs.floatingViewX = ScreenUtil.getDisplayWidth();   // 设置悬浮窗的X坐标
                    configs.floatingViewY = ScreenUtil.getDisplayHeight() - ScreenUtils.dp2Px(this, 200);  // 设置悬浮窗的Y坐标
                    configs.overMargin = -ScreenUtil.dip2px(8); // 设置悬浮窗距离边缘的外边距
                    FloatingViewManager.getInstance(this).addFloatingView(view, configs);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(EventClose eventClose) {
        LogUtil.i("===========", eventClose.isNight + "");
        MyApp.updateNight();
        finish();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    private void updatePlayButton(boolean playing) {
        if (playing) {
            mPlayVideo.setImageResource(R.mipmap.icon_video_pause);
            mVideoView.pause();
        } else {
            mPlayVideo.setImageResource(R.mipmap.icon_video_play);
            mVideoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_video:
                FloatingViewManager.getInstance(this).removeFloatingView();
                break;
            case R.id.iv_play_video:
                updatePlayButton(mVideoView.isPlaying());
                break;
            case R.id.rl_con:
                VideoDetailPlayActivity.start(this, mEyesInfo, mNextUrl, mVideoView);
                break;
        }
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
