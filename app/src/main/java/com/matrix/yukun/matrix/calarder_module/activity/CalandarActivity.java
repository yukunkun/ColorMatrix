package com.matrix.yukun.matrix.calarder_module.activity;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.calarder_module.adapter.RVCalandarAdapter;
import com.matrix.yukun.matrix.calarder_module.contant.CalandarBean;
import com.matrix.yukun.matrix.calarder_module.fragment.CalandarDetailFragment;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.reverse;

public class CalandarActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    RecyclerView mRvList;
    SwipeRefreshLayout mSw;
    List<CalandarBean> mCalandarBeanList=new ArrayList<>();
    private RVCalandarAdapter mRvCalandarAdapter;
    private ImageView mIvBack;

    @Override
    public int getLayout() {
        return R.layout.activity_calandar;
    }

    @Override
    public void initView() {
        mSw=findViewById(R.id.sw);
        mRvList=findViewById(R.id.rv_list);
        mIvBack = findViewById(R.id.iv_back);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRvList.setLayoutManager(linearLayoutManager);
        mCalandarBeanList = DataSupport.findAll(CalandarBean.class);
        reverse(mCalandarBeanList);
        mRvCalandarAdapter = new RVCalandarAdapter(mCalandarBeanList,this);
        mRvList.setAdapter(mRvCalandarAdapter);
        Toast.makeText(this,"长按可删除日程",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initListener() {
        mSw.setOnRefreshListener(this);
        mIvBack.setOnClickListener(this);
        mRvCalandarAdapter.setOnSelectCallBack(new RVCalandarAdapter.OnSelectCallBack() {
            @Override
            public void onSelectCallBack(CalandarBean calandarBean) {
                CalandarDetailFragment calandarDetailFragment=CalandarDetailFragment.getInstance(calandarBean);
                calandarDetailFragment.show(getFragmentManager(),"");
            }
        });

    }


    @Override
    public void onRefresh() {
        mSw.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_back){
            this.finish();
        }
    }
}
