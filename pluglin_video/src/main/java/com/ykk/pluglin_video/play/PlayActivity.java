package com.ykk.pluglin_video.play;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.entity.EventCategrayPos;
import com.ykk.pluglin_video.fragment.ImageFragment;
import com.ykk.pluglin_video.fragment.JokeFragment;
import com.ykk.pluglin_video.fragment.RecFragment;
import com.ykk.pluglin_video.fragment.SettingFragment;
import com.ykk.pluglin_video.fragment.SpecialTxtFragment;
import com.ykk.pluglin_video.fragment.TextFragment;
import com.ykk.pluglin_video.fragment.VideoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class PlayActivity extends BaseActivity {

    @BindView(R2.id.iv_main)
    ImageView mIvMain;
    @BindView(R2.id.rl_contain)
    RelativeLayout mRlContain;
    @BindView(R2.id.tablayout)
    TabLayout mTablayout;
    @BindView(R2.id.drawlayout)
    DrawerLayout mDrawlayout;
    @BindView(R2.id.iv_close)
    ImageView mIvClose;
    @BindView(R2.id.viewpager)
    ViewPager mViewpager;
    @BindView(R2.id.head)
    CircleImageView mHead;
    @BindView(R2.id.im_snow)
    ImageView mImSnow;
    @BindView(R2.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R2.id.im_bird)
    ImageView mImBird;
    @BindView(R2.id.rl_movie)
    RelativeLayout mRlMovie;
    @BindView(R2.id.im_modu)
    ImageView mImModu;
    @BindView(R2.id.rl_change_modul)
    RelativeLayout mRlChangeModul;
    @BindView(R2.id.im_ball)
    ImageView mImBall;
    @BindView(R2.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R2.id.rl_me)
    RelativeLayout mRlMe;
    @BindView(R2.id.tv_close)
    TextView mTvClose;
    @BindView(R2.id.sc_contain)
    ScrollView mScrollview;
    private MViewPagerAdapter mMViewPagerAdapter;
    private String[] mStringArray;
    List<Fragment> mFragments = new ArrayList<>();
    private boolean isNight;
    private VideoFragment mInstance1;
    private ImageFragment mInstance3;
    private JokeFragment mInstance4;
    private TextFragment mInstance5;

    @Override
    public int getLayout() {
        return R.layout.activity_play;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mStringArray = getResources().getStringArray(R.array.title);
        for (int i = 0; i < mStringArray.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mStringArray[i]));
        }

        RecFragment instance = RecFragment.getInstance();
        mInstance1 = VideoFragment.getInstance();
        SpecialTxtFragment instance2=SpecialTxtFragment.getInstance();
        mInstance3 = ImageFragment.getInstance();
        mInstance4 = JokeFragment.getInstance();
        mInstance5 = TextFragment.getInstance();
        mFragments.add(instance);
        mFragments.add(mInstance1);
        mFragments.add(instance2);
        mFragments.add(mInstance3);
        mFragments.add(mInstance4);
        mFragments.add(mInstance5);
        setAdapter();
        setListener();
        OverScrollDecoratorHelper.setUpOverScroll(mScrollview);
    }

    private void setAdapter() {
        mMViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(), mFragments, mStringArray);
        mViewpager.setAdapter(mMViewPagerAdapter);
        mViewpager.setOffscreenPageLimit(3);
    }

    private void setListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTablayout.setScrollPosition(position, 0, true);
                if(position==0||position==2||position==3||position==4){
                    mIvSetting.setVisibility(View.GONE);
                }else {
                    mIvSetting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTablayout.setupWithViewPager(mViewpager);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventCategrayPos event) {
        /* Do something */
        if(event.pos<1000){
            mViewpager.setCurrentItem(event.pos);
        }else {
            switch (event.pos){
                //横向列表
                case 1001:
                    if(mViewpager.getCurrentItem()==1){
                        mInstance1.getLayoutTag(true);
                    }
                    if(mViewpager.getCurrentItem()==3){
                        mInstance3.getLayoutTag(true);
                    }
                    if(mViewpager.getCurrentItem()==4){
                        mInstance4.getLayoutTag(true);
                    }
                    if(mViewpager.getCurrentItem()==5){
                        mInstance5.getLayoutTag(true);
                    }
                    break;
                //格子列表
                case 1002:
                    if(mViewpager.getCurrentItem()==1){
                        mInstance1.getLayoutTag(false);
                    }
                    if(mViewpager.getCurrentItem()==3){
                        mInstance3.getLayoutTag(false);
                    }
                    if(mViewpager.getCurrentItem()==4){
                        mInstance4.getLayoutTag(false);
                    }
                    if(mViewpager.getCurrentItem()==5){
                        mInstance5.getLayoutTag(false);
                    }
                    break;
            }
        }
    }

    @OnClick({R2.id.iv_chat, R2.id.iv_setting,R2.id.iv_main,R2.id.head, R2.id.iv_close,R2.id.rl_collect,R2.id.rl_main, R2.id.rl_movie,
            R2.id.rl_change_modul, R2.id.rl_me, R2.id.tv_close})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_main) {
            if (!mDrawlayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawlayout.openDrawer(Gravity.LEFT);
            }

        } else if (i == R.id.iv_close) {
            closeDrawLayout();

        } else if (i == R.id.rl_main) {
            closeDrawLayout();
            mViewpager.setCurrentItem(0);

        } else if (i == R.id.rl_movie) {//推荐
            closeDrawLayout();
            Intent intentHis = new Intent(this, HistoryTodayActivity.class);
            startActivity(intentHis);
            overridePendingTransition(R.anim.rotate, 0);

        } else if (i == R.id.rl_change_modul) {
            closeDrawLayout();
            setNightMode();

        } else if (i == R.id.rl_collect) {
            closeDrawLayout();
            Intent intentCol = new Intent(this, MyCollectActivity.class);
            startActivity(intentCol);
            overridePendingTransition(R.anim.rotate, R.anim.rotate_out);

        } else if (i == R.id.rl_me) {
            closeDrawLayout();
            Intent intentAbu = new Intent(this, AboutUsActivity.class);
            startActivity(intentAbu);
            overridePendingTransition(R.anim.rotate, R.anim.rotate_out);

        } else if (i == R.id.tv_close) {
            closeDrawLayout();
            finish();

        } else if (i == R.id.head) {
            closeDrawLayout();
            Intent intentAbus = new Intent(this, AboutUsActivity.class);
            startActivity(intentAbus);
            overridePendingTransition(R.anim.rotate, R.anim.rotate_out);

        } else if (i == R.id.iv_chat) {
            closeDrawLayout();
            Intent intentChat = new Intent(this, ChatActivity.class);
            startActivity(intentChat);
            overridePendingTransition(R.anim.rotate, R.anim.rotate_out);

        } else if (i == R.id.iv_setting) {
            SettingFragment settingFragment = SettingFragment.getInstance();
            settingFragment.show(getSupportFragmentManager(), "");

        }
    }

    private void setNightMode() {
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
//        overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
        saveModule(isNight);
    }

    private void saveModule(boolean isNight) {
        SharedPreferences sharedPreferences = getSharedPreferences("module", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean("isNight", isNight);
        editor.commit();//提交修改
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void closeDrawLayout() {
        mDrawlayout.closeDrawer(Gravity.LEFT);
    }
//    //双击退出
//    private long firstTime=0;
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                long secondTime=System.currentTimeMillis();
//
//                if(secondTime-firstTime>2000){
//                    Toast.makeText(PlayActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
//                    firstTime=secondTime;
//                    return true;
//                }else{
//                   finish();
//                }
//                break;
//        }
//        return super.onKeyUp(keyCode, event);
//    }
}
