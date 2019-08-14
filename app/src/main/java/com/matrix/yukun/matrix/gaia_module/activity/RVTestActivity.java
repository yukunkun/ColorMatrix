package com.matrix.yukun.matrix.gaia_module.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.RVRestAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.ItemBean;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RVTestActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    List<ItemBean> mIndexBeans =new ArrayList<>();
    private RVRestAdapter mRvRestAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, RVTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_rvtest;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        mRvRestAdapter = new RVRestAdapter(mIndexBeans);
        rv.setAdapter(mRvRestAdapter);
    }


    @Override
    public void initDate() {
        for (int i = 0; i < 10; i++) {
            ItemBean itemBean=new ItemBean();
            itemBean.setDescription("avatar:"+i);
            if(i%2==0){
                itemBean.setItemType(1);
            }else {
                itemBean.setItemType(2);
            }
            mIndexBeans.add(itemBean);
        }
        mRvRestAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        mRvRestAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initDate();
                mRvRestAdapter.loadMoreComplete();
            }
        },rv);
    }
}
