package com.matrix.yukun.matrix.main_module.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.selfview.WaterLoadView;

import butterknife.BindView;
import butterknife.OnClick;

public class TextDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    @BindView(R.id.webview)
    WebView mWebview;
    WaterLoadView mProgress;
    private String mUrl;
    private int mType;
    private String mTitle;

    public static void start(Context context,String title,String url){
        Intent intent=new Intent(context,TextDetailActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void start(Context context,String title,String url,View view){
        Intent intent=new Intent(context,TextDetailActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT_WATCH){
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context,view,"shareView").toBundle());
        }else {
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
        }
    }


    @Override
    public int getLayout() {
        return R.layout.activity_text_detail;
    }

    @Override
    public void initView() {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        mType=getIntent().getIntExtra("type",0);
        mProgress=findViewById(R.id.water_load);
        mTvTitle.setText(mTitle);
        //加载需要显示的网页
        if(TextUtils.isEmpty(mUrl)){
            ToastUtils.showToast("页面已飞走");
            mProgress.setVisibility(View.GONE);
            return;
        }
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);     //允许js弹出窗口
        webSettings.setUseWideViewPort(true);                           // 将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true);                      // 缩放至屏幕的大小
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  // 关闭WebView中缓存
        webSettings.setLoadsImagesAutomatically(true);                  // 支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");                // 设置编码格式
        webSettings.setSupportZoom(true);                               // 支持缩放，默认为true
        webSettings.setBuiltInZoomControls(true);                       // 设置内置的缩放控件，若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false);                      // 隐藏原生的缩放控件
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);// 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
        }
        mWebview.setOnKeyListener(mOnKeyListener);
        mWebview.loadUrl(mUrl);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                Uri uri = Uri.parse(mUrl);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                Uri uri = Uri.parse(mUrl);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    View.OnKeyListener mOnKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {  //表示按返回键
                    mWebview.goBack();   //后退
                    //webview.goForward();//前进
                    return true;    //已处理
                }
            }
            return false;
        }
    };

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

}
