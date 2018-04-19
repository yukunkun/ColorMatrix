package com.matrix.yukun.matrix.setting_module;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.chat_module.ChatActivity;
import com.matrix.yukun.matrix.gesture_module.GestureActivity;
import com.matrix.yukun.matrix.image_module.activity.ListDetailActivity;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.wallpaper_module.WallpaperActivity;
import com.matrix.yukun.matrix.weather_module.WeatherActivity;
import com.mcxtzhang.pathanimlib.StoreHouseAnimView;
import com.mcxtzhang.pathanimlib.res.StoreHousePath;
import com.mcxtzhang.pathanimlib.utils.PathParserUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class SettingActivity extends AppCompatActivity {

    @BindView(R2.id.mListview)
    ListView mListview;
    private ArrayList<String> lists=new ArrayList<>();
    private static boolean isNight=false;

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
        SetAdapter setAdapter = new SetAdapter(this);
        mListview.setAdapter(setAdapter);
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
                        Intent intent3=new Intent(SettingActivity.this,WeatherActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 3:
                        File destDis = new File(Environment.getExternalStorageDirectory()+"/yukun");
                        if (!destDis.exists()) {
                            destDis.mkdirs();
                        }
                        lists.clear();
                        File[] filess=destDis.listFiles();
                        for (int i = 0; i < filess.length; i++) {
                            lists.add(filess[i]+"");
                        }
                        //打开Matria图库
                        Intent intent_maps=new Intent(SettingActivity.this,ListDetailActivity.class);
                        intent_maps.putStringArrayListExtra("photo",lists);
                        intent_maps.putExtra("tag",1);
                        startActivity(intent_maps);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 4:
                        Intent intent=new Intent(SettingActivity.this,ChatActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    /*case 6:
                        setNightMode();
                        break;*/
                    case 5:
                        Intent getsure=new Intent(SettingActivity.this,GestureActivity.class);
                        startActivity(getsure);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 6:
                        Intent intentWall=new Intent(SettingActivity.this,WallpaperActivity.class);
                        startActivity(intentWall);
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

    private void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        //  切换模式
        getDelegate().setDefaultNightMode(isNight ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        //  重启Activity
        recreate();
        isNight=!isNight;
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
