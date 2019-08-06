package com.matrix.yukun.matrix.gaia_module.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.VideoMaterialAdapter;
import com.matrix.yukun.matrix.gaia_module.adapter.WorkPoolAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.gaia_module.net.GaiCallBack;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * author: kun .
 * date:   On 2019/7/5
 */
public class MaterialPoolFragment extends BaseFragment {

    public static int pos;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private GridLayoutManager mGridLayoutManager;
    private int pi = 1;
    List<GaiaIndexBean> mGaiaIndexBeans = new ArrayList<>();
    private VideoMaterialAdapter mVideoMaterialAdapter;

    public static MaterialPoolFragment getInstance(int position) {
        MaterialPoolFragment workSearchFragment = new MaterialPoolFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("pos",position);
        workSearchFragment.setArguments(bundle);
        return workSearchFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.matrial_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        pos=getArguments().getInt("pos");
        mGridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerview.setLayoutManager(mGridLayoutManager);
        mVideoMaterialAdapter = new VideoMaterialAdapter(getContext(),mGaiaIndexBeans);
        recyclerview.setAdapter(mVideoMaterialAdapter);
        recyclerview.addItemDecoration(new SpacesDoubleDecoration(5,10,10,8));
        initData(pos);
        inListener();
    }

    private void inListener() {
        smartLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pi=1;
                mGaiaIndexBeans.clear();
                mVideoMaterialAdapter.notifyDataSetChanged();
                initData(pos);
                smartLayout.finishRefresh();
            }
        });

        smartLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pi++;
                initData(pos);
            }
        });
    }

    private void initData(int pos) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t", pos);
            jsonObject.put("s", 0);
            jsonObject.put("opr", 0);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", 20);
            jsonObject.put("type", 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Api.BASE_URL + Api.SEARCHMATRIL)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                progressBar.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    List<GaiaIndexBean> list = gson.fromJson(data, new TypeToken<List<GaiaIndexBean>>() {}.getType());
                    if(list.size()>0){
                        mGaiaIndexBeans.addAll(list);
                        mVideoMaterialAdapter.notifyDataSetChanged();
                        smartLayout.finishLoadMore();
                    }else {
                        smartLayout.finishLoadMore();
                        ToastUtils.showToast("没有更多了");
                    }
                } else {
                    ToastUtils.showToast("没有更多了");
                }
            }

            @Override
            public void onDateError(String error) {
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }
        });
    }
}