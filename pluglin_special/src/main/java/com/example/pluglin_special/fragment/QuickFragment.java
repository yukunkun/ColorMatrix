package com.example.pluglin_special.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.pluglin_special.BaseFragment;
import com.example.pluglin_special.Constant;
import com.example.pluglin_special.R;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import butterknife.OnClick;

/**
 * author: kun .
 * date:   On 2018/9/25
 */
public class QuickFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mRlBanner;
    private ImageView mIvClose;
    private WebView mWebView;
    private BannerView mBannerView;
    private RelativeLayout mRlBannerLayout;
    private ProgressDialog progressBar;
    private FloatingActionButton mFloatingActionButton;

    public static QuickFragment newInstance(){
        return new QuickFragment();
    }
    @Override
    public int getLayout() {
        return R.layout.fragment_second_layout;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mRlBanner = (RelativeLayout) inflate.findViewById(R.id.rl_banner);
        mRlBannerLayout = (RelativeLayout) inflate.findViewById(R.id.rl_banner_layout);
        mIvClose = (ImageView) inflate.findViewById(R.id.iv_banner_close);
        mWebView = (WebView) inflate.findViewById(R.id.webview);
        mFloatingActionButton = (FloatingActionButton) inflate.findViewById(R.id.bt_add_conference);
        progressBar= new ProgressDialog(getContext());
        progressBar.setMessage("加载中");
        progressBar.show();
        getBanner();

        mWebView.getSettings().setJavaScriptEnabled(true);//启用js
        mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(Constant.QUICK);
    }

    @Override
    public void initListener() {
        mIvClose.setOnClickListener(this);
        //点击后退按钮,让WebView后退一页
        mWebView.setOnKeyListener(mOnKeyListener);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.dismiss();
            }

        });

        mFloatingActionButton.setOnClickListener(this);
    }

    View.OnKeyListener mOnKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  //表示按返回键
                    mWebView.goBack();   //后退
                    //webview.goForward();//前进
                    return true;    //已处理
                }
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.iv_banner_close){
            mRlBannerLayout.setVisibility(View.GONE);
        }
        if(id==R.id.bt_add_conference){
            String url = mWebView.getUrl();
            //代码实现跳转
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            //此处填链接
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    private void getBanner() {
        mBannerView = new BannerView(getActivity(), ADSize.BANNER, Constant.APPID,
                Constant.BANNER_ADID);
        mBannerView.setRefresh(30);
        mBannerView.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {
                Log.i("---onNoAD", i + "");
            }

            @Override
            public void onADReceiv() {
                Log.i("---onNoAD", "onNoAD");
            }
        });
        mRlBanner.addView(mBannerView);// 把banner加载到容器
        mBannerView.loadAD();
    }

}
