package com.matrix.yukun.matrix.main_module.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.activity.VideoDetailPlayActivity;
import com.matrix.yukun.matrix.main_module.adapter.VideoAdapter;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.search.DBSearchInfo;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.AnimUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class VideoFragment extends BaseFragment implements VideoAdapter.ItemClickCallBack {
    String url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected?num=5&page=0";
    int page = 1;
    @BindView(R.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R.id.sw)
    SwipeRefreshLayout mSw;
    @BindView(R.id.rl_remind)
    RelativeLayout  mLayoutRemind;
    List<EyesInfo> eyesInfos=new ArrayList<>();
    private VideoAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;

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
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new VideoAdapter(getContext(),eyesInfos);
        mRvJoke.setAdapter(mJokeAdapter);
        mJokeAdapter.setItemClickCallBack(this);
        getInfo();
    }

    public void initListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eyesInfos.clear();
                url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected?num=5&page=0";
                mJokeAdapter.notifyDataSetChanged();
                getInfo();
                mSw.setRefreshing(false);
            }
        });

        mRvJoke.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== RecyclerView.SCROLL_STATE_IDLE){
                    //竖向
                    int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                        page++;
                        getInfo();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
                int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
                for (int i = firstVisiblePosition; i <lastVisiblePosition; i++) {
                    View view = mLayoutManager.findViewByPosition(i);
                    TextView textView=view.findViewById(R.id.view_line);
                    if(textView!=null){
                        AnimUtils.setScaleXAnimation(textView);
                    }
                }
            }
        });
    }

    private void getInfo() {
        if(TextUtils.isEmpty(url) || url.equals("null")){
            ToastUtils.showToast("没有更多了");
            return;
        }
        NetworkUtils.networkGet(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //断网，加载本地
                List<EyesInfo> all = DataSupport.findAll(EyesInfo.class);
                if(all.size()>0){
                    eyesInfos.addAll(all);
                    mJokeAdapter.notifyDataSetChanged();
                    mLayoutRemind.setVisibility(View.GONE);
                    mSw.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                    try {
                        Gson gson=new Gson();
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray itemList = jsonObject.optJSONArray("itemList");
                        if(itemList.length()>0){
                            mLayoutRemind.setVisibility(View.GONE);
                        }
                        List<EyesInfo> jokeList = gson.fromJson(itemList.toString(), new TypeToken<List<EyesInfo>>(){}.getType());
                        eyesInfos.addAll(jokeList);
                        url=jsonObject.optString("nextPageUrl");
                        for (int i = 0; i < jokeList.size(); i++) {
                            if(!jokeList.get(i).getType().equals("video")){
                                eyesInfos.remove(jokeList.get(i));
                            }
                        }
                        savaToDB(url);
                        mJokeAdapter.notifyDataSetChanged();
                        mSw.setRefreshing(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    private void savaToDB(String nextUrl) {
        for (int i = 0; i < eyesInfos.size(); i++) {
            List<DBSearchInfo> all = DataSupport.where("title = ?",eyesInfos.get(i).getData().getTitle()).find(DBSearchInfo.class);
            if(all.size()==0){//存储
                DBSearchInfo.countToSearchInfo(eyesInfos.get(i), nextUrl).save();
            }
        }
    }

    @Override
    public void onShareCallback(int pos) {
        EyesInfo eyesInfo = eyesInfos.get(pos);
        ShareDialog shareDialog= ShareDialog.getInstance(eyesInfo.getData().getTitle(),eyesInfo.getData().getPlayUrl(),eyesInfo.getData().getCover().getDetail());
        shareDialog.show(getChildFragmentManager(),"");
    }

    @Override
    public void onItemClickListener(EyesInfo eyesInfo, View view) {
        Intent intent = new Intent(getContext(), VideoDetailPlayActivity.class);
        intent.putExtra("eyesInfo",eyesInfo);
        intent.putExtra("next_url",url);
        intent.putExtra("type",2);//小视频界面要用
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
            getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(),view,"shareView").toBundle());
        }else {
            getContext().startActivity(intent);
            ((Activity)getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
        }
    }

    @Override
    public void onItemCollectClickListener(EyesInfo eyesInfo, View view) {
        CollectsInfo.setCollectInfo(eyesInfo,url);
    }
}
