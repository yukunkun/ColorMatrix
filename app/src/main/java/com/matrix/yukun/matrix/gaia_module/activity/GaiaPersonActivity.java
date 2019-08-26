package com.matrix.yukun.matrix.gaia_module.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.CoverImgInfo;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.CreateAdapter;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.CreatePersonInfo;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.WorksInfo;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.gaia_module.net.GaiCallBack;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;

public class GaiaPersonActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.sm_refresh)
    SmartRefreshLayout smRefresh;
    LinearLayoutManager linearLayoutManager;
    private List<CreatePersonInfo> createPersonList=new ArrayList<>();
    private List<WorksInfo> worksList=new ArrayList<>();
    private int pi=1;
    private CreateAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, GaiaPersonActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_gaia_person;
    }

    @Override
    public void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.addItemDecoration(new SpacesDoubleDecoration(20));
        mAdapter = new CreateAdapter(this,createPersonList,worksList);
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void initDate() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("s", 0);
            jsonObject.put("t", 1);
            jsonObject.put("opr", 0);
            jsonObject.put("ps", 10);
            jsonObject.put("pi", pi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        LogUtil.i("===",jsonObject.toString());
        OkHttpUtils.postString().url(Api.BASE_URL + Api.PERSONURL)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("Set-Cookie","t=85AFDEFC7A1541E5902E20931D12DA70")
                .build().execute(new GaiCallBack() {

            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                if(data!=null){
                    try {
                        parasJson(new JSONObject(response));
                        mAdapter.notifyDataSetChanged();
                        smRefresh.finishLoadMore();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    ToastUtils.showToast("没有更多了");
                }
            }

            @Override
            public void onDateError(String error) {

            }
        });
    }

    private void parasJson(JSONObject response) {

        JSONArray a = response.optJSONArray("a");
        for (int m = 0; m < a.length(); m++) {
            CreatePersonInfo personInfo=new CreatePersonInfo();
            JSONObject jsonObject = a.optJSONObject(m);
            personInfo.setAddress(jsonObject.optString("address"));
            personInfo.setSignature(jsonObject.optString("signature"));
            personInfo.setNickName(jsonObject.optString("nickName"));
            personInfo.setCreateCount(jsonObject.optInt("createCount"));
            personInfo.setBrowseCount(jsonObject.optInt("browseCount"));
            personInfo.setLikeCount(jsonObject.optInt("likeCount"));
            personInfo.setId(jsonObject.optInt("id"));
            personInfo.setAvatar(jsonObject.optString("avatar"));
            personInfo.setFocus(jsonObject.optInt("focus"));
            createPersonList.add(personInfo);
        }

        JSONObject o = response.optJSONObject("o");
        JSONArray works = o.optJSONArray("works");
        for (int i = 0; i < works.length(); i++) {
            WorksInfo worksInfo=new WorksInfo();
            JSONArray jsonArray = works.optJSONArray(i);
            ArrayList<CoverImgInfo> strings=new ArrayList<>();
            for (int j = 0; j < jsonArray.length(); j++) {
                CoverImgInfo imgInfo=new CoverImgInfo();
                JSONObject jsonObject = jsonArray.optJSONObject(j);
                imgInfo.setCover(jsonObject.optString("cover"));
                imgInfo.setScreenshot(jsonObject.optString("screenshot"));
                imgInfo.setId(jsonObject.optInt("id"));
                imgInfo.setType(jsonObject.optInt("type"));
                imgInfo.setFlag(jsonObject.optInt("flag"));
                strings.add(imgInfo);
            }
            worksInfo.setCoverImgInfos(strings);
            worksList.add(worksInfo);
        }

    }

    @Override
    public void initListener() {
        smRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                worksList.clear();
                createPersonList.clear();
                mAdapter.notifyDataSetChanged();
                pi=1;
                initDate();
                smRefresh.finishRefresh();
            }
        });
        smRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pi++;
                initDate();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                GaiaSearchActivity.start(this);
                break;
        }
    }
}
