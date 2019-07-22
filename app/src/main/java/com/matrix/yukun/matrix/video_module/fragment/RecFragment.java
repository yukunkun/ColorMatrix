package com.matrix.yukun.matrix.video_module.fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.btmovie_module.Constant;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.adapter.EyeRecAdapter;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.video_module.entity.EventCategrayPos;
import com.matrix.yukun.matrix.video_module.entity.EventVideo;
import com.matrix.yukun.matrix.video_module.entity.EyesInfo;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.play.VideoDetailPlayActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
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

public class RecFragment extends BaseFragment implements EyeRecAdapter.ShareCallBack, NativeExpressAD.NativeExpressADListener {
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
        mTvRemind.setText("加油奔跑中。。。");
        mLayoutBg.setVisibility(View.VISIBLE);
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new EyeRecAdapter(getContext(),eyesInfos);
        mJokeAdapter.setShareCallBack(this);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
        mSw.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,android.R.color.black,
                 android.R.color.holo_red_light, android.R.color.holo_orange_light
                );
    }

    private void initAdv() {
        NativeExpressAD nativeExpressAD = new NativeExpressAD(getContext(), new ADSize(340, ADSize.AUTO_HEIGHT), Constant.APPID, Constant.BANNER_NATID, this); // 传入Activity
        // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        nativeExpressAD.loadAD(1);
    }

    //分享
    @Override
    public void onShareCallback(int pos) {
        EyesInfo eyesInfo = eyesInfos.get(pos);
        ShareDialog shareDialog=ShareDialog.getInstance(eyesInfo.getData().getTitle(),eyesInfo.getData().getPlayUrl(),eyesInfo.getData().getCover().getDetail());
        shareDialog.show(getChildFragmentManager(),"");
    }

    @Override
    public void onItemClickListener(EyesInfo eyesInfo,View view) {
        Intent intent = new Intent(getContext(), VideoDetailPlayActivity.class);
        intent.putExtra("eyesInfo",eyesInfo);
        intent.putExtra("next_url",url);
        intent.putExtra("type",1);//小视频界面要用
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
            getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(),view,"shareView").toBundle());
        }else {
            getContext().startActivity(intent);
            ((Activity)getContext()).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
        }
    }

    @Override
    public void onItemCollectClickListener(EyesInfo eyesInfo, View view) {
        CollectsInfo.setCollectInfo(eyesInfo,url);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
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
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
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
            }
        });

        mJokeAdapter.getCategroyPos(new EyeRecAdapter.CategroyCallBack() {
            @Override
            public void choosePos(int pos) {
                EventBus.getDefault().post(new EventCategrayPos(pos));
            }
        });

//        mLayoutVideo.setOnTouchListener(new View.OnTouchListener() {
//            private float mX;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                     case MotionEvent.ACTION_DOWN://0
//                         mX = event.getX();
//                         return false;
//                     case MotionEvent.ACTION_UP://1
//                         startPlayActivity();
//                         break;
//                     case MotionEvent.ACTION_MOVE://2
//                         //右滑动移除
//                         float  x = event.getX();
//                         if(x-mX>25){
//                         }
//                        break;
//                }
//                return true;
//            }
//        });
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
                        initAdv();
                        url=jsonObject.optString("nextPageUrl");
//                        mJokeAdapter.notifyDataSetChanged();
                        mLayoutBg.setVisibility(View.GONE);
                        //存储
                        if(isRefresh){
                            isRefresh=false;
                            DataSupport.deleteAll(EyesInfo.class);
                            for (int i = 0; i < eyesInfos.size(); i++) {
                                eyesInfos.get(i).setCover(eyesInfos.get(i).getData().getCover().getDetail());
                                eyesInfos.get(i).setIcon(eyesInfos.get(i).getData().getAuthor().getIcon());
                                eyesInfos.get(i).setCategory(eyesInfos.get(i).getData().getCategory());
                                eyesInfos.get(i).setSlogan(eyesInfos.get(i).getData().getSlogan());
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

    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        EyesInfo eyesInfo=new EyesInfo();
        eyesInfo.setAdvType(1);
        eyesInfo.setNativeExpressADView(list.get(0));
        mJokeAdapter.notifyDataSetChanged();
        LogUtil.i("=======",list.get(0).getBoundData().getTitle());
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onNoAD(AdError adError) {

    }
}
