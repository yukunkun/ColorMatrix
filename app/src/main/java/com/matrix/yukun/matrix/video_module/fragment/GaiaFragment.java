package com.matrix.yukun.matrix.video_module.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaPersonActivity;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaPlayActivity;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaSearchActivity;
import com.matrix.yukun.matrix.gaia_module.activity.MaterialActivity;
import com.matrix.yukun.matrix.gaia_module.activity.ProductActivity;
import com.matrix.yukun.matrix.gaia_module.adapter.RVGaiaAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.BannerInfo;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.gaia_module.net.GaiCallBack;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.video_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

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
        mRvGaiaAdapter = new RVGaiaAdapter(R.layout.gaia_index_item, mGaiaIndexBeans);
        rvList.setAdapter(mRvGaiaAdapter);
        mRvGaiaAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        rvList.addItemDecoration(new SpacesDoubleDecoration(0,5,5,20));
        setAnimation(ivProduct);
        setAnimation(ivSucai);
        initData();
        initBanner();
        initListener();
    }

    private void initListener() {
        mRvGaiaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GaiaPlayActivity.start(getContext());
            }
        });
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
        NetworkUtils.networkGet(Api.BASE_URL+Api.BANNER).build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                if(data!=null){
                    Gson gson=new Gson();
                    List<BannerInfo> bannerInfos = gson.fromJson(data, new TypeToken<List<BannerInfo>>(){}.getType());
                    mBannerInfos.addAll(bannerInfos);
                    if(mBannerInfos.size()==6){
                        mBannerInfos.remove(5);
                    }
                    conBanner.notifyDataSetChanged();
                }
            }

            @Override
            public void onDateError(String s) {
                ToastUtils.showToast(s);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }
        });

        OkHttpUtils.get().url(Api.BASE_URL+Api.RECOMEND).build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                try {
                    Gson gson=new Gson();
                    JSONObject jsonObject=new JSONObject(data);
                    JSONArray works = jsonObject.optJSONArray("works");
                    List<GaiaIndexBean> gaiaIndexBeans = gson.fromJson(works.toString(), new TypeToken<List<GaiaIndexBean>>(){}.getType());
                    JSONArray materials = jsonObject.optJSONArray("materials");
                    List<GaiaIndexBean> materialsbean = gson.fromJson(materials.toString(), new TypeToken<List<GaiaIndexBean>>(){}.getType());
                    mGaiaIndexBeans.addAll(gaiaIndexBeans);
                    mGaiaIndexBeans.addAll(materialsbean);
                    mRvGaiaAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDateError(String s) {
                ToastUtils.showToast(s);
            }
        });
    }

    @OnClick({R.id.iv_main, R.id.iv_search,R.id.iv_product, R.id.iv_sucai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_main:
                GaiaPersonActivity.start(getContext());
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
            Glide.with(context).load(Api.COVER_PREFIX+data.getShuffl()).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://www.gaiamount.com");    //设置跳转的网站
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
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
