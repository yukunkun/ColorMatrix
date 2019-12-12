package com.matrix.yukun.matrix.main_module.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.main_module.adapter.RVAttentAdapter;
import com.matrix.yukun.matrix.main_module.entity.AttentList;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static me.everything.android.ui.overscroll.OverScrollDecoratorHelper.ORIENTATION_VERTICAL;

public class AttentionActivity extends BaseActivity {
    @BindView(R2.id.iv_back)
    ImageView mIVBack;
    @BindView(R2.id.rv)
    RecyclerView mRecyclerView;
    @BindView(R2.id.rl_remind)
    RelativeLayout mLayout;
    List<AttentList> mAttentLists=new ArrayList<>();
    private RVAttentAdapter mRvAttentAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        List<AttentList> historyPlays = DataSupport.findAll(AttentList.class);
        if(historyPlays!=null&&historyPlays.size()>0){
            mLayout.setVisibility(View.GONE);
            mAttentLists.addAll(historyPlays);
            Collections.reverse(mAttentLists);
        }
        mRvAttentAdapter = new RVAttentAdapter(this,mAttentLists);
        mRecyclerView.setAdapter(mRvAttentAdapter);
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
    }

    @Override
    public void initDate() {

    }
}
