package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.TouTiaoActivity;
import com.matrix.yukun.matrix.main_module.adapter.ImageAdapter;
import com.matrix.yukun.matrix.main_module.adapter.ShareCallBack;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.entity.EventShowSecond;
import com.matrix.yukun.matrix.main_module.entity.ImageInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class ImageFragment extends BaseFragment implements ShareCallBack {
    String url="https://www.apiopen.top/meituApi";
    private View mFloor;
    private TwoLevelHeader mHeader;
    private SmartRefreshLayout mSmartRefreshLayout;
    private int page = 40;
    private List<ImageInfo> jokeInfoList=new ArrayList<>();
    private ImageAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mIvRoot;
    private CardView mCardView;
    private SmartRefreshLayout mRefreshGame;
    private ImageView mIvSecondBack;
    private RecyclerView mRvJoke;
    private RelativeLayout mLayoutRemind;

    public static ImageFragment getInstance() {
        ImageFragment recFragment = new ImageFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_image;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mSmartRefreshLayout = inflate.findViewById(R.id.refreshLayout);
        mFloor = inflate.findViewById(R.id.secondfloor);
        mHeader = inflate.findViewById(R.id.header);
        mIvRoot = inflate.findViewById(R.id.secondfloor_content);
        mCardView = inflate.findViewById(R.id.card_view);
        mRefreshGame = inflate.findViewById(R.id.smartrefresh);
        mIvSecondBack = inflate.findViewById(R.id.iv_second_back);
        mRvJoke = inflate.findViewById(R.id.recyclerview);
        mLayoutRemind = inflate.findViewById(R.id.rl_remind);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new ImageAdapter(getContext(),jokeInfoList);
        mJokeAdapter.setShareCallBack(this);
        mRefreshGame.setPrimaryColorsId(R.color.color_2299ee, R.color.color_whit);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo(true);
        setListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getViewpagerPos(EventShowSecond showSecond){
        if(showSecond.selectPosition!=3){
            mHeader.finishTwoLevel();
            mIvRoot.animate().alpha(0).setDuration(1000);
        }
    }

    private void setListener() {
        mSmartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                    page++;
                    getInfo(false);
                }
                mSmartRefreshLayout.finishLoadMore(20);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Random random=new Random();
                int nextInt = random.nextInt(30);
                jokeInfoList.clear();
                mJokeAdapter.notifyDataSetChanged();
                page=nextInt;
                getInfo(true);
                refreshLayout.finishRefresh(20);
            }
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mFloor.setTranslationY(Math.min(offset - mFloor.getHeight(), mSmartRefreshLayout.getLayout().getHeight() - mFloor.getHeight()));
            }
        });

        mHeader.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                mIvRoot.animate().alpha(1).setDuration(2000);
                return true;//true 将会展开二楼状态 false 关闭刷新
            }
        });
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TouTiaoActivity.start(getContext());
                mHeader.finishTwoLevel();
                mIvRoot.animate().alpha(0).setDuration(1000);
            }
        });
        mIvSecondBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeader.finishTwoLevel();
                mIvRoot.animate().alpha(0).setDuration(1000);
            }
        });
    }

    private void getInfo(final boolean isFirst) {
        Random random=new Random();
        int nextInt = random.nextInt(20);
        NetworkUtils.networkGet(url)
                .addParams("type",3+"")
                .addParams("page",nextInt+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                List<ImageInfo> all = DataSupport.findAll(ImageInfo.class);
                if(all.size()>0){
                    jokeInfoList.addAll(all);
                    mJokeAdapter.notifyDataSetChanged();
                    mLayoutRemind.setVisibility(View.GONE);
                    mSmartRefreshLayout.finishLoadMore();
                    mSmartRefreshLayout.finishRefresh();
                }
            }
            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String code = jsonObject.optString("code");
                    if("200".equals(code)){
                        mLayoutRemind.setVisibility(View.GONE);
                        JSONArray data = jsonObject.optJSONArray("data");
                        Gson gson=new Gson();
                        List<ImageInfo> imageInfos = gson.fromJson(data.toString(), new TypeToken<List<ImageInfo>>(){}.getType());
                        jokeInfoList.addAll(imageInfos);
                        mJokeAdapter.notifyDataSetChanged();
                        if(isFirst){
                            DataSupport.deleteAll(ImageInfo.class);
                            for (int i = 0; i < imageInfos.size(); i++) {
                                imageInfos.get(i).save();
                            }
                        }
                    }else {
                        Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
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
        ShareDialog shareDialog= ShareDialog.getInstance(getString(R.string.title_content),imageInfo.getUrl(),imageInfo.getUrl());
        shareDialog.show(getFragmentManager(),"");
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}