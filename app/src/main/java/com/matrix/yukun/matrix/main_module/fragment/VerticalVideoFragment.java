package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVPlayAllAdapter;
import com.matrix.yukun.matrix.main_module.entity.PlayAllBean;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
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
//                .addParams("type",1+"")
//                .addParams("page",page+"")
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
                                for (int i = 0; i < 10; i++) {
                                    PlayAllBean playAllBean=new PlayAllBean();
                                    playAllBean.setThumbnail(downloadUrl);
                                    mPlayAllBeans.add(playAllBean);
                                }
                                mSwipeRefreshLayout.finishRefresh();
                                mSwipeRefreshLayout.finishLoadMore();
                                mLayout.setVisibility(View.GONE);
                                mRvVerticalAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    if(jsonObject.optInt("code")==200){
//                        JSONArray data = jsonObject.optJSONArray("data");
//                        Gson gson=new Gson();
//                        List<PlayAllBean>  playAllBeans = gson.fromJson(data.toString(), new TypeToken<List<PlayAllBean>>(){}.getType());
//                        if(page==1){
//                            mPlayAllBeans.clear();
//                        }
//                        mPlayAllBeans.addAll(playAllBeans);
//                        for (int i = 0; i < playAllBeans.size(); i++) {
//                            if(!playAllBeans.get(i).getType().equals("video")){
//                                mPlayAllBeans.remove(playAllBeans.get(i));
//                            }
//                        }
//                        mSwipeRefreshLayout.finishRefresh();
//                        mSwipeRefreshLayout.finishLoadMore();
//                        mLayout.setVisibility(View.GONE);
//                        mRvVerticalAdapter.notifyDataSetChanged();
//                        if(page==1){
//                            DataSupport.deleteAll(PlayAllBean.class);
//                            for (int i = 0; i < mPlayAllBeans.size(); i++) {
//                                mPlayAllBeans.get(i).save();
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
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

    @Override
    public void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=1;
                initData(page);
            }
        });
        mSwipeRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                initData(page);
            }
        });
    }
}
