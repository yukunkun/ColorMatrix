package com.matrix.yukun.matrix.movie_module.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.activity.TopPresent.TopPresent;
import com.matrix.yukun.matrix.movie_module.activity.TopPresent.TopPresentImpl;
import com.matrix.yukun.matrix.movie_module.activity.adapter.OnEventpos;
import com.matrix.yukun.matrix.movie_module.activity.adapter.TopDetailAdapter;
import com.matrix.yukun.matrix.movie_module.bean.Subjects;
import com.matrix.yukun.matrix.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class TopDetailActivity extends /*MovieBase*/Activity implements TopPresentImpl {
    private TopPresent topPresent;
    private RecyclerView recyclerView;
    private WebView webView;
    private ArrayList<Subjects.DirectorsBean> director;
    private ArrayList<Subjects.CastsBean> actor = new ArrayList<>();
    private ArrayList<TopMovieBean> topMovieBeens=new ArrayList<>();
    ArrayList<String> webUri = new ArrayList<>();
    private ArrayList<String> actAvatar;
    private ArrayList<String> dirAvatar;
    GestureDetector detector;
    private TopDetailAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_detail);
        EventBus.getDefault().register(this);
        topPresent = new TopPresent(this);
        director = getIntent().getParcelableArrayListExtra("director");
        actor = getIntent().getParcelableArrayListExtra("actor");
        actAvatar = getIntent().getStringArrayListExtra("actAvatar");
        dirAvatar = getIntent().getStringArrayListExtra("dirAvatar");
        init();
        setListent();
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
        public  void getelect(OnEventpos onEventpos){
        int pos = onEventpos.pos;
        detailAdapter.getSelectPosition(pos);
        detailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
//        webView.onTouchEvent(ev);//这几行代码也要执行，将webview载入MotionEvent对象一下，况且用载入把，不知道用什么表述合适
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setListent() {
        detector=new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
                if(e1!=null){
                    float beginY = e1.getY();
                    float endY = e2.getY();
                    if(beginY-endY>60&&Math.abs(v)>0){
                        recyclerView.setVisibility(View.GONE);
                        int height= ScreenUtils.instance().getHeight(TopDetailActivity.this);
                        ViewGroup.LayoutParams layoutParams =webView.getLayoutParams();
                        layoutParams.height= (int) (height-60);
                        webView.setLayoutParams(layoutParams);
                    }else if(endY-beginY>60&&Math.abs(v1)>0){   //下滑
//                        MyApp.showToast("down");
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void init() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        webView = (WebView) findViewById(R.id.webview);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < director.size(); i++) {
            String alt = director.get(i).getAlt();
            webUri.add(alt);
            TopMovieBean topMovieBean = new TopMovieBean();
            topMovieBean.setImage(dirAvatar.get(i));
            topMovieBean.setName(director.get(i).getName());
            topMovieBeens.add(topMovieBean);
        }

        for (int i = 0; i < actor.size(); i++) {
            String alt = actor.get(i).getAlt();
            webUri.add(alt);
            TopMovieBean topMovieBean = new TopMovieBean();
            topMovieBean.setImage(actAvatar.get(i));
            topMovieBean.setName(actor.get(i).getName());
            topMovieBeens.add(topMovieBean);
        }
        detailAdapter = new TopDetailAdapter(this, topMovieBeens,topPresent);
        recyclerView.setAdapter(detailAdapter);
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        getInfo(0);
        detailAdapter.getSelectPosition(0);

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void getInfo(int i) {
        webView.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        webView.loadUrl(webUri.get(i));
        webView.setWebViewClient(new HelloWebViewClient());
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(actAvatar!=null){
            actAvatar.clear();
            actAvatar=null;
        }
        if(dirAvatar!=null){
            dirAvatar.clear();
            dirAvatar=null;
        }
        recyclerView=null;
    }
//    @Override
//    //设置回退
//    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        }
//        return false;
//    }
    public void MovieTopBack(View view) {
        finish();
    }
}

