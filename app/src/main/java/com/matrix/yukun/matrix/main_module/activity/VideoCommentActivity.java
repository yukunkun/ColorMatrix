package com.matrix.yukun.matrix.main_module.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.adapter.RVCommentAdapter;
import com.matrix.yukun.matrix.main_module.entity.VideoCommentBean;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class VideoCommentActivity extends BaseActivity {
    @BindView(R2.id.rl_remind)
    RelativeLayout mRlRemind;
    @BindView(R2.id.iv_close)
    ImageView mIvClose;
    @BindView(R2.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R2.id.ll_contain)
    LinearLayout mLlin;
    private String mVideoId;
    private int page=1;
    List<VideoCommentBean> mVideoCommentBeans=new ArrayList<>();
    private String url="https://www.apiopen.top/satinCommentApi";
    private RVCommentAdapter mRvCommentAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mWidthPixels;
    private float mX;
    private float mY;

    @Override
    public int getLayout() {
        return R.layout.activity_video_comment;
    }

    @Override
    public void initView() {
        mVideoId=getIntent().getStringExtra("videoId");
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRvCommentAdapter = new RVCommentAdapter(this,mVideoCommentBeans);
        mRecyclerView.setAdapter(mRvCommentAdapter);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidthPixels = displayMetrics.widthPixels;
    }

    @Override
    public void initListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItemPosition==mLinearLayoutManager.getItemCount()-1){
                        page++;
                        initDate();
                        mRvCommentAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX()-mX>0&& mX<=mWidthPixels/6
                        && Math.abs(event.getX() - mX) > Math.abs(event.getY() - mY)
                        && Math.abs(event.getX() - mX) > mWidthPixels/2){
                    mLlin.scrollTo(Integer.valueOf((int) -(event.getX() - mX)), 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() < mWidthPixels/2) {
                    // 如果没有拉取过半，则回退
                    mLlin.scrollTo(0, 0);
                } else {
                    if (event.getX() - mX > 0
                            && mX <= mWidthPixels/2
                            && Math.abs(event.getX() - mX) > Math.abs(event.getY() - mY)
                            && Math.abs(event.getX() - mX) > mWidthPixels/6) {
                        finish();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void initDate() {
        OkHttpUtils.post().url(url)
                .addParams("id",mVideoId)
                .addParams("page",page+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.optInt("code")==200){
                        JSONObject data = jsonObject.optJSONObject("data");
                        JSONObject hot = data.getJSONObject("hot");
                        JSONArray list = hot.optJSONArray("list");
                        if(list.length()>0){
                            mRlRemind.setVisibility(View.GONE);
                            Gson gson=new Gson();
                            List<VideoCommentBean> videoComment= gson.fromJson(list.toString(), new TypeToken<List<VideoCommentBean>>(){}.getType());
                            mVideoCommentBeans.addAll(videoComment);
                            mRvCommentAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtils.showToast("没有更多评论");
                        }
                    }else {
                        ToastUtils.showToast("获取评论失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R2.id.iv_close})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_close) {
            finish();
        }
    }
}
