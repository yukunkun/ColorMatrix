package com.matrix.yukun.matrix.gaia_module.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.fragment.MaterialSearchFragment;
import com.matrix.yukun.matrix.gaia_module.fragment.WorkSearchFragment;
import com.matrix.yukun.matrix.util.KeyBoardUtil;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.play.MViewPagerAdapter;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GaiaSearchActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> mFragmentList=new ArrayList<>();
    private String[] mStrings=new String[2];
    private WorkSearchFragment mInstance;
    private MaterialSearchFragment mInstance1;

    public static void start(Context context) {
        Intent intent = new Intent(context, GaiaSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_gaia_search;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDate() {
        mInstance = WorkSearchFragment.getInstance();
        mFragmentList.add(mInstance);
        mInstance1 = MaterialSearchFragment.getInstance();
        mFragmentList.add(mInstance1);
        mStrings = getResources().getStringArray(R.array.gaia_search);
        for (int i = 0; i < mStrings.length; i++) {
            tablayout.addTab(tablayout.newTab().setText(mStrings[i]));
        }
        viewpager.setAdapter(new MViewPagerAdapter(getSupportFragmentManager(),mFragmentList,mStrings));
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                if(!TextUtils.isEmpty(etSearch.getText().toString())){
                    if(mInstance!=null){
                        mInstance.setKey(etSearch.getText().toString());
                    }if(mInstance1!=null){
                        mInstance1.setKey(etSearch.getText().toString());
                    }
                    KeyBoardUtil.closeKeyboard(this);
                }else {
                    ToastUtils.showToast("搜索内容为空");
                }
                break;
        }
    }

}
