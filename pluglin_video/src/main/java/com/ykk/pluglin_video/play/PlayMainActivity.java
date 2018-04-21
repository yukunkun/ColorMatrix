package com.ykk.pluglin_video.play;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.fragment.AboutUsFragment;
import com.ykk.pluglin_video.fragment.MyCollectFragment;
import com.ykk.pluglin_video.fragment.PlayFragment;
import com.ykk.pluglin_video.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayMainActivity extends BaseActivity {

    @BindView(R2.id.rg)
    RadioGroup mRg;
    private List<Fragment> mFragments=new ArrayList<>();
    private int lastPos=0;
    private boolean isNight;

    @Override
    public int getLayout() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Window window = getWindow();
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
        return R.layout.activity_play_main;
    }

    @Override
    public void initView() {
        PlayFragment playFragment=PlayFragment.getInstance();
        mFragments.add(playFragment);
        MyCollectFragment myCollectFragment= MyCollectFragment.getInstance();
        mFragments.add(myCollectFragment);
        AboutUsFragment aboutUsFragment=AboutUsFragment.getInstance();
        mFragments.add(aboutUsFragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_layout, playFragment);
        fragmentTransaction.commit();

        ((RadioButton) (mRg.getChildAt(0))).setChecked(true);
        setListener();
    }

    private void setListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.index) {
                    ((RadioButton) (mRg.getChildAt(0))).setChecked(true);
                    show(0);

                } else if (checkedId == R.id.collect) {
                    ((RadioButton) (mRg.getChildAt(1))).setChecked(true);
                    show(1);

                } else if (checkedId == R.id.me) {
                    ((RadioButton) (mRg.getChildAt(2))).setChecked(true);
                    show(2);

                }
            }
        });
    }

        public void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //  将是否为夜间模式保存到SharedPreferences
        if(currentNightMode==32){
            isNight=true;
        }else {
            isNight=false;
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
     * @param pos
     */
    public void show(int pos) {
        Fragment fragment = mFragments.get(pos);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(mFragments.get(lastPos));

        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fl_layout, fragment);
        }
        fragmentTransaction.commit();
        lastPos = pos;
    }

}
