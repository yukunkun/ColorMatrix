package com.ykk.pluglin_video.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ykk.pluglin_video.BaseFragment;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.ImageAdapter;
import com.ykk.pluglin_video.entity.EyesInfo;
import com.ykk.pluglin_video.entity.ImageInfo;
import com.ykk.pluglin_video.entity.RecInfo;
import com.ykk.pluglin_video.netutils.NetworkUtils;
import com.ykk.pluglin_video.utils.SpacesDoubleDecoration;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class ImageFragment extends BaseFragment {
    String url="https://www.apiopen.top/meituApi";
    int page = 50;
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    List<ImageInfo> jokeInfoList=new ArrayList<>();
    private ImageAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;
    boolean isVertical=true;
    private GridLayoutManager mGridLayoutManager;
    private SpacesDoubleDecoration mSpacesDoubleDecoration;

    public static ImageFragment getInstance() {
        ImageFragment recFragment = new ImageFragment();
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
        mJokeAdapter = new ImageAdapter(getContext(),jokeInfoList);
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
                Random random=new Random();
                int nextInt = random.nextInt(30);
                jokeInfoList.clear();
                mJokeAdapter.notifyDataSetChanged();
                page=nextInt;
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
        Random random=new Random();
        int nextInt = random.nextInt(25);
        NetworkUtils.networkGet(url)
                .addParams("type",3+"")
                .addParams("page",nextInt+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String code = jsonObject.optString("code");
                    if("200".equals(code)){
                        JSONArray data = jsonObject.optJSONArray("data");
                        Gson gson=new Gson();
                        List<ImageInfo> imageInfos =gson.fromJson(data.toString(), new TypeToken<List<ImageInfo>>(){}.getType());
                        jokeInfoList.addAll(imageInfos);
                        mJokeAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
