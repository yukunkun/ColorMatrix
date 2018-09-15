package com.ykk.pluglin_video.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ykk.pluglin_video.BaseFragment;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.entity.EventCategrayPos;
import com.ykk.pluglin_video.play.AboutUsActivity;
import com.ykk.pluglin_video.play.ChatActivity;
import com.ykk.pluglin_video.play.HistoryTodayActivity;
import com.ykk.pluglin_video.play.MViewPagerAdapter;
import com.ykk.pluglin_video.play.MyCollectActivity;
import com.ykk.pluglin_video.play.PlayMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by yukun on 18-1-2.
 */

public class PlayFragment extends BaseFragment {
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
    @BindView(R2.id.iv_chat)
    ImageView mIvChat;
    @BindView(R2.id.iv_collect)
    ImageView mIvCollect;
    @BindView(R2.id.rl_collect)
    RelativeLayout mRlCollect;
    private MViewPagerAdapter mMViewPagerAdapter;
    private String[] mStringArray;
    List<Fragment> mFragments = new ArrayList<>();
    private VideoFragment mInstance1;
    private ImageFragment mInstance3;
    private JokeFragment mInstance4;
    private TextFragment mInstance5;
    private SpecialTxtFragment mInstance2;
    private RecFragment mInstance;

    public static PlayFragment getInstance() {
        PlayFragment playFragment = new PlayFragment();
        return playFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_play;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mStringArray = getResources().getStringArray(R.array.title);
        for (int i = 0; i < mStringArray.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mStringArray[i]));
        }
        mInstance = RecFragment.getInstance();
        mInstance1 = VideoFragment.getInstance();
//        mInstance2 = SpecialTxtFragment.getInstance();
        mInstance3 = ImageFragment.getInstance();
        mInstance4 = JokeFragment.getInstance();
        mInstance5 = TextFragment.getInstance();
        mFragments.add(mInstance);
        mFragments.add(mInstance1);
//        mFragments.add(mInstance2);
        mFragments.add(mInstance3);
        mFragments.add(mInstance4);
        mFragments.add(mInstance5);
        setAdapter();
        setListener();
        OverScrollDecoratorHelper.setUpOverScroll(mScrollview);
    }

    private void setAdapter() {
        mMViewPagerAdapter = new MViewPagerAdapter(getChildFragmentManager(), mFragments, mStringArray);
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
                if (position == 0 || position == 3) {
                    mIvSetting.setVisibility(View.GONE);
                } else {
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
        if (event.pos < 1000) {
            mViewpager.setCurrentItem(event.pos);
        } else {
            switch (event.pos) {
                //横向列表
                case 1001:
                    if (mViewpager.getCurrentItem() == 1) {
                        mInstance1.getLayoutTag(true);
                    }
                    if (mViewpager.getCurrentItem() == 2) {
                        mInstance3.getLayoutTag(true);
                    }
                    if (mViewpager.getCurrentItem() == 3) {
                        mInstance4.getLayoutTag(true);
                    }
                    if (mViewpager.getCurrentItem() == 4) {
                        mInstance5.getLayoutTag(true);
                    }
                    break;
                //格子列表
                case 1002:
                    if (mViewpager.getCurrentItem() == 1) {
                        mInstance1.getLayoutTag(false);
                    }
                    if (mViewpager.getCurrentItem() == 2) {
                        mInstance3.getLayoutTag(false);
                    }
                    if (mViewpager.getCurrentItem() == 3) {
                        mInstance4.getLayoutTag(false);
                    }
                    if (mViewpager.getCurrentItem() == 4) {
                        mInstance5.getLayoutTag(false);
                    }
                    break;
            }
        }
    }

    @OnClick({R2.id.iv_chat, R2.id.iv_setting, R2.id.iv_main, R2.id.head, R2.id.iv_close, R2.id.rl_collect, R2.id.rl_main,
            R2.id.rl_movie, R2.id.rl_change_modul, R2.id.rl_me, R2.id.tv_close})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_main) {
            if (!mDrawlayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawlayout.openDrawer(Gravity.LEFT);
            }

        } else if (i == R.id.iv_close) {
            closeDrawLayout();

        } else if (i == R.id.rl_main) {
            mViewpager.setCurrentItem(0);
            closeDrawLayout();

        } else if (i == R.id.rl_movie) {//推荐
            Intent intentHis = new Intent(getContext(), HistoryTodayActivity.class);
            startActivity(intentHis);
            ((Activity) getContext()).overridePendingTransition(R.anim.rotate, 0);
            closeDrawLayout();

        } else if (i == R.id.rl_change_modul) {
            closeDrawLayout();
            ((PlayMainActivity) getContext()).setNightMode();

        } else if (i == R.id.rl_collect) {
            Intent intentCol = new Intent(getContext(), MyCollectActivity.class);
            startActivity(intentCol);
            ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
            closeDrawLayout();

        } else if (i == R.id.rl_me) {
            Intent intentAbu = new Intent(getContext(), AboutUsActivity.class);
            startActivity(intentAbu);
            ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
            closeDrawLayout();

        } else if (i == R.id.tv_close) {
            closeDrawLayout();
            ((Activity) getContext()).finish();

        } else if (i == R.id.head) {
            Intent intentAbus = new Intent(getContext(), AboutUsActivity.class);
            startActivity(intentAbus);
            ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
            closeDrawLayout();

        } else if (i == R.id.iv_chat) {
            Intent intentChat = new Intent(getContext(), ChatActivity.class);
            startActivity(intentChat);
            ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
            closeDrawLayout();

        } else if (i == R.id.iv_setting) {
            SettingFragment settingFragment = SettingFragment.getInstance();
            settingFragment.show(getChildFragmentManager(), "");

        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void closeDrawLayout() {
        mDrawlayout.closeDrawer(Gravity.LEFT);
    }

    //双击退出
    private long firstTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
