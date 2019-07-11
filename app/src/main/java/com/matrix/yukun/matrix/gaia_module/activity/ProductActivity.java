package com.matrix.yukun.matrix.gaia_module.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.WorkPoolFragmentAdapter;
import com.matrix.yukun.matrix.video_module.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    WorkPoolFragmentAdapter mAdapter;
    private String[] mFilterArray;

    public static void start(Context context) {
        Intent intent = new Intent(context, ProductActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_product;
    }

    @Override
    public void initView() {
        mFilterArray = getResources().getStringArray(R.array.work_pool);

    }

    @Override
    public void initDate() {

        //设置tab的模式
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //适配器
        mAdapter = new WorkPoolFragmentAdapter(this, getSupportFragmentManager());
        viewpager.setAdapter(mAdapter);
        //关联两者getVideoInfo
        tablayout.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(3);

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                GaiaSearchActivity.start(this);
                break;
        }
    }
}
