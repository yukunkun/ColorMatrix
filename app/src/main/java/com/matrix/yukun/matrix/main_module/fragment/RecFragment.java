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
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.activity.VideoDetailPlayActivity;
import com.matrix.yukun.matrix.main_module.adapter.EyeRecAdapter;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.entity.EventCategrayPos;
import com.matrix.yukun.matrix.main_module.entity.EventVideo;
import com.matrix.yukun.matrix.main_module.entity.EyesInfo;
import com.matrix.yukun.matrix.main_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.AnimUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
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

public class RecFragment extends BaseFragment implements EyeRecAdapter.ShareCallBack {
    String url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected?num=5&page=0";
    @BindView(R2.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;
    List<EyesInfo> eyesInfos=new ArrayList<>();
    @BindView(R2.id.rl_remind)
    RelativeLayout mLayoutBg;
    @BindView(R2.id.tv_remind)
    TextView mTvRemind;
    private int page;
    private EyeRecAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;
    private EventVideo mEventVideo;
    private boolean isRefresh=true;

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
        mJokeAdapter.setShareCallBack(this);
        mRvJoke.setAdapter(mJokeAdapter);
        mTvRemind.setVisibility(View.VISIBLE);
        getInfo();
        mSw.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,android.R.color.black,
                 android.R.color.holo_red_light, android.R.color.holo_orange_light
                );
    }

    //分享
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
        intent.putExtra("type",1);//小视频界面要用
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

//    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=0;
                url = "http://baobab.kaiyanapp.com/api/v4/tabs/selected?num=5&page=0";
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
                if(newState== RecyclerView.SCROLL_STATE_IDLE){
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

        mJokeAdapter.getCategroyPos(new EyeRecAdapter.CategroyCallBack() {
            @Override
            public void choosePos(int pos) {
                EventBus.getDefault().post(new EventCategrayPos(pos));
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
                    mLayoutBg.setVisibility(View.GONE);
                    mSw.setRefreshing(false);
                }
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
                        url=jsonObject.optString("nextPageUrl");
                        mJokeAdapter.notifyDataSetChanged();
                        mLayoutBg.setVisibility(View.GONE);
                        //存储
                        if(isRefresh){
                            isRefresh=false;
                            DataSupport.deleteAll(EyesInfo.class);
                            for (int i = 0; i < eyesInfos.size(); i++) {
                                eyesInfos.get(i).setCover(eyesInfos.get(i).getData().getCover().getDetail());
                                eyesInfos.get(i).setIcon(eyesInfos.get(i).getData().getAuthor().getIcon());
                                eyesInfos.get(i).setCategory(eyesInfos.get(i).getData().getCategory());
                                eyesInfos.get(i).setRemark(eyesInfos.get(i).getData().getAuthor().getName());
                                eyesInfos.get(i).setDescription(eyesInfos.get(i).getData().getDescription());
                                eyesInfos.get(i).save();
                            }
                        }
                    }
                    mSw.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
