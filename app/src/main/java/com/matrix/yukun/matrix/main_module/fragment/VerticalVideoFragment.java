package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVImageAdapter;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.ImageData;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;

/**
 * author: kun .
 * date:   On 2018/12/25
 */
public class VerticalVideoFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private RVImageAdapter mRvVerticalAdapter;
    private String url="http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/";
    private List<ImageData> mImageDatas=new ArrayList<>();
    private SmartRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;
    private Random mRandom=new Random();
    private int page=getRandom();

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
                            updateImageData(imageDatas);
                            mImageDatas.addAll(imageDatas);
                            mRvVerticalAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast("没有更多了");
                        }
                        mSwipeRefreshLayout.finishRefresh();
                        mSwipeRefreshLayout.finishLoadMore();
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

    private void updateImageData(List<ImageData> imageDatas) {
        List<CollectsInfo> collectsInfos = DataSupport.where("type = ?","2").find(CollectsInfo.class);
        for (int i = 0; i <imageDatas.size() ; i++) {
            for (CollectsInfo collectsInfo: collectsInfos) {
                if(collectsInfo.getPlay_url().equals(imageDatas.get(i).getUrl())){
                    imageDatas.get(i).setCollect(true);
                }
            }
        }
    }
    public int getRandom(){
        return mRandom.nextInt(30);
    }

    @Override
    public void initListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=getRandom();
                mImageDatas.clear();
                initData();
            }
        });

        mSwipeRefreshLayout.setOnLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            }
        });

        mRvVerticalAdapter.setOnClickItemListener(new RVImageAdapter.OnClickItemListener() {
            @Override
            public void onClickItemClick(int pos, ImageData data) {
                if(data.isCollect()){
                    CollectsInfo collectInfo=new CollectsInfo();
                    collectInfo.setHeader(data.getUrl());
                    collectInfo.setCover(data.getUrl());
                    collectInfo.setTitle("佚名");
                    collectInfo.setName("佚名");
                    collectInfo.setType(2);
                    collectInfo.setPlay_url(data.getUrl());
                    collectInfo.setGif(false);
                    collectInfo.save();
                }else {
                    DataSupport.deleteAll(CollectsInfo.class,"cover=?",data.getUrl());
                }
                Toast.makeText(getContext(), "添加到收藏成功", Toast.LENGTH_SHORT).show();
                mRvVerticalAdapter.updateItem(pos,data);
            }

            @Override
            public void onLoadError(int pos, ImageData imageData) {
                synchronized (this){
                    if(pos<mImageDatas.size()){
                        mImageDatas.remove(pos);
                        mRvVerticalAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
