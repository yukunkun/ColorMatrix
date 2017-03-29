package com.matrix.yukun.matrix.leshilive_module;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshi_module.present.LeShiAdapter;
import com.matrix.yukun.matrix.leshi_module.present.LeShiListImple;
import com.matrix.yukun.matrix.leshilive_module.bean.LiveListBean;
import com.matrix.yukun.matrix.leshilive_module.present.LiveListAdapter;
import com.matrix.yukun.matrix.leshilive_module.present.LiveListPresent;
import com.matrix.yukun.matrix.movie_module.MovieBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveListActivity extends MovieBaseActivity<LiveListPresent> implements LeShiListImple {

    @BindView(R.id.liveback)
    ImageView liveback;
    @BindView(R.id.storeHouseAnimView)
    TextView storeHouseAnimView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    private LiveListPresent liveListPresent;
    List<LiveListBean> liveListBeen=new ArrayList<>();
    private LiveListAdapter liveListAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_le_shi_list);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        setListener();
        getViews();
        liveListPresent = new LiveListPresent(this);
        basePresent=liveListPresent;
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.liveback, R.id.swipe})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liveback:
                finish();
                break;

        }
    }

    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void getInfo(List<ListBean> list) {

    }

    public void getliveInfo(List<LiveListBean> list) {
        liveListBeen.addAll(list);
        liveListAdapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
        Toast.makeText(this, "加载更多...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissDialogs() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void setListener() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                liveListBeen.clear();
                liveListPresent.getInfo();
            }
        });
    }

    @Override
    public void getViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        liveListAdapter = new LiveListAdapter(this,liveListBeen);
        recyclerView.setAdapter(liveListAdapter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        liveListBeen.clear();
    }
}
