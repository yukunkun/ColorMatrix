package com.ykk.pluglin_video.play;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R2;

import butterknife.BindView;
import butterknife.OnClick;

public class JokeDetailActivity extends BaseActivity {


    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.iv_share)
    ImageView mIvShare;
    @BindView(R2.id.tv_content)
    TextView mTvContent;
    private String mContent;

    @Override
    public int getLayout() {
        return R.layout.activity_joke_detail;
    }

    @Override
    public void initView() {
        mContent = getIntent().getStringExtra("content");
        mTvContent.setText(mContent);

    }


    @OnClick({R2.id.iv_back, R2.id.iv_share})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finish();

        } else if (i == R.id.iv_share) {
            shareSend();

        }
    }

    private void shareSend() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, mContent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, ""));
    }
}
