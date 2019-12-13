package com.matrix.yukun.matrix.main_module.activity;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.adapter.RVHistoryAdapter;
import com.matrix.yukun.matrix.main_module.entity.HistoryPlay;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static me.everything.android.ui.overscroll.OverScrollDecoratorHelper.ORIENTATION_VERTICAL;

public class HistoryPlayActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIVBack;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_remind)
    RelativeLayout mLayout;
    List<HistoryPlay> mHistoryPlayList=new ArrayList<>();
    private RVHistoryAdapter mRvHistoryAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_history_play;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<HistoryPlay> historyPlays = DataSupport.findAll(HistoryPlay.class);
        if(historyPlays!=null&&historyPlays.size()>0){
            mLayout.setVisibility(View.GONE);
            mHistoryPlayList.addAll(historyPlays);
            Collections.reverse(mHistoryPlayList);
        }
        mRvHistoryAdapter = new RVHistoryAdapter(this,mHistoryPlayList);
        mRecyclerView.setAdapter(mRvHistoryAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView,ORIENTATION_VERTICAL);

    }

    @Override
    public void initListener() {
        mIVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletDialog();
            }
        });
    }

    private void showDeletDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除全部")
                .setMessage("是否删除全部历史纪录？")
                .setNegativeButton("不",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(HistoryPlay.class);
                        mHistoryPlayList.clear();
                        mRvHistoryAdapter.notifyDataSetChanged();
                        mLayout.setVisibility(View.VISIBLE);
                    }
                })
                .show();
    }

    @Override
    public void initDate() {

    }
}
