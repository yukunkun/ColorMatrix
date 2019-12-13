package com.matrix.yukun.matrix.main_module.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.dialog.ImageDownLoadDialog;
import com.matrix.yukun.matrix.util.StatusBarUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import uk.co.senab.photoview.PhotoView;

public class MapActivity extends BaseActivity {

    @BindView(R.id.photoview)
    PhotoView mPhotoview;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_images)
    ImageView mIvImage;
    @BindView(R.id.tv_clip)
    TextView mTvImage;
    @BindView(R.id.tv_sup)
    TextView mTvSup;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.webview)
    WebView mWebView;
    private String recomendMap="https://cdn.mom1.cn/?mom=json";
    private String beaudyurl="https://uploadbeta.com/api/pictures/random/?key=%E6%8E%A8%E5%A5%B3%E9%83%8E";
    private String downlaoUrl="https://uploadbeta.com/api/pictures/random/?key=BingEverydayWallpaperPicture";
    private boolean mIsGif;

    public static void start(Context context){
        Intent intent=new Intent(context,MapActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_map;
    }

    @Override
    public void initView() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        setImage(downlaoUrl);
        mProgressBar.setVisibility(View.GONE);
    }

    private void setImage(String url) {
        mWebView.loadUrl(url);
//        RequestOptions options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//        Glide.with(this).load(url).apply(options).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                ToastUtils.showToast(e.getMessage());
//                mProgressBar.setVisibility(View.GONE);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                mProgressBar.setVisibility(View.GONE);
//                return false;
//            }
//        }).into(mPhotoview);
    }

    @OnClick({R.id.iv_more, R.id.iv_back,R.id.tv_clip,R.id.tv_sup})

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_more) {
            ImageDownLoadDialog imageDownLoadDialog= ImageDownLoadDialog.getInstance(downlaoUrl);
            imageDownLoadDialog.show(getSupportFragmentManager(),"");
        } else if (i == R.id.iv_back) {
            finish();
        }else if(i==R.id.tv_clip){
            mWebView.loadUrl(downlaoUrl);
//            mProgressBar.setVisibility(View.VISIBLE);
//            setImage(beaudyurl);
//            initUrl();
//            mProgressBar.setVisibility(View.VISIBLE);
        }else if (i == R.id.tv_sup) {
            setImage(beaudyurl);
        }
    }

    public void initUrl() {
        OkHttpUtils.get().url(recomendMap).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if(!TextUtils.isEmpty(response)){
                    int indexOf = response.indexOf("{");
                    String data=response.substring(indexOf,response.length());
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        if(jsonObject.optString("result").equals("200")){
                            downlaoUrl="http:"+jsonObject.optString("img");
                            setImage(downlaoUrl);
                        }else {
                            downlaoUrl=beaudyurl;
                            setImage(downlaoUrl);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    downlaoUrl=beaudyurl;
                    setImage(downlaoUrl);
                }
            }
        });
    }
}
