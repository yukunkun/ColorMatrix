package com.matrix.yukun.matrix.video_module.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaSearchActivity;
import com.matrix.yukun.matrix.gaia_module.activity.MaterialActivity;
import com.matrix.yukun.matrix.gaia_module.activity.ProductActivity;
import com.matrix.yukun.matrix.gaia_module.adapter.RVGaiaAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.BannerInfo;
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
    @BindView(R.id.iv_product)
    ImageView ivProduct;
    @BindView(R.id.iv_sucai)
    ImageView ivSucai;
    Unbinder unbinder;
    private GridLayoutManager mGridLayoutManager;
    private List<GaiaIndexBean> mGaiaIndexBeans = new ArrayList<>();
    private List<BannerInfo> mBannerInfos = new ArrayList<>();
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
        mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvList.setLayoutManager(mGridLayoutManager);
        mRvGaiaAdapter = new RVGaiaAdapter(R.layout.gaia_index_layout_item, mGaiaIndexBeans);
        rvList.setAdapter(mRvGaiaAdapter);
        mRvGaiaAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        setAnimation(ivProduct);
        setAnimation(ivSucai);
        initData();
        initBanner();
    }

    private void initBanner() {
        conBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, mBannerInfos);

        conBanner.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicatored})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .startTurning(3000)
                .setCanLoop(true);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mGaiaIndexBeans.add(new GaiaIndexBean());
        }
        for (int i = 0; i < 5; i++) {
            mBannerInfos.add(new BannerInfo());
        }
    }

    @OnClick({R.id.iv_main, R.id.iv_search,R.id.iv_product, R.id.iv_sucai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_main:

                break;
            case R.id.iv_search:
                GaiaSearchActivity.start(getContext());
                break;
            case R.id.iv_product:
                ProductActivity.start(getContext());
                break;
            case R.id.iv_sucai:
                MaterialActivity.start(getContext());
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class LocalImageHolderView implements Holder<BannerInfo> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, BannerInfo data) {
            Glide.with(context).load(R.mipmap.bg_header_nav).into(imageView);
        }
    }
    /**
     * 设置旋转的动画
     */
    public void setAnimation(View view) {
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", -6, 6,6,-6);
        mObjectAnimator.setDuration(500);
        mObjectAnimator.setRepeatCount(-1);
        mObjectAnimator.setInterpolator(new AccelerateInterpolator());
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.start();
    }
}
