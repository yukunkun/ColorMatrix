package com.matrix.yukun.matrix.download_module.fragment;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.adapter.RVDownLoadedAdapter;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;
import com.matrix.yukun.matrix.main_module.views.SwipeItemLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * author: kun .
 * date:   On 2019/1/21
 */
public class DownLoadedFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<FileInfo> mInfos=new ArrayList<>();
    private RVDownLoadedAdapter mRvDownLoadedAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout mRelativeLayout;

    public static DownLoadedFragment getInstance(){
        DownLoadedFragment documentFragment=new DownLoadedFragment();
        return documentFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_downloaded;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mRecyclerView = inflate.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = inflate.findViewById(R.id.sw);
        mRelativeLayout = inflate.findViewById(R.id.rl_remind);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRvDownLoadedAdapter = new RVDownLoadedAdapter(mInfos,getContext());
        mRecyclerView.setAdapter(mRvDownLoadedAdapter);
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        initData();
    }

    public void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                mInfos.clear();
                initData();
            }
        });
    }

    private void initData() {
        File file=new File(AppConstant.VIDEOPATH);
        if(!file.exists()){
            file.mkdir();
        }
        File[] listFiles = file.listFiles();
        if(listFiles!=null){
            for (int i = 0; i < listFiles.length; i++) {
                FileInfo fileInfo=new FileInfo(listFiles[i]);
                mInfos.add(fileInfo);
            }
        }
        if(mInfos.size()>0){
            mRelativeLayout.setVisibility(View.GONE);
        }
        mRvDownLoadedAdapter.notifyDataSetChanged();
    }
}
