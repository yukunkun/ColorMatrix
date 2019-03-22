package com.matrix.yukun.matrix.video_module.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.adapter.TextAdapter;
import com.matrix.yukun.matrix.video_module.entity.NewsInfo;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.video_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.video_module.views.SwipeItemLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class TextFragment extends BaseFragment {
    String url="https://www.apiopen.top/journalismApi";
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    @BindView(R2.id.rl_remind)
    RelativeLayout mLayoutRemind;
    List<NewsInfo> jokeInfoList=new ArrayList<>();
    private TextAdapter mTextAdapter;
    private LinearLayoutManager mLayoutManager;
    boolean isVertical=true;
    private GridLayoutManager mGridLayoutManager;
    private SpacesDoubleDecoration mSpacesDoubleDecoration;

    public static TextFragment getInstance() {
        TextFragment recFragment = new TextFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    public void getLayoutTag(boolean isTag){
        isVertical=isTag;
        if(isVertical){
            mRvJoke.setLayoutManager(mLayoutManager);
        }else {
            mRvJoke.setLayoutManager(mGridLayoutManager);
            mSpacesDoubleDecoration=new SpacesDoubleDecoration(0,1,1,0);
            mRvJoke.addItemDecoration(mSpacesDoubleDecoration);
        }
        mTextAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mGridLayoutManager=new GridLayoutManager(getContext(),2);
        if(isVertical){
            mRvJoke.setLayoutManager(mLayoutManager);
        }else {
            mRvJoke.setLayoutManager(mGridLayoutManager);
        }
        mTextAdapter = new TextAdapter(getContext(),jokeInfoList);
        mRvJoke.setAdapter(mTextAdapter);
        mRvJoke.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        mRvJoke.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        getOneDayInfo();
        setListener();
    }

    private void setListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jokeInfoList.clear();
                getOneDayInfo();
            }
        });

        mRvJoke.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isVertical){
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
//                        getOneDayInfo();
                    }
                }else {
                    //格子布局
                    int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mGridLayoutManager.getItemCount()-1){
//                        getOneDayInfo();
                    }
                }
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
                    JSONObject data = jsonObject.optJSONObject("data");
                    JSONArray tech = data.optJSONArray("dy");
                    JSONArray sports = data.optJSONArray("auto");
                    JSONArray auto = data.optJSONArray("sports");
                    JSONArray money = data.optJSONArray("money");
                    Gson gson = new Gson();
                    List<NewsInfo> techlist = gson.fromJson(tech.toString(), new TypeToken<List<NewsInfo>>() {}.getType());
                    List<NewsInfo> moneylist = gson.fromJson(money.toString(), new TypeToken<List<NewsInfo>>() {}.getType());
                    List<NewsInfo> sportlist = gson.fromJson(sports.toString(), new TypeToken<List<NewsInfo>>() {}.getType());
                    List<NewsInfo> autolist = gson.fromJson(auto.toString(), new TypeToken<List<NewsInfo>>() {}.getType());

                    jokeInfoList.addAll(techlist);
                    jokeInfoList.addAll(moneylist);
                    jokeInfoList.addAll(sportlist);
                    jokeInfoList.addAll(autolist);
                    if(jokeInfoList.size()>0){
                        mLayoutRemind.setVisibility(View.GONE);
                    }

                    mSw.setRefreshing(false);
                    mTextAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
