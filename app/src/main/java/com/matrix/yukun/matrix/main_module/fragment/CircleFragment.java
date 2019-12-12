package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.LeanCloudInit;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVContactAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.main_module.entity.EventUpdateHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
//import cn.leancloud.AVObject;
//import cn.leancloud.session.AVConnectionManager;

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
        EventBus.getDefault().register(this);
        mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvList.setLayoutManager(mLinearLayoutManager);
        mRvContactAdapter = new RVContactAdapter(R.layout.contact_item_layout,mContactInfos);
        rvList.setAdapter(mRvContactAdapter);
        updateTitle();
        initData();
    }

    private void updateTitle() {
        if(!LeanCloudInit.getInstance().isLogionleanCloud()){
            tvTitle.setText(getString(R.string.logining));
        }else {
            tvTitle.setText(getString(R.string.secret_circle));
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateHeader(EventUpdateHeader eventUpdateHeader) {
        if (MyApp.userInfo != null) {
            LeanCloudInit.getInstance().init(MyApp.userInfo.getId());
            tvTitle.setText(getString(R.string.secret_circle));
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
