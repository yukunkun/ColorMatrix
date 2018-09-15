package com.ykk.pluglin_video.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ykk.pluglin_video.BaseFragment;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.CollectAdapter;
import com.ykk.pluglin_video.entity.CollectsInfo;
import com.ykk.pluglin_video.utils.SpacesDoubleDecoration;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCollectFragment extends BaseFragment {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_deal)
    TextView mTvDeal;
    @BindView(R2.id.rl)
    RelativeLayout mRl;
    @BindView(R2.id.rv_collect)
    RecyclerView mRvCollect;
    GridLayoutManager mLayoutManager;
    CollectAdapter mCollectAdapter;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    @BindView(R2.id.default_bg)
    RelativeLayout mDefaultBg;
    private List<CollectsInfo> mCollectInfoList;

    public static MyCollectFragment getInstance() {
        return new MyCollectFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_my_collect;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mCollectInfoList = DataSupport.findAll(CollectsInfo.class);
        Collections.reverse(mCollectInfoList);
        mIvBack.setVisibility(View.GONE);
        if(mCollectInfoList.size()==0){
            mDefaultBg.setVisibility(View.VISIBLE);
        }else {
            mDefaultBg.setVisibility(View.GONE);
        }
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRvCollect.setLayoutManager(mLayoutManager);
        mCollectAdapter = new CollectAdapter(getContext(), mCollectInfoList);
        mRvCollect.setAdapter(mCollectAdapter);
        mRvCollect.addItemDecoration(new SpacesDoubleDecoration(0, 4, 8, 16));

        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCollectInfoList.clear();
                mCollectInfoList = DataSupport.findAll(CollectsInfo.class);
                if(mCollectInfoList.size()==0){
                    mDefaultBg.setVisibility(View.VISIBLE);
                }else {
                    mDefaultBg.setVisibility(View.GONE);
                }
                Collections.reverse(mCollectInfoList);
                mCollectAdapter.getInfo(mCollectInfoList);
                mSw.setRefreshing(false);
            }
        });
    }

    @OnClick({R2.id.iv_back, R2.id.tv_deal})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
        } else if (i == R.id.tv_deal) {
            delete();
        }
    }

    private void delete() {
        new AlertDialog.Builder(getContext()).setTitle("编辑").setMessage("长按可移除该视频")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

}
