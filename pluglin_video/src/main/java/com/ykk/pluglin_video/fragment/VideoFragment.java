package com.ykk.pluglin_video.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ykk.pluglin_video.BaseFragment;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.VideoAdapter;
import com.ykk.pluglin_video.entity.EyesInfo;
import com.ykk.pluglin_video.netutils.NetworkUtils;
import com.ykk.pluglin_video.utils.SpacesDoubleDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class VideoFragment extends BaseFragment {
    String url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";
    int page = 1;
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    List<EyesInfo> eyesInfos=new ArrayList<>();
    private VideoAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isVertical=true;
    private GridLayoutManager mGridLayoutManager;
    private SpacesDoubleDecoration mSpacesDoubleDecoration;

    public static VideoFragment getInstance() {
        VideoFragment recFragment = new VideoFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mGridLayoutManager = new GridLayoutManager(getContext(),2);
        if(isVertical){
            mRvJoke.setLayoutManager(mLayoutManager);
        }else {
            mRvJoke.setLayoutManager(mGridLayoutManager);
        }
        mJokeAdapter = new VideoAdapter(getContext(),eyesInfos);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
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
        mJokeAdapter.setTextViewWidth(isTag);
        mJokeAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eyesInfos.clear();
                url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";
                mJokeAdapter.notifyDataSetChanged();
//                page=1;
                getInfo();
                mSw.setRefreshing(false);
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
                //竖向
                if(isVertical){
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                        page++;
                        getInfo();
                    }
                }else {
                    //格子布局
                    int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mGridLayoutManager.getItemCount()-1){
                        page++;
                        getInfo();
                    }
                }

            }
        });
    }

    private void getInfo() {
        NetworkUtils.networkGet(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                    try {
                        Gson gson=new Gson();
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray itemList = jsonObject.optJSONArray("itemList");
                        List<EyesInfo> jokeList = gson.fromJson(itemList.toString(), new TypeToken<List<EyesInfo>>(){}.getType());
                        eyesInfos.addAll(jokeList);
                        for (int i = 0; i < jokeList.size(); i++) {
                            if(!jokeList.get(i).getType().equals("video")){
                                eyesInfos.remove(jokeList.get(i));
                            }
                        }
                        if(jsonObject.optString("nextPageUrl")!=null&&!jsonObject.optString("nextPageUrl").equals("")){
                            url=jsonObject.optString("nextPageUrl");
                        }
                        mJokeAdapter.notifyDataSetChanged();
                        mSw.setRefreshing(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });

    }
}
