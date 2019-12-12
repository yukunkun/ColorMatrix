package com.matrix.yukun.matrix.main_module.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaPlayActivity;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaSearchActivity;
import com.matrix.yukun.matrix.gaia_module.activity.MaterialActivity;
import com.matrix.yukun.matrix.gaia_module.activity.ProductActivity;
import com.matrix.yukun.matrix.gaia_module.activity.RVTestActivity;
import com.matrix.yukun.matrix.gaia_module.adapter.RVGaiaAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.BannerInfo;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.bean.VideoType;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.gaia_module.net.GaiCallBack;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.encrypt.AES128;
import com.matrix.yukun.matrix.util.encrypt.SHA256;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class CircleFragment extends BaseFragment {

    private GridLayoutManager mGridLayoutManager;
    private List<GaiaIndexBean> mGaiaIndexBeans = new ArrayList<>();
    private List<BannerInfo> mBannerInfos = new ArrayList<>();
    private RVGaiaAdapter mRvGaiaAdapter;

    public static CircleFragment getInstance() {
        CircleFragment circleFragment = new CircleFragment();
        return circleFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        initData();
    }

    public void initListener() {

    }

    private void initData() {

    }

  /*  @OnClick({R.id.iv_main, R.id.iv_search, R.id.iv_product, R.id.iv_sucai})
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
*/
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
