package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVSearchImageAdapter;
import com.matrix.yukun.matrix.main_module.entity.SearchImageInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class SearchImageFragment extends BaseFragment {
    String url="http://image.baidu.com/search/listjson?pn=0&rn=50&tag1=美女&tag2=全部&ftags=小清新";
    private SmartRefreshLayout mSmartRefreshLayout;
    // http://image.baidu.com/search/detail
    private int pn = 0;
    private String tag1 = "动物";
    private String tag2 = "全部";
    private String ftags= "小清新";
    List<SearchImageInfo> imageInfos=new ArrayList<>();
    private RecyclerView mRvJoke;
    private RelativeLayout mLayoutRemind;
    private StaggeredGridLayoutManager mManager;
    private RVSearchImageAdapter mRvSearchImageAdapter;

    public static SearchImageFragment getInstance(String tag1) {
        SearchImageFragment recFragment = new SearchImageFragment();
        Bundle bundle=new Bundle();
        bundle.putString("tag1",tag1);
        recFragment.setArguments(bundle);
        return recFragment;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public void setFtags(String ftags) {
        this.ftags = ftags;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_search_image;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        tag1=getArguments().getString("tag1");
        mSmartRefreshLayout = inflate.findViewById(R.id.refreshLayout);
        mRvJoke = inflate.findViewById(R.id.recyclerview);
        mLayoutRemind = inflate.findViewById(R.id.rl_remind);
        mManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRvJoke.setLayoutManager(mManager);
        mRvJoke.addItemDecoration(new SpacesDoubleDecoration(10));
        mRvSearchImageAdapter = new RVSearchImageAdapter(getContext(),imageInfos);
        mRvJoke.setAdapter(mRvSearchImageAdapter);
        mSmartRefreshLayout.autoRefresh();
        setListener();
        initData();
    }

    private void initData() {
        GetBuilder getBuilder = NetworkUtils.networkGet(url)
                .addParams("pn",pn+"")
                .addParams("rn","50")
                .addParams("tag1", tag1)
                .addParams("tag2", tag2)
                .addParams("ie", "utf-8");
        if(!TextUtils.isEmpty(ftags)){
            getBuilder.addParams("ftags", ftags);
        }
        getBuilder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String data = jsonObject.optString("data");
                    Gson gson=new Gson();
                    List<SearchImageInfo> imageInfo = gson.fromJson(data.toString(), new TypeToken<List<SearchImageInfo>>() {
                    }.getType());
                    if(imageInfo.size()!=0){
                        if(pn==0){
                            imageInfos.clear();
                        }
                        imageInfos.addAll(imageInfo);
                        mRvSearchImageAdapter.notifyDataSetChanged();
                        mSmartRefreshLayout.finishRefresh();
                        mSmartRefreshLayout.finishLoadMore();
                    }else {
                        mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListener() {
        mSmartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pn=pn+2;
                initData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pn=0;
                initData();
            }
        });
    }

}