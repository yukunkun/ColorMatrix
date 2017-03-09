package com.matrix.yukun.matrix.setting_module;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
import com.matrix.yukun.matrix.weather_module.WeatherActivity;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout layout_jianjie;
    private RelativeLayout layout_fankui;
    private RelativeLayout layout_xieyi;
    private RelativeLayout layout_pingfen;
    private RelativeLayout layout_banben;
    private TextView textViewBanben;
    private RelativeLayout layout_movie;
    private RelativeLayout layout_wea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        getVersion();
    }

    private void init() {
        layout_jianjie = (RelativeLayout) findViewById(R.id.rea_jianjie);
        layout_fankui = (RelativeLayout) findViewById(R.id.rea_fankui);
        layout_xieyi = (RelativeLayout) findViewById(R.id.rea_xieyi);
        layout_pingfen = (RelativeLayout) findViewById(R.id.rea_pingfen);
        layout_banben = (RelativeLayout) findViewById(R.id.rea_fankui);
        textViewBanben = (TextView) findViewById(R.id.textview_banben);
        layout_banben = (RelativeLayout) findViewById(R.id.rea_fankui);
        layout_movie = (RelativeLayout) findViewById(R.id.rea_movie);
        layout_wea = (RelativeLayout) findViewById(R.id.rea_wea);
        layout_jianjie.setOnClickListener(this);
        layout_fankui.setOnClickListener(this);
        layout_xieyi.setOnClickListener(this);
        layout_pingfen.setOnClickListener(this);
        layout_movie.setOnClickListener(this);
        layout_wea.setOnClickListener(this);
        //弹簧效果
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView)findViewById(R.id.scrollview));
    }

    private void getVersion() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        textViewBanben.setText("V "+version);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    public void SetBack(View view) {
        finish();
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rea_jianjie:
                Intent intent=new Intent(this,AgreeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
            case R.id.rea_fankui:
                FankuiDialog noteCommentDialog=FankuiDialog.newInstance(0);
                noteCommentDialog.show(getSupportFragmentManager(),"NoteDetailActivity");
                break;
            case R.id.rea_xieyi:
                Intent intent1=new Intent(this,IntroduceActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
            case R.id.rea_pingfen:
                Uri  uri = Uri.parse(AppConstants.YINGYONGBAOPATH);
                Intent  intents = new  Intent(Intent.ACTION_VIEW, uri);
                startActivity(intents);
                break;
            case R.id.rea_movie:
                Intent intent2=new Intent(this,MovieActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
            case R.id.rea_wea:
                Intent intent3=new Intent(this,WeatherActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;

        }
    }
}
