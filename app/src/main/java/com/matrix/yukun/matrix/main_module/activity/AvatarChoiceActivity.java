package com.matrix.yukun.matrix.main_module.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVAvatarAdapter;
import com.matrix.yukun.matrix.main_module.entity.ImageData;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class AvatarChoiceActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    private RecyclerView mRecyclerView;
    private RVAvatarAdapter mRvVerticalAdapter;
    private String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/30/";
    private List<ImageData> mImageDatas = new ArrayList<>();
    private SmartRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager mLayoutManager;
    private Random mRandom = new Random();
    private int page = getRandom();

    public static void start(Context context) {
        Intent intent = new Intent(context, AvatarChoiceActivity.class);
        ((Activity) context).startActivityForResult(intent, 1001);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_avatar_choice;
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.smart_layout);
        mLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRvVerticalAdapter = new RVAvatarAdapter(this, mImageDatas);
        mRecyclerView.setAdapter(mRvVerticalAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));
    }

    public int getRandom() {
        return mRandom.nextInt(40);
    }

    @Override
    public void initDate() {
        OkHttpUtils.get().url(url + page)
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.optJSONArray("results");
                        if (results != null && results.length() > 0) {
                            Gson gson = new Gson();
                            List<ImageData> imageDatas = gson.fromJson(results.toString(), new TypeToken<List<ImageData>>() {
                            }.getType());
                            mImageDatas.addAll(imageDatas);
                            mRvVerticalAdapter.notifyDataSetChanged();

                        } else {
                            ToastUtils.showToast("error");
                        }
                        mSwipeRefreshLayout.finishLoadMore();
                        mSwipeRefreshLayout.finishRefresh();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });
    }

    @Override
    public void initListener() {
        mSwipeRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initDate();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = getRandom();
                mImageDatas.clear();
                initDate();
            }
        });
        mRvVerticalAdapter.setOnItemClickListener(new RVAvatarAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos, String url) {
                Intent intent = new Intent();
                intent.putExtra("pos", pos);
                intent.putExtra("url", url);
                setResult(1002, intent);
                finish();
            }

            @Override
            public void onLoadError(int pos, ImageData imageData) {
                synchronized (this){
                    if(pos<mImageDatas.size()){
                        mImageDatas.remove(pos);
                        mRvVerticalAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
