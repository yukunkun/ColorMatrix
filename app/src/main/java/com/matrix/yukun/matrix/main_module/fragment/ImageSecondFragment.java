package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVPlayAllAdapter;
import com.matrix.yukun.matrix.main_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class ImageSecondFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private RVPlayAllAdapter mRvVerticalAdapter;
    private String url="https://cdn.mom1.cn/?mom=json";
    private List<PlayAllBean> mPlayAllBeans=new ArrayList<>();
    private SmartRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mLayout;

    public static ImageSecondFragment getInstance(){
        ImageSecondFragment verticalVideoFragment=new ImageSecondFragment();
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
                                mSwipeRefreshLayout.finishRefresh();
                                mSwipeRefreshLayout.finishLoadMore();
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
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
                mSwipeRefreshLayout.finishRefresh();
            }
        });
//        mSwipeRefreshLayout.setOnRefreshListener(() -> {
//            initData();
//            mSwipeRefreshLayout.setRefreshing(false);
//        });

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