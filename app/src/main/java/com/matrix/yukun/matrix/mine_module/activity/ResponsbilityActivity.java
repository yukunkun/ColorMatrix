package com.matrix.yukun.matrix.mine_module.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.mine_module.entity.WebType;

import java.util.ArrayList;
import java.util.List;

public class ResponsbilityActivity extends BaseActivity {

    private int mType;
    private List<String> mList=new ArrayList<>();
    private WebView mWebView;

    public static void start(Context context, int type){
        Intent intent=new Intent(context,ResponsbilityActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_introduce;
    }

    @Override
    public void initView() {
        mType = getIntent().getIntExtra("type", 0);
        mWebView = findViewById(R.id.webview);
        setWebview();
    }

    @Override
    public void initDate() {
        mList.add("file:///android_asset/aggrement.html");
        mList.add("file:///android_asset/responsbility.html");
        mList.add("file:///android_asset/introduce.html");
        mList.add("file:///android_asset/question.html");
        loadWeb();
    }

    private void loadWeb() {
        switch (mType){
            case 0:
                mWebView.loadUrl(mList.get(WebType.AGREEMENT.getType()));
                break;
            case 1:
                mWebView.loadUrl(mList.get(WebType.RESPONSIBILITY.getType()));
                break;
            case 2:
                mWebView.loadUrl(mList.get(WebType.INTRODUCE.getType()));
                break;
            case 3:
                mWebView.loadUrl(mList.get(WebType.QUESTION.getType()));
                break;
        }
    }

    private void setWebview() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);                         // 设置可以与JavaScript交互
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);     //允许js弹出窗口
//        webSettings.setUseWideViewPort(true);                           // 将图片调整到适合WebView的大小
//        webSettings.setLoadWithOverviewMode(true);                      // 缩放至屏幕的大小
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  // 关闭WebView中缓存
//        webSettings.setLoadsImagesAutomatically(true);                  // 支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");                // 设置编码格式
//        webSettings.setSupportZoom(true);                               // 支持缩放，默认为true
//        webSettings.setBuiltInZoomControls(true);                       // 设置内置的缩放控件，若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false);                      // 隐藏原生的缩放控件
//        webSettings.setAllowContentAccess(true);
//        webSettings.setAppCacheEnabled(false);
//        String ua = webSettings.getUserAgentString();
//        webSettings.setUserAgentString(ua + ";spap");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webSettings.setMediaPlaybackRequiresUserGesture(true);// 用户是否需要通过手势播放媒体(不会自动播放)，默认值 true
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);// 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
//        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
    }
    public void IntBacks(View view) {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
