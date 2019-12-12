package com.matrix.yukun.matrix.main_module.main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.search.DBSearchInfo;
import com.matrix.yukun.matrix.main_module.search.RVSerchAdapter;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_load_fail)
    ImageView mIvLoadFail;
    @BindView(R.id.sv)
    SearchView mSearchView;
    @BindView(R.id.ll_search_con)
    RelativeLayout mLayout;
    @BindView(R.id.ll_root)
    LinearLayout mLlRoot;
    @BindView(R.id.av_load)
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private int limit=50;
    private EditText mEditText;
    private List<DBSearchInfo> mDBSearchInfoList=new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private RVSerchAdapter mRvSerchAdapter;

    public static void start(Context context, View view){
        Intent intent=new Intent(context,SearchActivity.class);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,view,"shareview").toBundle());
        }else {
            context.startActivity(intent);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
      startShowView();
      initRV();
    }

    private void initRV() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvSerchAdapter = new RVSerchAdapter(this,mDBSearchInfoList);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRvSerchAdapter);
    }

    private void startShowView() {
        mSearchView.setFocusable(true);
        mSearchView.requestFocusFromTouch();
        mSearchView.setQueryHint("输入搜索内容");
        mEditText = mSearchView.findViewById(androidx.appcompat.appcompat.R.id.search_src_text);
        mEditText.setHintTextColor(ContextCompat.getColor(SearchActivity.this, R.color.C4));
        mEditText.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.white));
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.color_1e000000));
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mLlRoot.setOnClickListener(this);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                showLlLoadView(query);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    dismissLoadView();
                }
                return false;
            }
        });
    }

    /**
     *
     * @param query
     */
    private void searchkey(String query) {
        mDBSearchInfoList.clear();
        mRvSerchAdapter.setQuery(query);
        mRvSerchAdapter.notifyDataSetChanged();
        List<DBSearchInfo> publishdate_desc = DataSupport.where("description like ? or title like ? or authorDes like ?","%"+query+"%","%"+query+"%","%"+query+"%").find(DBSearchInfo.class);
        if(publishdate_desc.size()>0){
            for (int i = 0; i < publishdate_desc.size(); i++) {
                if(!TextUtils.isEmpty(publishdate_desc.get(i).getTitle())&&!TextUtils.isEmpty(publishdate_desc.get(i).getAuthorDes())){
                    if(publishdate_desc.get(i).getTitle().indexOf(query)!=-1 || publishdate_desc.get(i).getAuthorDes().indexOf(query)!=-1){
                        publishdate_desc.get(i).setSearchType(1);
                    }
                } else if(!TextUtils.isEmpty(publishdate_desc.get(i).getDescription())&&publishdate_desc.get(i).getDescription().indexOf(query)!=-1){
                    publishdate_desc.get(i).setSearchType(2);
                }
            }
            mRecyclerView.setVisibility(View.VISIBLE);
            mDBSearchInfoList.addAll(publishdate_desc);
            mAVLoadingIndicatorView.setVisibility(View.GONE);
            mIvLoadFail.setVisibility(View.GONE);
            showLlLoadSuccessView();
        }else {
            mRecyclerView.setVisibility(View.GONE);
            mAVLoadingIndicatorView.setVisibility(View.GONE);
            mIvLoadFail.setVisibility(View.VISIBLE);
        }
    }

    private void dismissLoadView(){
        mAVLoadingIndicatorView.setVisibility(View.GONE);
        mIvLoadFail.setVisibility(View.GONE);
        ValueAnimator valueAnimator=ValueAnimator.ofInt(mLayout.getHeight(),0);
        final ViewGroup.LayoutParams layoutParams = mLayout.getLayoutParams();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int values = (int) animation.getAnimatedValue();
                layoutParams.height=values;
                mLayout.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    private void showLlLoadView(final String query) {
        mLayout.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator=ValueAnimator.ofInt(0, ScreenUtils.dp2Px(this,200));
        final ViewGroup.LayoutParams layoutParams = mLayout.getLayoutParams();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int values = (int) animation.getAnimatedValue();
                layoutParams.height=values;
                mLayout.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAVLoadingIndicatorView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!TextUtils.isEmpty(query)){
                    searchkey(query);
                }else{
                    ToastUtils.showToast("请输入内容");
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(600);
        valueAnimator.start();
    }

    private void showLlLoadSuccessView() {
        mLayout.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator=ValueAnimator.ofInt(ScreenUtils.dp2Px(this,200), ScreenUtils.instance().getHeight(this));
        final ViewGroup.LayoutParams layoutParams = mLayout.getLayoutParams();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int values = (int) animation.getAnimatedValue();
                layoutParams.height=values;
                mLayout.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRvSerchAdapter.update(mDBSearchInfoList);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(600);
        valueAnimator.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_root:
            case R.id.iv_back:
                startAnimation(mLayout);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
//            removeSoftKey();
            startAnimation(mLayout);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void removeSoftKey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void startAnimation(View view){
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.color_00000000));
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP && mLayout.getVisibility()== View.VISIBLE) {
            Animator animationTop = ViewAnimationUtils.createCircularReveal(getWindow().getDecorView(), view.getWidth(),
                    0, Math.max(view.getWidth(), view.getHeight()),0);
            animationTop.addListener(new Animator.AnimatorListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAnimationStart(Animator animation) {
                    finishAfterTransition();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mLayout.setVisibility(View.GONE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animationTop.start();
        }else {
            finish();
        }

    }
}
