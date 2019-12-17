package com.matrix.yukun.matrix.leancloud_module.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVFriendListAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.FriendsBMob;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.GsonUtil;
import cn.bmob.v3.listener.FindListener;

/**
 * author: kun .
 * date:   On 2019/12/17
 */
public class FriendFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<UserInfoBMob> mFriendsBMobs=new ArrayList<>();
    private RVFriendListAdapter mRvFriendListAdapter;

    public static FriendFragment getInstance(){
        FriendFragment friendFragment=new FriendFragment();
        return friendFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_friend;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mRecyclerView = inflate.findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRvFriendListAdapter = new RVFriendListAdapter(getContext(),R.layout.friend_item_layout,mFriendsBMobs);
        mRecyclerView.setAdapter(mRvFriendListAdapter);
        initFriendData();
    }

    private void initFriendData() {
        BmobQuery<FriendsBMob> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", MyApp.getUserInfo().getId());
        //执行查询方法
        query.findObjects(new FindListener<FriendsBMob>() {
            @Override
            public void done(List<FriendsBMob> list, BmobException e) {
                if(e==null){
                    for (int i = 0; i < list.size(); i++) {
                        ArrayList<String> friendList = list.get(i).getFriendList();
                        for (int j = 0; j < friendList.size(); j++) {
                            String userinfo = friendList.get(i);
                            UserInfoBMob userInfoBMob= (UserInfoBMob) GsonUtil.toObject(userinfo, UserInfoBMob.class);
                            mFriendsBMobs.add(userInfoBMob);
                            LogUtil.i(list.toString());
                        }
                    }
                    mRvFriendListAdapter.notifyDataSetChanged();
                }else {
                    ToastUtils.showToast("获取好友失败："+e.toString());
                }
            }
        });
    }

}
