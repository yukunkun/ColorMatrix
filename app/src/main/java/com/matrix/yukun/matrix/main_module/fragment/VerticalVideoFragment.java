package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVPlayAllAdapter;
import com.matrix.yukun.matrix.main_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * author: kun .
 * date:   On 2018/12/25
 */
public class VerticalVideoFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private RVPlayAllAdapter mRvVerticalAdapter;
    private String url="https://cdn.mom1.cn/?mom=json";
    private List<PlayAllBean> mPlayAllBeans=new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mLayout;

    public static VerticalVideoFragment getInstance(){
        VerticalVideoFragment verticalVideoFragment=new VerticalVideoFragment();
        return verticalVideoFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_vertical_video;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mRecyclerView = inflate.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = inflate.findViewById(R.id.srl_layout);
        mLayout = inflate.findViewById(R.id.rl_remind);
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRvVerticalAdapter = new RVPlayAllAdapter(getContext(),mPlayAllBeans);
        mRecyclerView.setAdapter(mRvVerticalAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));
        initData();
        initListener();
    }

    private void initData() {
        OkHttpUtils.get().url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                if(!TextUtils.isEmpty(response)){
                    int indexOf = response.indexOf("{");
                    if(indexOf!=-1){
                        String data=response.substring(indexOf,response.length());
                        try {
                            JSONObject jsonObject=new JSONObject(data);
                            if(jsonObject.optString("result").equals("200")){
                                String downloadUrl="http:"+jsonObject.optString("img");
                                PlayAllBean playAllBean=new PlayAllBean();
                                playAllBean.setThumbnail(downloadUrl);
                                mPlayAllBeans.add(playAllBean);
                                mLayout.setVisibility(View.GONE);
                                mRvVerticalAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onError(Call call, Exception e, int id) {
                List<PlayAllBean> all = DataSupport.findAll(PlayAllBean.class);
                if(all.size()>0){
                    mPlayAllBeans.clear();
                    mPlayAllBeans.addAll(all);
                    mLayout.setVisibility(View.GONE);
                    mRvVerticalAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                        initData();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
