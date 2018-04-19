package com.ykk.pluglin_video.play;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.views.WaterLoadView;

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
    @BindView(R2.id.water_load)
    WaterLoadView mProgress;

    @Override
    public int getLayout() {
        return R.layout.activity_text_detail;
    }

    @Override
    public void initView() {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        mTvTitle.setText(title);
        mWebview.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        mWebview.loadUrl(url);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            mWebview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }

    @OnClick(R2.id.iv_back)
    public void onClick() {
        finish();
    }

}
