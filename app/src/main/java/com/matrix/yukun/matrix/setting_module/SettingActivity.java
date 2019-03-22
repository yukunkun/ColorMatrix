package com.matrix.yukun.matrix.setting_module;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.gesture_module.GestureActivity;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.video_module.play.MyGallaryActivity;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.matrix.yukun.matrix.wallpaper_module.WallpaperActivity;
import com.mcxtzhang.pathanimlib.StoreHouseAnimView;
import com.mcxtzhang.pathanimlib.res.StoreHousePath;
import com.mcxtzhang.pathanimlib.utils.PathParserUtils;
import com.tencent.bugly.beta.Beta;
import java.util.ArrayList;
import java.util.List;

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
                List<Class> classes=new ArrayList<>();
                Class aClass=null;
                switch (position) {
                    case 0:
                        Resources res = getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.tool_icon);
                        loadImage(bmp);
                        break;
                    case 1:
                        aClass = MovieActivity.class;
                        break;
                    case 2:
                        aClass = MyGallaryActivity.class;
                        break;
                    case 3:
                        aClass = WallpaperActivity.class;
                        break;
                    case 4:
                        aClass = GestureActivity.class;
                        break;
                    case 5:
                        FankuiDialog noteCommentDialog = FankuiDialog.newInstance(0);
                        noteCommentDialog.show(getSupportFragmentManager(), "NoteDetailActivity");
                        break;
                    case 6:
                        Beta.checkUpgrade();
                        break;
                    case 7:
                        ToastUtils.showToast("版本号：V"+getVersion());
                        break;
                }
                if(aClass!=null){
                    Intent intent=new Intent(SettingActivity.this,aClass);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });
    }
    private String getVersion() {
        // 获取packagemanager的实例
        PackageManager packageManager = this.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
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
