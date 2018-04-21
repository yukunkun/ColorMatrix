package com.matrix.yukun.matrix.setting_module;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.matrix.yukun.matrix.R;
import com.tencent.bugly.beta.Beta;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class AboutUsActivity extends AppCompatActivity {

    ListView mListview;
    ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        init();
        setListener();
    }

    private void init() {
        mListview =findViewById(R.id.listview);
        ivIcon=findViewById(R.id.iv_back);
        mListview.setAdapter(new AboutUsAdapter(getApplicationContext()));
        OverScrollDecoratorHelper.setUpOverScroll(mListview);
    }

    private void setListener() {
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent intent = new Intent(AboutUsActivity.this, AgreeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 1:
                        Intent intent1 = new Intent(AboutUsActivity.this, IntroduceActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 2:
                        FankuiDialog noteCommentDialog = FankuiDialog.newInstance(0);
                        noteCommentDialog.show(getSupportFragmentManager(), "NoteDetailActivity");
                        break;
                    case 3:
                        Beta.checkUpgrade();
                        break;
                }
            }
        });
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
