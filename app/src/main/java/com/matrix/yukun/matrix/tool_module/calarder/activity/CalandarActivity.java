package com.matrix.yukun.matrix.tool_module.calarder.activity;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.calarder.adapter.RVCalandarAdapter;
import com.matrix.yukun.matrix.tool_module.calarder.contant.CalandarBean;
import com.matrix.yukun.matrix.tool_module.calarder.fragment.CalandarDetailFragment;

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
                CalandarDetailFragment calandarDetailFragment= CalandarDetailFragment.getInstance(calandarBean);
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
        if (v.getId()== R.id.iv_back){
            this.finish();
        }
    }
}
