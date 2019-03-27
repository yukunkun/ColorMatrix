package com.matrix.yukun.matrix.video_module.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.adapter.RVPlayAllAdapter;
import com.matrix.yukun.matrix.video_module.adapter.RVVerticalAdapter;
import com.matrix.yukun.matrix.video_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.video_module.utils.SpacesDoubleDecoration;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
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
    private String url="https://www.apiopen.top/satinGodApi";
    private List<PlayAllBean> mPlayAllBeans=new ArrayList<>();
    private SmartRefreshLayout mSwipeRefreshLayout;
    private int page=1;
    private GridLayoutManager mLayoutManager;
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
        mLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRvVerticalAdapter = new RVPlayAllAdapter(getContext(),mPlayAllBeans);
        mRecyclerView.setAdapter(mRvVerticalAdapter);
        mRecyclerView.addItemDecoration(new SpacesDoubleDecoration(10,10,10,10));
        initData(page);
        initListener();
    }

    private void initData(final int page) {
        OkHttpUtils.get().url(url)
                .addParams("type",1+"")
                .addParams("page",page+"")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optInt("code")==200){
                        JSONArray data = jsonObject.optJSONArray("data");
                        Gson gson=new Gson();
                        List<PlayAllBean>  playAllBeans = gson.fromJson(data.toString(), new TypeToken<List<PlayAllBean>>(){}.getType());
                        if(page==1){
                            mPlayAllBeans.clear();
                        }
                        mPlayAllBeans.addAll(playAllBeans);
                        for (int i = 0; i < playAllBeans.size(); i++) {
                            if(!playAllBeans.get(i).getType().equals("video")){
                                mPlayAllBeans.remove(playAllBeans.get(i));
                            }
                        }
                        mSwipeRefreshLayout.finishRefresh();
                        mSwipeRefreshLayout.finishLoadmore();
                        mLayout.setVisibility(View.GONE);
                        mRvVerticalAdapter.notifyDataSetChanged();
                        if(page==1){
                            DataSupport.deleteAll(PlayAllBean.class);
                            for (int i = 0; i < mPlayAllBeans.size(); i++) {
                                mPlayAllBeans.get(i).save();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Call call, Exception e, int id) {
                mSwipeRefreshLayout.finishRefresh();
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

    private void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=1;
                initData(page);
            }
        });
        mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                initData(page);
            }
        });
    }
}
