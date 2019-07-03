package com.matrix.yukun.matrix.video_module.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaSearchActivity;
import com.matrix.yukun.matrix.gaia_module.adapter.RVGaiaAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.video_module.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class GaiaFragment extends BaseFragment {

    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    @BindView(R.id.con_banner)
    ConvenientBanner conBanner;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private GridLayoutManager mGridLayoutManager;
    private List<GaiaIndexBean> mGaiaIndexBeans=new ArrayList<>();
    private RVGaiaAdapter mRvGaiaAdapter;

    public static GaiaFragment getInstance() {
        GaiaFragment gaiaFragment = new GaiaFragment();
        return gaiaFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_gaia;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mGridLayoutManager = new GridLayoutManager(getContext(),2);
        rvList.setLayoutManager(mGridLayoutManager);
        mRvGaiaAdapter = new RVGaiaAdapter(R.layout.gaia_index_layout_item,mGaiaIndexBeans);
        rvList.setAdapter(mRvGaiaAdapter);
        mRvGaiaAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mGaiaIndexBeans.add(new GaiaIndexBean());
        }
    }

    @OnClick({R.id.iv_main, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_main:

                break;
            case R.id.iv_search:
                GaiaSearchActivity.start(getContext());
                break;
        }
    }
}
