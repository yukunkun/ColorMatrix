package com.ykk.pluglin_video.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ykk.pluglin_video.BaseFragment;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.ImageAdapter;
import com.ykk.pluglin_video.entity.RecInfo;
import com.ykk.pluglin_video.netutils.NetworkUtils;
import com.ykk.pluglin_video.utils.SpacesDoubleDecoration;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class ImageFragment extends BaseFragment {
    String url = "http://lf.snssdk.com/neihan/stream/mix/v1/?content_type=-103";
    int page = 1;
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    List<RecInfo> jokeInfoList=new ArrayList<>();
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
                jokeInfoList.clear();
                mJokeAdapter.notifyDataSetChanged();
                page=1;
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
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataArr = jsonObject.optJSONObject("data");
                    JSONArray data = dataArr.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        RecInfo recInfo=new RecInfo();
                        JSONObject jsonObject1 = data.optJSONObject(i);
                        JSONObject group = jsonObject1.optJSONObject("group");
                        recInfo.setText(group.optString("text"));
                        recInfo.setShare_url(group.optString("share_url"));
                        recInfo.setCreate_time(group.optString("create_time"));
                        recInfo.setPlay_time(group.optLong("comment_count"));

                        JSONObject user = group.optJSONObject("user");
                        recInfo.setUser_name(user.optString("name"));
                        recInfo.setHeader(user.optString("avatar_url"));
                        JSONObject covers = group.optJSONObject("large_image");
                        JSONArray cover_url = covers.optJSONArray("url_list");
                        recInfo.setCover(cover_url.optJSONObject(0).optString("url"));
                        int media_type = group.optInt("media_type");
                        //判断GIF
                        if(media_type==1){
                            recInfo.setGif(false);
                        }else {
                            recInfo.setGif(true);
                        }
                        jokeInfoList.add(recInfo);
                    }
                    mJokeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
