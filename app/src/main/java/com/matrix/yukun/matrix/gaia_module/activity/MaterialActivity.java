package com.matrix.yukun.matrix.gaia_module.activity;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.MaterialPoolFragmentAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class MaterialActivity extends BaseActivity {

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
    MaterialPoolFragmentAdapter mAdapter;

    public static void start(Context context){
        Intent intent=new Intent(context,MaterialActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_material;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDate() {
       //设置tab的模式
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //适配器
        mAdapter = new MaterialPoolFragmentAdapter(this, getSupportFragmentManager());
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
