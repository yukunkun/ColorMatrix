package com.matrix.yukun.matrix.tool_module.gif.activity;


import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.tool_module.gif.adapter.RVGifAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GIFActivity extends BaseActivity {

    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    @BindView(R2.id.rl_video_contain)
    RelativeLayout mLayoutVideo;
    @BindView(R2.id.jzps_player)
    VideoView mVideoView;
    @BindView(R2.id.iv_close_video)
    ImageView mIvCloseVideo;
    @BindView(R2.id.iv_play_video)
    ImageView mIvVideoPlay;
    @BindView(R2.id.rl_remind)
    RelativeLayout mLayoutBg;
    @BindView(R2.id.tv_remind)
    TextView mTvRemind;
    @BindView(R2.id.iv_back)
    ImageView mIvback;
    private GridLayoutManager mGridLayoutManager;
    private List<String> mList=new ArrayList<>();
    private RVGifAdapter mRvGifAdapter;

    public static  void start(Context context){
        Intent intent=new Intent(context,GIFActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayout() {
        return R.layout.activity_gif;
    }

    @Override
    public void initView() {
        mGridLayoutManager = new GridLayoutManager(this,4);
        mTvRemind.setText("加油奔跑中。。。");
        mLayoutBg.setVisibility(View.VISIBLE);
        mRvJoke.setLayoutManager(mGridLayoutManager);
        mSw.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,android.R.color.black,
                android.R.color.holo_red_light, android.R.color.holo_orange_light
        );
        mRvGifAdapter = new RVGifAdapter(this,mList);
        mRvJoke.setAdapter(mRvGifAdapter);
        mRvJoke.addItemDecoration(new SpacesDoubleDecoration(5));
    }

    @Override
    public void initDate() {
        File file=new File(AppConstant.GIFLOAD);
        if(!file.exists()){
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if(i<25){
                mList.add(listFiles[i].getPath());
            }
        }
        if(mList.size()>0){
            mLayoutBg.setVisibility(View.GONE);
            mRvGifAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initListener() {
        mIvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSw.setRefreshing(false);
            }
        });
    }
}
