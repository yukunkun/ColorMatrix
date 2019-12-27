package com.matrix.yukun.matrix.leancloud_module.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.emoji.CubeEmoticonEditText;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.leancloud_module.InputPanelManager;
import com.matrix.yukun.matrix.leancloud_module.adapter.LeanChatAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeanBaseActivity extends BaseActivity implements InputListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.rv_chatview)
    CubeRecyclerView rvChatview;
    @BindView(R.id.sr_refresh)
    CubeSwipeRefreshLayout srRefresh;
    @BindView(R.id.ll_root)
    LinearLayout mLayoutRoot;

    public ContactInfo mData;
    public InputPanelManager mInputPanelManager;
    private LinearLayoutManager mLayoutManager;
    private List<LeanChatMessage> mLeanChatMessages = new ArrayList<>();
    public LeanChatAdapter mLeanChatAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_lean_base;
    }

    @Override
    public void initView() {
        mData = (ContactInfo) getIntent().getSerializableExtra("data");
        mInputPanelManager = new InputPanelManager(this, mLayoutRoot, this);
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvChatview.setLayoutManager(mLayoutManager);
        mLeanChatAdapter = new LeanChatAdapter(mLeanChatMessages,this);
        rvChatview.setAdapter(mLeanChatAdapter);

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onSendMessageClick(String msg) {

    }

    @Override
    public void onPictureClick(List<Photo> picPath) {

    }

    @Override
    public void onBottomMove(int position) {

    }
}
