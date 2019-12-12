package com.matrix.yukun.matrix.download_module.fragment;


import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.adapter.RVDownLoadAdapter;
import com.matrix.yukun.matrix.download_module.bean.DownLoadError;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;
import com.matrix.yukun.matrix.download_module.service.DownLoadEngine;
import com.matrix.yukun.matrix.download_module.service.DownLoadListener;
import com.matrix.yukun.matrix.main_module.views.SwipeItemLayout;
import com.matrix.yukun.matrix.util.FileUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/1/21
 */
public class DownLoadFragment extends BaseFragment implements DownLoadListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<FileInfo> mInfos=new ArrayList<>();
    private RVDownLoadAdapter mRvDownLoadedAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout mRelativeLayout;
    private RelativeLayout mRlContain;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private TextView mTvName;
    private TextView mTvPro;

    public static DownLoadFragment getInstance(){
        DownLoadFragment documentFragment=new DownLoadFragment();
        return documentFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_download;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        DownLoadEngine.getInstance().getDownManager().addListener(this);
        mRecyclerView = inflate.findViewById(R.id.recyclerview);
        mRlContain = inflate.findViewById(R.id.rl_contain);
        mProgressBar = inflate.findViewById(R.id.progressbar);
        mImageView = inflate.findViewById(R.id.iv_icon);
        mTvName = inflate.findViewById(R.id.tv_pro);
        mTvPro = inflate.findViewById(R.id.tv_product);
        mSwipeRefreshLayout = inflate.findViewById(R.id.sw);
        mRelativeLayout = inflate.findViewById(R.id.rl_remind);
        mRlContain.setVisibility(View.GONE);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRvDownLoadedAdapter = new RVDownLoadAdapter(mInfos,getContext());
        mRecyclerView.setAdapter(mRvDownLoadedAdapter);
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        initData();
        initListener();
    }

    private void initData() {
        List<FileInfo> fileInfos = DataSupport.findAll(FileInfo.class);
        Collections.reverse(fileInfos);
        if(fileInfos.size()>0){
            mRelativeLayout.setVisibility(View.GONE);
        }
        mInfos.addAll(fileInfos);
        mRvDownLoadedAdapter.notifyDataSetChanged();
    }

    public void initListener() {

    }
    @Override
    public void onStart(FileInfo fileInfo) {
        mRlContain.setVisibility(View.VISIBLE);
        mTvName.setText(fileInfo.fileName);
        Glide.with(getContext()).load(fileInfo.imageUrl).into(mImageView);
        mProgressBar.setMax((int)fileInfo.size);
        for (int i = 0; i < mInfos.size(); i++) {
            if(mInfos.get(i).url.equals(fileInfo.url)){
                mInfos.remove(i);
                mRvDownLoadedAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onFail(DownLoadError error) {

    }

    public boolean isError(){
        if(mRelativeLayout.getVisibility()==View.GONE && mRlContain.getVisibility()==View.GONE){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onDownLoad(FileInfo fileInfo, long totalSize, long progress) {
        if(mRlContain.getVisibility()==View.GONE){
            mRlContain.setVisibility(View.VISIBLE);
            mTvName.setText(fileInfo.fileName);
            Glide.with(getContext()).load(fileInfo.imageUrl).into(mImageView);
            for (int i = 0; i < mInfos.size(); i++) {
                if(mInfos.get(i).url.equals(fileInfo.url)){
                    mInfos.remove(i);
                    mRvDownLoadedAdapter.notifyDataSetChanged();
                }
            }
        }
        mProgressBar.setMax((int)fileInfo.size);
        mProgressBar.setProgress((int)progress);
        mTvPro.setText("当前进度："+(int)((float)progress/totalSize*100)+"% "+ FileUtil.formatFileSize(fileInfo.size));
    }

    @Override
    public void onComplete(FileInfo fileInfo) {
//        mRlContain.setVisibility(View.GONE);
        DataSupport.deleteAll(FileInfo.class, "url = ?", fileInfo.url);
        mTvPro.setText("当前进度：已完成 "+ FileUtil.formatFileSize(fileInfo.size));
        for (int i = 0; i < mInfos.size(); i++) {
            if(mInfos.get(i).url.equals(fileInfo.url)){
                mInfos.remove(i);
                mRvDownLoadedAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDownLoadCancel(FileInfo fileInfo) {

    }

    @Override
    public void onDownLoadPause(FileInfo fileInfo) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DownLoadEngine.getInstance().getDownManager().removeListener(this);
    }
}
