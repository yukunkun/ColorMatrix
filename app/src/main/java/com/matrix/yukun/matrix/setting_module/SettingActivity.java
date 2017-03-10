package com.matrix.yukun.matrix.setting_module;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.movie_module.MovieActivity;
import com.matrix.yukun.matrix.weather_module.WeatherActivity;
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
                        Intent intent=new Intent(SettingActivity.this,AgreeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 2:
                        Intent intent2=new Intent(SettingActivity.this,MovieActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 3:
                        Intent intent3=new Intent(SettingActivity.this,WeatherActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
                        break;
                    case 4:
                        Intent intent1=new Intent(SettingActivity.this,IntroduceActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.right_in,R.anim.left_out);

//                Uri  uri = Uri.parse(AppConstants.YINGYONGBAOPATH);
//                Intent  intents = new  Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intents);
                        break;
                    case 5:
                        FankuiDialog noteCommentDialog=FankuiDialog.newInstance(0);
                        noteCommentDialog.show(getSupportFragmentManager(),"NoteDetailActivity");
                        break;
                    case 6:
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
