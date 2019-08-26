package com.matrix.yukun.matrix.main_module.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.selfview.WaterLoadView;

import butterknife.BindView;
import butterknife.OnClick;

public class TextDetailActivity extends BaseActivity {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.rl)
    RelativeLayout mRl;
    @BindView(R2.id.webview)
    WebView mWebview;
    WaterLoadView mProgress;

    @Override
    public int getLayout() {
        return R.layout.activity_text_detail;
    }

    @Override
    public void initView() {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        mProgress=findViewById(R.id.water_load);
        mTvTitle.setText(title);
        mWebview.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        mWebview.loadUrl(url);
        mWebview.setOnKeyListener(mOnKeyListener);
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

    @OnClick(R2.id.iv_back)
    public void onClick() {
        finish();
    }

}
