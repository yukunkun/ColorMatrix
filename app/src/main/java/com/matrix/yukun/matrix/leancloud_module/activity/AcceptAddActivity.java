package com.matrix.yukun.matrix.leancloud_module.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.LeanCloudInit;
import com.matrix.yukun.matrix.leancloud_module.entity.AddFriendInfo;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.leancloud_module.entity.FriendsBMob;
import com.matrix.yukun.matrix.main_module.activity.LoginActivity;
import com.matrix.yukun.matrix.main_module.activity.PlayMainActivity;
import com.matrix.yukun.matrix.main_module.entity.EventUpdateHeader;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.helper.GsonUtil;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AcceptAddActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_contain)
    RelativeLayout rlContain;
    ContactInfo mContactInfo;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sm_refresh)
    SmartRefreshLayout smRefresh;
    @BindView(R.id.ll)
    LinearLayout ll;
    List<AddFriendInfo> data = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private RVAdapter mRvAdapter;

    public static void start(Context context, ContactInfo contactInfo) {
        Intent intent = new Intent(context, AcceptAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("data", contactInfo);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_accept_add;
    }

    @Override
    public void initView() {
        mContactInfo = (ContactInfo) getIntent().getSerializableExtra("data");
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvList.setLayoutManager(mLinearLayoutManager);
        mRvAdapter = new RVAdapter(R.layout.add_item_layout, data);
        rvList.setAdapter(mRvAdapter);
    }

    @Override
    public void initDate() {
        AddFriendInfo addFriendInfo = new AddFriendInfo();
        String lastMessage = mContactInfo.getLastMessage();
        if (!TextUtils.isEmpty(lastMessage)) {
            UserInfoBMob userInfoBMob = (UserInfoBMob) GsonUtil.toObject(lastMessage, UserInfoBMob.class);

            List<AddFriendInfo> addFriendInfos = DataSupport.where("avatar = ?", userInfoBMob.getAvator()).find(AddFriendInfo.class);
            if (!addFriendInfos.isEmpty()) {
                addFriendInfo.setAdd(true);
            }
            addFriendInfo.setUserInfoBMob(lastMessage);
            addFriendInfo.setName(userInfoBMob.getName());
            addFriendInfo.setAvatar(userInfoBMob.getAvator());
            addFriendInfo.setCreateTime(mContactInfo.getLastTime());
            addFriendInfo.setUserId(mContactInfo.getTo()); //接收者
            if (!addFriendInfo.isAdd()) {
                addFriendInfo.save();
            }
        }
        List<AddFriendInfo> all = DataSupport.findAll(AddFriendInfo.class);
        data.addAll(all);
        mRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        smRefresh.setOnLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smRefresh.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smRefresh.finishRefresh();
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    class RVAdapter extends BaseQuickAdapter<AddFriendInfo, BaseViewHolder> {

        public RVAdapter(int layoutResId, @Nullable List<AddFriendInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AddFriendInfo item) {
            helper.setText(R.id.tv_name, item.getName());
            TextView tvAdd = helper.getView(R.id.tv_add);
            GlideUtil.loadCircleImage(item.getAvatar(), helper.getView(R.id.iv_avatar));
            if (!mContactInfo.getTo().equals(MyApp.getUserInfo().getId())) {
                tvAdd.setClickable(false);
                tvAdd.setText("已申请");
                tvAdd.setTextColor(mContext.getResources().getColor(R.color.color_b9b9b9));
                tvAdd.setBackgroundResource(R.drawable.shape_collect_bg_checked);
            } else if (item.isAdd()) {
                tvAdd.setClickable(false);
                tvAdd.setText("已添加");
                tvAdd.setTextColor(mContext.getResources().getColor(R.color.color_b9b9b9));
                tvAdd.setBackgroundResource(R.drawable.shape_collect_bg_checked);
            }else {
                tvAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvAdd.setClickable(false);
                        tvAdd.setText("已添加");
                        tvAdd.setTextColor(mContext.getResources().getColor(R.color.color_b9b9b9));
                        tvAdd.setBackgroundResource(R.drawable.shape_collect_bg_checked);
                        item.setAdd(true);
                        item.save();
                        buildFriendContact(item);
                    }
                });
            }

        }
    }

    private void buildFriendContact(AddFriendInfo item) {
        ArrayList<String> mList=new ArrayList();
        mList.add(item.getUserInfoBMob());

        FriendsBMob friendsBMob=new FriendsBMob();
        friendsBMob.setUserId(MyApp.getUserInfo().getId());
        friendsBMob.setAvator(MyApp.getUserInfo().getAvator());
        friendsBMob.setName(MyApp.getUserInfo().getName());
        LogUtil.i(item.toString());
        BmobQuery<FriendsBMob> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("userId", MyApp.getUserInfo().getId());
        //执行查询方法
        query.findObjects(new FindListener<FriendsBMob>() {
            @Override
            public void done(List<FriendsBMob> list, BmobException e) {
                if(e==null&&!list.isEmpty()){
                    LogUtil.i(list.toString());
                    mList.addAll(list.get(0).getFriendList());
                    boolean goodFriend = isGoodFriend(item.getUserInfoBMob(), list.get(0).getFriendList());
                    if(goodFriend){
                        return;
                    }
                    friendsBMob.setFriendList(mList);
                    LogUtil.i(friendsBMob.toString());
                    friendsBMob.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){

                            }else {
                                ToastUtils.showToast("添加好友失败");
                            }
                        }
                    });
                }else {
                    if(e.getErrorCode()==101||e.getErrorCode()==9015){
                        friendsBMob.setFriendList(mList);
                        LogUtil.i(friendsBMob.toString());
                        friendsBMob.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){

                                }else {
                                    ToastUtils.showToast("添加好友失败");
                                }
                            }

                        });
                    }
                    LogUtil.i(e.toString());
                }
            }
        });


        ArrayList otherList=new ArrayList();
        otherList.add(GsonUtil.toJson(MyApp.getUserInfo()));
        UserInfoBMob infoBMob= (UserInfoBMob) GsonUtil.toObject(item.getUserInfoBMob(),UserInfoBMob.class);
        FriendsBMob othersBMob=new FriendsBMob();
        othersBMob.setUserId(infoBMob.getId());
        othersBMob.setAvator(infoBMob.getAvator());
        othersBMob.setName(infoBMob.getName());
        LogUtil.i(item.toString());
        LogUtil.i(othersBMob.toString());
        query.findObjects(new FindListener<FriendsBMob>() {
            @Override
            public void done(List<FriendsBMob> list, BmobException e) {
                if(e==null&&!list.isEmpty()){
                    LogUtil.i(list.toString());
                    otherList.addAll(list.get(0).getFriendList());
                    boolean goodFriend = isGoodFriend(GsonUtil.toJson(MyApp.getUserInfo()), list.get(0).getFriendList());
                    if(goodFriend){
                        return;
                    }
                    othersBMob.setFriendList(otherList);
                    LogUtil.i(othersBMob.toString());
                    othersBMob.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                ToastUtils.showToast("添加好友成功");
                            }else {
                                ToastUtils.showToast("添加好友失败");
                            }
                        }
                    });
                }else {
                    if(e.getErrorCode()==101||e.getErrorCode()==9015){
                        othersBMob.setFriendList(otherList);
                        LogUtil.i(othersBMob.toString());
                        othersBMob.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    ToastUtils.showToast("添加好友成功");
                                }else {
                                    ToastUtils.showToast("添加好友失败");
                                }
                            }

                        });
                    }
                    LogUtil.i(e.toString());
                }
            }
        });



    }

    private boolean isGoodFriend(String othersBMob, ArrayList<String> friendList) {
        UserInfoBMob infoBMob= (UserInfoBMob) GsonUtil.toObject(othersBMob,UserInfoBMob.class);
        for (int i = 0; i < friendList.size(); i++) {
            String userStr = friendList.get(i);
            UserInfoBMob userInfoBMob= (UserInfoBMob) GsonUtil.toObject(userStr,UserInfoBMob.class);
            if(infoBMob.getId().equals(userInfoBMob.getId())){
                return true;
            }
        }
        return false;
    }
}
