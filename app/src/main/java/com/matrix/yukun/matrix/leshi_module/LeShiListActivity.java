package com.matrix.yukun.matrix.leshi_module;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.bean.ListBean;
import com.matrix.yukun.matrix.leshi_module.present.LeShiAdapter;
import com.matrix.yukun.matrix.leshi_module.present.LeShiListImple;
import com.matrix.yukun.matrix.leshi_module.present.LeShiPresent;
import com.matrix.yukun.matrix.movie_module.MovieBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeShiListActivity extends AppCompatActivity implements LeShiListImple{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    private LeShiPresent leShiPresent;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;
    List<ListBean> list=new ArrayList<>();
    private LeShiAdapter leShiAdapter;
    private int index=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_shi_list);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        getViews();
        setListener();
        leShiPresent = new LeShiPresent(this);
        leShiPresent.getInfo(1);
    }


    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void getInfo(List<ListBean> lists) {
        if(lists.size()>0){
            list.addAll(lists);
            leShiAdapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
            Toast.makeText(LeShiListActivity.this, "加载更多...", Toast.LENGTH_SHORT).show();
        }
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
                list.clear();
                index=1;
                leShiPresent.getInfo(index);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==list.size()-1){
                    index++;
                    leShiPresent.getInfo(index);
                }
            }
        });
    }

    @Override
    public void getViews() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        leShiAdapter = new LeShiAdapter(this,list);
        recyclerView.setAdapter(leShiAdapter);
    }

    public void LeSHiListBack(View view) {
        finish();
    }
}
