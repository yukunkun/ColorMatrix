package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.ImageAdapter;
import com.matrix.yukun.matrix.main_module.adapter.ShareCallBack;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.entity.ImageInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class ImageSecondFragment extends BaseFragment implements ShareCallBack {
    String url="http://cn.bing.com/HPImageArchive.aspx";
    private SmartRefreshLayout mSmartRefreshLayout;
    private int n = 7;
    private int idx = 0;
    private List<ImageInfo> jokeInfoList=new ArrayList<>();
    private ImageAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRvJoke;

    public static ImageSecondFragment getInstance() {
        ImageSecondFragment recFragment = new ImageSecondFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_second_image;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mSmartRefreshLayout = inflate.findViewById(R.id.refreshLayout);
        mRvJoke = inflate.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new ImageAdapter(getContext(),jokeInfoList);
        mJokeAdapter.setShareCallBack(this);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
    }

    private void setListener() {
        mSmartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                    idx=idx+n+1;
                    getInfo();
                }
                mSmartRefreshLayout.finishLoadMore(10);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                jokeInfoList.clear();
                mJokeAdapter.notifyDataSetChanged();
                idx=0;
                getInfo();
                refreshLayout.finishRefresh(10);
            }
        });
    }

    private void getInfo() {
        NetworkUtils.networkGet(url)
                .addParams("format","js")
                .addParams("idx",idx+"")
                .addParams("n",n+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast(e.toString());
            }
            @Override
            public void onResponse(String response, int id) {
                try {
                    if(!TextUtils.isEmpty(response)){
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray images = jsonObject.optJSONArray("images");
                        Gson gson=new Gson();
                        List<ImageInfo> imageInfos = gson.fromJson(images.toString(), new TypeToken<List<ImageInfo>>(){}.getType());
                        jokeInfoList.addAll(imageInfos);
                        mJokeAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onShareCallback(int pos) {
        ImageInfo imageInfo = jokeInfoList.get(pos);
        ShareDialog shareDialog= ShareDialog.getInstance(getString(R.string.title_content),"http://s.cn.bing.net" + imageInfo.getUrl(),"http://s.cn.bing.net" + imageInfo.getUrl());
        shareDialog.show(getFragmentManager(),"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}