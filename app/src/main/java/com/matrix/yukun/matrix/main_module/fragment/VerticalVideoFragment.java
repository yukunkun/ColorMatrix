package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVImageAdapter;
import com.matrix.yukun.matrix.main_module.entity.ImageData;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * author: kun .
 * date:   On 2018/12/25
 */
public class VerticalVideoFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private RVImageAdapter mRvVerticalAdapter;
    private String url="http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";
    private List<ImageData> mImageDatas=new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;
    private int page=1;
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
        mLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRvVerticalAdapter = new RVImageAdapter(getContext(),mImageDatas);
        mRecyclerView.setAdapter(mRvVerticalAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));
        initData();
        initListener();
    }

    private void initData() {
        OkHttpUtils.get().url(url+page)
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                if(!TextUtils.isEmpty(response)){
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray results = jsonObject.optJSONArray("results");
                        if(results!=null&&results.length()>0){
                            Gson gson=new Gson();
                            List<ImageData> imageDatas  = gson.fromJson(results.toString(), new TypeToken<List<ImageData>>() {}.getType());
                            mImageDatas.addAll(imageDatas);
                            mRvVerticalAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast("error");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });
    }

    @Override
    public void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                mImageDatas.clear();
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
                        page++;
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
