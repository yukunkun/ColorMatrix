package com.ykk.pluglin_video.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.EyeRecAdapter;
import com.ykk.pluglin_video.adapter.RecAdapter;
import com.ykk.pluglin_video.entity.EventCategrayPos;
import com.ykk.pluglin_video.entity.EyesInfo;
import com.ykk.pluglin_video.netutils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ykk.pluglin_video.BaseFragment;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class RecFragment extends BaseFragment {
    String url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    List<EyesInfo> eyesInfos=new ArrayList<>();
    private EyeRecAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;

    public static RecFragment getInstance() {
        RecFragment recFragment = new RecFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new EyeRecAdapter(getContext(),eyesInfos);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
        mSw.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,android.R.color.black,
                 android.R.color.holo_red_light, android.R.color.holo_orange_light
                );
        mSw.setRefreshing(true);

    }

    private void setListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";
                eyesInfos.clear();
                mJokeAdapter.notifyDataSetChanged();
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
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                    getInfo();
                }
            }
        });

        mJokeAdapter.getCategroyPos(new RecAdapter.CategroyCallBack() {
            @Override
            public void choosePos(int pos) {
                EventBus.getDefault().post(new EventCategrayPos(pos));
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
