package com.matrix.yukun.matrix.setting_module;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.gesture_module.GestureActivity;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.LeShiListActivity;
import com.matrix.yukun.matrix.leshilive_module.LeShiLiveActivity;
import com.matrix.yukun.matrix.leshilive_module.LiveActivity;
import com.matrix.yukun.matrix.leshilive_module.LiveListActivity;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.weather_module.WeatherActivity;
import com.mcxtzhang.pathanimlib.StoreHouseAnimView;
import com.mcxtzhang.pathanimlib.res.StoreHousePath;
import com.mcxtzhang.pathanimlib.utils.PathParserUtils;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.mListview)
    ListView mListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        StoreHouseAnimView mAnimView = (StoreHouseAnimView)findViewById(R.id.storeHouseAnimView);
        mAnimView.setColorBg(Color.WHITE).setColorFg(Color.BLACK);
        mAnimView.setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("SETTING",0.4f,5)));
        mAnimView.setPathMaxLength(400).setAnimTime(2000).startAnim();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        mListview.setAdapter(new SetAdapter(getApplicationContext()));
        OverScrollDecoratorHelper.setUpOverScroll(mListview);
    }

    private void setListener() {
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Resources res=getResources();
                        Bitmap bmp= BitmapFactory.decodeResource(res,R.mipmap.tool_icon);
                        loadImage(bmp);
                        break;
                    case 1:
                        Intent intent2=new Intent(SettingActivity.this,MovieActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 2:
                        Intent intent4=new Intent(SettingActivity.this,LeShiListActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 3:
                        Intent intent3=new Intent(SettingActivity.this,WeatherActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 4:
                        Intent getsure=new Intent(SettingActivity.this,GestureActivity.class);
                        startActivity(getsure);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 5:
                        Intent intent=new Intent(SettingActivity.this,LiveActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 6:
                        Intent intent1=new Intent(SettingActivity.this,LeShiLiveActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 7:
                        Intent intentUs=new Intent(SettingActivity.this,AboutUsActivity.class);
                        startActivity(intentUs);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                }
            }
        });
    }
    //下载图标
    private void loadImage(final Bitmap bmp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtil.loadImage(bmp, "matrix_icon.png");
            }
        }).start();
    }

    public void SetingBack(View view) {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
