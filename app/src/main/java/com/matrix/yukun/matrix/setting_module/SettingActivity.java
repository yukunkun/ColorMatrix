package com.matrix.yukun.matrix.setting_module;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leshi_module.LeShiActivity;
import com.matrix.yukun.matrix.leshi_module.LeShiListActivity;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
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
                        Intent intent=new Intent(SettingActivity.this,AgreeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
//                Uri  uri = Uri.parse(AppConstants.YINGYONGBAOPATH);
//                Intent  intents = new  Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intents);
                        break;
                    case 5:
                        Intent intent1=new Intent(SettingActivity.this,IntroduceActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 6:
                        FankuiDialog noteCommentDialog=FankuiDialog.newInstance(0);
                        noteCommentDialog.show(getSupportFragmentManager(),"NoteDetailActivity");
                        break;
                    case 7:
                        Beta.checkUpgrade();
                    case 8:
                        Beta.checkUpgrade();
                        break;
                }
            }
        });
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
