package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.entity.TouTiaoBean;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.util.log.LogUtil;
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
    // http://ic.snssdk.com/2/article/v25/stream/?category=news_hot&count=20
    String url="http://ic.snssdk.com/2/article/v25/stream/";
    private SmartRefreshLayout mSmartRefreshLayout;
    private int count = 0;
    List<TouTiaoBean> imageInfos=new ArrayList<>();
    private RecyclerView mRvJoke;
    private RelativeLayout mLayoutRemind;
    private StaggeredGridLayoutManager mManager;

    public static SearchImageFragment getInstance() {
        SearchImageFragment recFragment = new SearchImageFragment();
        Bundle bundle=new Bundle();
        recFragment.setArguments(bundle);
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_search_image;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mSmartRefreshLayout = inflate.findViewById(R.id.refreshLayout);
        mRvJoke = inflate.findViewById(R.id.recyclerview);
        mLayoutRemind = inflate.findViewById(R.id.rl_remind);
        mManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRvJoke.setLayoutManager(mManager);
        mRvJoke.addItemDecoration(new SpacesDoubleDecoration(10));
//        mRvJoke.setAdapter();
        mSmartRefreshLayout.autoRefresh();
        setListener();
        initData();
    }

    private void initData() {
        GetBuilder getBuilder = NetworkUtils.networkGet(url)
                .addParams("count",count+"");
            getBuilder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optString("message").equals("success")){
                        String data = jsonObject.optString("data");
                        Gson gson=new Gson();
                        List<TouTiaoBean> imageInfo = gson.fromJson(data.toString(), new TypeToken<List<TouTiaoBean>>() {}.getType());
                        LogUtil.i("=========",imageInfo.toString());

                        if(imageInfo.size()!=0){
                            if(count==0){
                                imageInfos.clear();
                            }
                            mSmartRefreshLayout.finishRefresh();
                            mSmartRefreshLayout.finishLoadMore();
                        }else {
                            mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
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
                initData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

}