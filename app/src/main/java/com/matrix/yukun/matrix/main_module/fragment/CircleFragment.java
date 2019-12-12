package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.RVGaiaAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.BannerInfo;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.leancloud_module.LeanCloudInit;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVContactAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: kun .
 * date:   On 2019/7/3
 */
public class CircleFragment extends BaseFragment {

    @BindView(R.id.iv_contact)
    ImageView ivContact;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private LinearLayoutManager mLinearLayoutManager;
    private List<ContactInfo> mContactInfos = new ArrayList<>();
    private RVContactAdapter mRvContactAdapter;

    public static CircleFragment getInstance() {
        CircleFragment circleFragment = new CircleFragment();
        return circleFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvList.setLayoutManager(mLinearLayoutManager);
        mRvContactAdapter = new RVContactAdapter(R.layout.contact_item_layout,mContactInfos);
        rvList.setAdapter(mRvContactAdapter);
        if(!LeanCloudInit.getInstance().isLogionleanCloud()){
            tvTitle.setText(getString(R.string.logining));
        }else {
            tvTitle.setText(getString(R.string.secret_circle));
        }
        initData();
    }

    public void initListener() {

    }

    private void initData() {

    }

    @OnClick({R.id.iv_contact, R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_contact:
                break;
            case R.id.iv_add:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
