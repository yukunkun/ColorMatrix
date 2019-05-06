package com.matrix.yukun.matrix.net_module;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class HistoryActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_history)
    TextView mTvHistory;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    public static void start(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_history;
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.iv_back, R.id.tv_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_history:
                break;
        }
    }
}
