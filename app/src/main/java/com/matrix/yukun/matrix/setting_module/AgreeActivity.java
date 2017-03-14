package com.matrix.yukun.matrix.setting_module;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.matrix.yukun.matrix.R;
import com.mcxtzhang.pathanimlib.StoreHouseAnimView;
import com.mcxtzhang.pathanimlib.res.StoreHousePath;
import com.mcxtzhang.pathanimlib.utils.PathParserUtils;

public class AgreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings setting = webView.getSettings();
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadUrl("file:///android_asset/introduce.html");

        StoreHouseAnimView mAnimView = (StoreHouseAnimView)findViewById(R.id.storeHouseAnimView);
        mAnimView.setColorBg(Color.WHITE).setColorFg(Color.GREEN);
        mAnimView.setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("MATRIXPHOTO",0.4f,5)));
        mAnimView.setPathMaxLength(200).setAnimTime(2000).startAnim();
    }

    public void IntBack(View view) {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }
}
