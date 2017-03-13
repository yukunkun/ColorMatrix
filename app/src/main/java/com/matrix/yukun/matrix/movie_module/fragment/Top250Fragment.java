package com.matrix.yukun.matrix.movie_module.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.BaseFrag;
import com.matrix.yukun.matrix.movie_module.adapter.MovieTopAdapter;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.movie_module.present.Present;
import com.matrix.yukun.matrix.movie_module.present.PresentImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by yukun on 17-3-3.
 */
public class Top250Fragment extends BaseFrag implements PresentImpl {
//    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
//    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;
    private MovieTopAdapter movieTopAdapter;
    private Present present;
    private int pi=1;
    private List<Subjects> subjectsList=new ArrayList<>();
    private boolean mIsRefreshing=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("加载中...");
        progressDialog.show();
        present=new Present(this);
        this.basePresent = present;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_item, null);
        ButterKnife.bind(this, view);
        getViews(view);
        setListener();
        return view;
    }

    @Override
    public void getViews(View view){
        linearLayoutManager = new LinearLayoutManager(getActivity());
        movieTopAdapter = new MovieTopAdapter(getContext(),subjectsList);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(movieTopAdapter);
//        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }


    @Override
    public void showMessage(String msg) {
        MyApp.showToast(msg);
    }

    @Override
    public void getInfo(List<Subjects> list) {
        subjectsList.addAll(list);
        mSwipeRefreshLayout.setRefreshing(false);
        movieTopAdapter.notifyDataSetChanged();
        mIsRefreshing=false;
    }

    @Override
    public void dismissDialogs() {
        progressDialog.dismiss();
    }

    @Override
    public void setListener() {

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == linearLayoutManager.getItemCount() - 1) {
                    //加载更多
                    MyApp.showToast("加载更多");
                    pi=pi+10;
                    present.getInfo(pi);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing=true;
                subjectsList.clear();
                pi=0;
                present.getInfo(pi);
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        );
    }
}
