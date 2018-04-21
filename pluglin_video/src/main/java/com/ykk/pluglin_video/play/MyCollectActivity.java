package com.ykk.pluglin_video.play;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.CollectAdapter;
import com.ykk.pluglin_video.entity.CollectsInfo;
import com.ykk.pluglin_video.utils.SpacesDoubleDecoration;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCollectActivity extends BaseActivity {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_deal)
    TextView mTvDeal;
    @BindView(R2.id.rl)
    RelativeLayout mRl;
    @BindView(R2.id.rv_collect)
    RecyclerView mRvCollect;
    GridLayoutManager mLayoutManager;
    CollectAdapter mCollectAdapter;
    @BindView(R2.id.sw)
    SwipeRefreshLayout mSw;

    @Override
    public int getLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Window window = getWindow();
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_my_collect;
    }

    @Override
    public void initView() {
        List<CollectsInfo> collectInfoList = DataSupport.findAll(CollectsInfo.class);
        Collections.reverse(collectInfoList);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRvCollect.setLayoutManager(mLayoutManager);
        mCollectAdapter = new CollectAdapter(this, collectInfoList);
        mRvCollect.setAdapter(mCollectAdapter);
        mRvCollect.addItemDecoration(new SpacesDoubleDecoration(0, 4, 8, 16));
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSw.setRefreshing(false);
            }
        });
    }

    @OnClick({R2.id.iv_back, R2.id.tv_deal})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finish();

        } else if (i == R.id.tv_deal) {
            delete();

        }
    }

    private void delete() {
        new AlertDialog.Builder(this).setTitle("编辑").setMessage("长按可移除该视频")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
