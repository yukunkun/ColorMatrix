package com.matrix.yukun.matrix.leancloud_module.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.fragment.FriendFragment;
import com.matrix.yukun.matrix.leancloud_module.fragment.GroupFragment;
import com.matrix.yukun.matrix.main_module.activity.MViewPagerAdapter;
import com.matrix.yukun.matrix.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactMemberActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    String[] mStrings = new String[]{"好友", "群组"};
    List<Fragment> mFrameLayouts = new ArrayList<>();
    private MViewPagerAdapter mMViewPagerAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, ContactMemberActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public int getLayout() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        return R.layout.activity_contact_member;
    }

    @Override
    public void initView() {
        for (int i = 0; i < mStrings.length; i++) {
            tablayout.addTab(tablayout.newTab().setText(mStrings[i]));
        }
        mFrameLayouts.add(FriendFragment.getInstance());
        mFrameLayouts.add(GroupFragment.getInstance());
        mMViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(), mFrameLayouts, mStrings);
        viewPager.setAdapter(mMViewPagerAdapter);
        tablayout.setupWithViewPager(viewPager);
//        if (ViewCompat.isLaidOut(tablayout)) {
//        } else {
//            tablayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                @Override
//                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                    tablayout.setupWithViewPager(viewPager);
//                    tablayout.removeOnLayoutChangeListener(this);
//                }
//            });
//        }
    }

    @Override
    public void initDate() {

    }

    @Override
    public void initListener() {
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tablayout.setScrollPosition(position, 0, true);
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.iv_search:
                break;
        }
    }

}
