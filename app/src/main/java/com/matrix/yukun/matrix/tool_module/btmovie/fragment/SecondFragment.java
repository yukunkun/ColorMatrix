package com.matrix.yukun.matrix.tool_module.btmovie.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.btmovie.Constant;

/**
 * author: kun .
 * date:   On 2018/9/25
 */
public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout mRlBanner;
    private ImageView mIvClose;
    private WebView mWebView;
    private RelativeLayout mRlBannerLayout;
    private ProgressDialog progressBar;
    private FloatingActionButton mFloatingActionButton;

    public static SecondFragment newInstance(){
        return new SecondFragment();
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
        mFloatingActionButton = (FloatingActionButton) inflate.findViewById(R.id.bt_add_conference);
        mWebView = (WebView) inflate.findViewById(R.id.webview);
        progressBar= new ProgressDialog(getContext());
        progressBar.setMessage("加载中");
//        progressBar.show();
        mWebView.getSettings().setJavaScriptEnabled(true);//启用js
        mWebView.getSettings().setBlockNetworkImage(false);//解决图片不显示
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(Constant.BTBTDY);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.iv_banner_close){
            mRlBannerLayout.setVisibility(View.GONE);
        }

        if(id== R.id.bt_add_conference){
            String url = mWebView.getUrl();
            if(!TextUtils.isEmpty(url)&& url.startsWith("thunder:")){
                try{
                    String link = url;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    intent.addCategory("android.intent.category.DEFAULT");
                    startActivity(intent);
                }catch(Exception e){
                    ToastUtils.showToast( "没有安装迅雷，请安装迅雷下载");
                }
            }else {
                //代码实现跳转
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                //此处填链接
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        }
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
}
