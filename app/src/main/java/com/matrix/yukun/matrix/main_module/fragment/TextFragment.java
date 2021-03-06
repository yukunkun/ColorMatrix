package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.TouTiaoActivity;
import com.matrix.yukun.matrix.main_module.adapter.TextAdapter;
import com.matrix.yukun.matrix.main_module.entity.EventShowSecond;
import com.matrix.yukun.matrix.main_module.entity.NewsInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.views.SwipeItemLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

    /* https://cdn.mom1.cn/?mom=url //随机图片
     * http://lorempixel.com/1600/900
     * https://unsplash.it/1600/900?random（国内加载略慢）
     * https://uploadbeta.com/api/pictures/random/?key=BingEverydayWallpaperPicture【返回必应图片】
     * https://uploadbeta.com/api/pictures/random/?key=%E6%8E%A8%E5%A5%B3%E9%83%8E【随机美女图片】
     * http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1（必应返回JSON数据，具体百度）
     * http://s.cn.bing.net
     */

public class TextFragment extends BaseFragment {
    //来源 https://blog.csdn.net/qq_41212530/article/details/85693158
    String url="http://c.m.163.com/nc/article/headline/T1348647853363/0-40.html";
    List<NewsInfo> jokeInfoList=new ArrayList<>();
    private TextAdapter mTextAdapter;
    private LinearLayoutManager mLayoutManager;
    private TwoLevelHeader mHeader;
    private SmartRefreshLayout mSmartRefreshLayout;
    private RelativeLayout mIvRoot;
    private CardView mCardView;
    private SmartRefreshLayout mRefreshGame;
    private ImageView mIvSecondBack;
    private RecyclerView mRvJoke;
    private RelativeLayout mLayoutRemind;
    private View mFloor;

    public static TextFragment getInstance() {
        TextFragment recFragment = new TextFragment();
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
        mRefreshGame.setPrimaryColorsId(R.color.color_2299ee, R.color.color_whit);
        mRvJoke.setLayoutManager(mLayoutManager);
        mTextAdapter = new TextAdapter(getContext(),jokeInfoList);
        mRvJoke.setAdapter(mTextAdapter);
        mRvJoke.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        mRvJoke.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        getOneDayInfo();
        setListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getViewpagerPos(EventShowSecond showSecond){
        if(showSecond.selectPosition!=5){
            mHeader.finishTwoLevel();
            mIvRoot.animate().alpha(0).setDuration(1000);
        }
    }
    private void setListener() {
        mSmartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mSmartRefreshLayout.finishLoadMore(20);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                jokeInfoList.clear();
                getOneDayInfo();
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

    private void getOneDayInfo() {
        NetworkUtils.networkGet(url)
                .build()
                .execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.optJSONArray("T1348647853363");
                    Gson gson = new Gson();
                    List<NewsInfo> autolist = gson.fromJson(data.toString(), new TypeToken<List<NewsInfo>>() {}.getType());
                    if(autolist!=null&&autolist.size()>0){
                        autolist.remove(0);
                        jokeInfoList.addAll(autolist);
                        if(jokeInfoList.size()>0){
                            mLayoutRemind.setVisibility(View.GONE);
                        }
                        mSmartRefreshLayout.finishLoadMore();
                        mSmartRefreshLayout.finishRefresh();
                        mTextAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
