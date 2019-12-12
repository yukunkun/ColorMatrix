package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVVideoDetailAdapter;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * author: kun .
 * date:   On 2018/12/27
 */
public class VideoListFragment extends BaseFragment {

    private String mNextUrl;
    private List<EyesInfo> eyesInfos=new ArrayList<>();
    private RelativeLayout mLayoutBg;
    private SwipeRefreshLayout mSRlayout;
    private RecyclerView mRecyclerView;
    private RVVideoDetailAdapter mRvVideoDetailAdapter;
    private EyesInfo mEyesInfo;
    private int mType;

    public static VideoListFragment getInstance(EyesInfo eyesInfo, String nextUrl, int type){
        VideoListFragment videoListFragment=new VideoListFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("eyesInfo",eyesInfo);
        bundle.putString("nextUrl",nextUrl);
        bundle.putInt("type",type);
        videoListFragment.setArguments(bundle);
        return videoListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNextUrl = getArguments().getString("nextUrl");
        mEyesInfo = (EyesInfo) getArguments().getSerializable("eyesInfo");
        mType=getArguments().getInt("type");
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLayoutBg = inflate.findViewById(R.id.rl_remind);
        mSRlayout = inflate.findViewById(R.id.sw);
        mRecyclerView = inflate.findViewById(R.id.rv_joke);
        initData();
        getInfo();
    }

    public void initListener() {
        mSRlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSRlayout.setRefreshing(false);
            }
        });
    }

    private void initData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRvVideoDetailAdapter = new RVVideoDetailAdapter(getContext(),eyesInfos,mNextUrl,mType);
        mRecyclerView.setAdapter(mRvVideoDetailAdapter);
    }


    private void getInfo() {
        if(TextUtils.isEmpty(mNextUrl) || mNextUrl.equals("null")){
            eyesInfos.add(mEyesInfo);
            mRvVideoDetailAdapter.notifyDataSetChanged();
            mLayoutBg.setVisibility(View.GONE);
            return;
        }
        NetworkUtils.networkGet(mNextUrl)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Gson gson=new Gson();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray itemList = jsonObject.optJSONArray("itemList");
                    if(itemList!=null&&itemList.length()>0){
                        List<EyesInfo> jokeList = gson.fromJson(itemList.toString(), new TypeToken<List<EyesInfo>>(){}.getType());
                        eyesInfos.addAll(jokeList);
                        for (int i = 0; i < jokeList.size(); i++) {
                            if(!jokeList.get(i).getType().equals("video")){
                                eyesInfos.remove(jokeList.get(i));
                            }
                        }
                        mRvVideoDetailAdapter.notifyDataSetChanged();
                        mLayoutBg.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
