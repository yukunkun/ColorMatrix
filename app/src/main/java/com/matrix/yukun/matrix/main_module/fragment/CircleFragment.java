package com.matrix.yukun.matrix.main_module.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.matrix.yukun.matrix.BaseFragment;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.LeanCloudInit;
import com.matrix.yukun.matrix.leancloud_module.activity.ContactMemberActivity;
import com.matrix.yukun.matrix.leancloud_module.activity.SearchFriendActivity;
import com.matrix.yukun.matrix.leancloud_module.adapter.RVContactAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.leancloud_module.impl.ConversitionListenerImpl;
import com.matrix.yukun.matrix.leancloud_module.utils.MessageWrapper;
import com.matrix.yukun.matrix.main_module.entity.EventUpdateHeader;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.leancloud.im.v2.AVIMConversation;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
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
    PopupWindow popupWindow;
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
        mLinearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rvList.setLayoutManager(mLinearLayoutManager);
        mRvContactAdapter = new RVContactAdapter(R.layout.contact_item_layout,mContactInfos);
        rvList.setAdapter(mRvContactAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(rvList,RecyclerView.HORIZONTAL);
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
        mRvContactAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    private void initData() {
        LeanCloudInit.getInstance().searchRecent(new ConversitionListenerImpl() {
            @Override
            public void conversitionData(List<AVIMConversation> convs) {
                List<ContactInfo> infos = MessageWrapper.getInstance().covertToContactInfo(convs);
                mContactInfos.addAll(infos);
                LogUtil.i(mContactInfos.toString());
                mRvContactAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(Exception e) {
//                ToastUtils.showToast("获取失败:"+e);
            }
        });
    }

    @OnClick({R.id.iv_contact, R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_contact:
                ContactMemberActivity.start(getContext());
                break;
            case R.id.iv_add:
                showMorePopWindow();
                break;
        }
    }

    /**
     * 弹出popWindow
     */
    private void showMorePopWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View popView = LayoutInflater.from(getContext()).inflate(R.layout.main_plus_popupwindow, null);
        TextView mTvCreateGroup = (TextView) popView.findViewById(R.id.create_group_tv);
        TextView mTvAddFriend = (TextView) popView.findViewById(R.id.add_friend_tv);
        TextView mTvAddGroup = (TextView) popView.findViewById(R.id.add_group_tv);
        popupWindow = new PopupWindow(popView, ScreenUtil.dip2px(130), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);// 设置弹出窗体可触摸
        popupWindow.setOutsideTouchable(true); // 设置点击弹出框之外的区域后，弹出框消失
        popupWindow.setAnimationStyle(R.style.TitleMorePopAnimationStyle); // 设置动画
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        ScreenUtil.setBackgroundAlpha(getActivity(), 0.9f);
        int popWidth = popupWindow.getContentView().getMeasuredWidth();
        int windowWidth = ScreenUtil.getDisplayWidth();
        popupWindow.showAsDropDown(ivAdd, -ScreenUtil.dip2px(130)+ivAdd.getWidth()-3, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ScreenUtil.setBackgroundAlpha(getActivity(), 1f);
            }
        });
        mTvCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTvAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFriendActivity.start(getContext(),0);
                popupWindow.dismiss();
            }
        });
        mTvAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFriendActivity.start(getContext(),1);
                popupWindow.dismiss();
            }
        });

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
