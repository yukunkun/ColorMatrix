package com.matrix.yukun.matrix.main_module.activity;

import android.content.DialogInterface;
import android.os.Build;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.CollectAdapter;
import com.matrix.yukun.matrix.main_module.adapter.ShareCallBack;
import com.matrix.yukun.matrix.main_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.main_module.entity.CollectsInfo;
import com.matrix.yukun.matrix.main_module.utils.SpacesDoubleDecoration;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCollectActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_deal)
    TextView mTvDeal;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    @BindView(R.id.rv_collect)
    RecyclerView mRvCollect;
    @BindView(R.id.sw)
    SwipeRefreshLayout mSw;
    @BindView(R.id.rl_remind)
    RelativeLayout mRelativeLayout;
    GridLayoutManager mLayoutManager;
    CollectAdapter mCollectAdapter;

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
        final List<CollectsInfo> collectInfoList = DataSupport.findAll(CollectsInfo.class);
        if(collectInfoList!=null&&collectInfoList.size()>0){
            mRelativeLayout.setVisibility(View.GONE);
        }
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
        mCollectAdapter.setShareCallBack(new ShareCallBack() {
            @Override
            public void onShareCallback(int pos) {
                CollectsInfo collectsInfo = collectInfoList.get(pos);
                ShareDialog instance = ShareDialog.getInstance(collectsInfo.getTitle(),collectsInfo.getPlay_url(),collectsInfo.getCover());
                instance.show(getSupportFragmentManager(),"");
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_deal})
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
