package com.matrix.yukun.matrix.gaia_module.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.SearchWorkAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.gaia_module.net.GaiCallBack;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * author: kun .
 * date:   On 2019/7/5
 */
public class WorkSearchFragment extends BaseFragment {
    LinearLayout lastSelectedView;
    @BindView(R.id.relevancy)
    LinearLayout relevancy;
    @BindView(R.id.most_person)
    LinearLayout mostPerson;
    @BindView(R.id.most_collection)
    LinearLayout mostCollection;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<GaiaIndexBean> mGaiaIndexBeans = new ArrayList<>();
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private SearchWorkAdapter mSearchWorkAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String key = "";
    private int pi = 1;
    private int stype = 0;

    public static WorkSearchFragment getInstance() {
        WorkSearchFragment workSearchFragment = new WorkSearchFragment();
        return workSearchFragment;
    }

    public void setKey(String key) {
        this.key = key;
        pi = 1;
        initData(stype, key, pi);
    }

    @Override
    public int getLayout() {
        return R.layout.work_search_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(mLinearLayoutManager);
        mSearchWorkAdapter = new SearchWorkAdapter(getContext(), mGaiaIndexBeans);
        recyclerview.setAdapter(mSearchWorkAdapter);
        changeStyle(relevancy);
        initListener();
    }

    private void initData(int stype, String key, int pi) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wtype", 0);
            jsonObject.put("ptype", 0);
            jsonObject.put("stype", stype);
            jsonObject.put("pi", pi);
            jsonObject.put("ps", 10);
            jsonObject.put("key", key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Api.BASE_URL + Api.SEARCHWORK)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                progressBar.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(data)) {
                    Gson gson = new Gson();
                    List<GaiaIndexBean> list = gson.fromJson(data, new TypeToken<List<GaiaIndexBean>>() {
                    }.getType());
                    if(list.size()>0){
                        mGaiaIndexBeans.addAll(list);
                        mSearchWorkAdapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.showToast("没有更多了");
                    }
                } else {
                    ToastUtils.showToast("没有更多了");
                }
            }

            @Override
            public void onDateError(String error) {
                ToastUtils.showToast("搜索错误: " + error);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }
        });
    }

    private void initListener() {
        //加载更多
        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == mLinearLayoutManager.getItemCount() - 1) {
                    //加载更多
                    pi++;
                    initData(stype, key, pi);
                }
            }
        });
    }


    public void changeStyle(LinearLayout view) {
        if (lastSelectedView != null && lastSelectedView.getChildAt(0) instanceof TextView) {
            TextView child = (TextView) lastSelectedView.getChildAt(0);
            child.setTextColor(getResources().getColor(R.color.color_b1b1b1));
            child.setBackgroundResource(R.color.transparent);
        }
        TextView textViewChild = (TextView) view.getChildAt(0);
        textViewChild.setTextColor(Color.WHITE);
        textViewChild.setBackgroundResource(R.drawable.shape_tv_radius12_drakgray);
        lastSelectedView = view;
    }

    @OnClick({R.id.relevancy, R.id.most_person, R.id.most_collection})
    public void onViewClicked(View view) {
        progressBar.setVisibility(View.VISIBLE);
        switch (view.getId()) {
            case R.id.relevancy:
                changeStyle(relevancy);
                stype = 0;
                pi = 1;
                mGaiaIndexBeans.clear();
                mSearchWorkAdapter.notifyDataSetChanged();
                initData(stype, key, pi);
                break;

            case R.id.most_person:
                changeStyle(mostPerson);
                stype = 2;
                pi = 1;
                mGaiaIndexBeans.clear();
                mSearchWorkAdapter.notifyDataSetChanged();
                initData(stype, key, pi);
                break;
            case R.id.most_collection:
                changeStyle(mostCollection);
                stype = 3;
                pi = 1;
                mGaiaIndexBeans.clear();
                mSearchWorkAdapter.notifyDataSetChanged();
                initData(stype, key, pi);
                break;
        }
    }
}
