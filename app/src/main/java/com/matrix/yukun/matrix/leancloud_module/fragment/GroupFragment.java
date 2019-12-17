package com.matrix.yukun.matrix.leancloud_module.fragment;

import android.os.Bundle;
import android.view.View;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVFriendListAdapter;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVGroupListAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.FriendsBMob;
import com.matrix.yukun.matrix.leancloud_module.entity.GroupBMob;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kun .
 * date:   On 2019/12/17
 */
public class GroupFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<GroupBMob> mFriendsBMobs = new ArrayList<>();
    private RVGroupListAdapter mRVGroupListAdapter;

    public static GroupFragment getInstance() {
        GroupFragment friendFragment = new GroupFragment();
        return friendFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_friend;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mRecyclerView = inflate.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRVGroupListAdapter = new RVGroupListAdapter(getContext(), R.layout.friend_item_layout, mFriendsBMobs);
        mRecyclerView.setAdapter(mRVGroupListAdapter);
    }
}
