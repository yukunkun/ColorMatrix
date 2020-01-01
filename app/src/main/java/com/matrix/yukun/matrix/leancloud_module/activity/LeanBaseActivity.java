package com.matrix.yukun.matrix.leancloud_module.activity;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.leancloud_module.InputPanelManager;
import com.matrix.yukun.matrix.leancloud_module.MessageManager;
import com.matrix.yukun.matrix.leancloud_module.adapter.LeanChatAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

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
    public UserInfoBMob mInfoBMob;
    public InputPanelManager mInputPanelManager;
    private LinearLayoutManager mLayoutManager;
    private List<LeanChatMessage> mLeanChatMessages = new ArrayList<>();
    public LeanChatAdapter mLeanChatAdapter;
    public String chatId;
    public String chatName;
    public String chatAvator;

    @Override
    public int getLayout() {
        return R.layout.activity_lean_base;
    }

    @Override
    public void initView() {
        mData = (ContactInfo) getIntent().getSerializableExtra("data");
        mInfoBMob = (UserInfoBMob) getIntent().getSerializableExtra("user");
        mInputPanelManager = new InputPanelManager(this, mLayoutRoot, this);
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvChatview.setLayoutManager(mLayoutManager);
        mLeanChatAdapter = new LeanChatAdapter(mLeanChatMessages,this);
        rvChatview.setAdapter(mLeanChatAdapter);
    }

    @Override
    public void initDate() {
//        if(mInfoBMob!=null)
//            LogUtil.i(mInfoBMob.toString());
//        if(mData!=null)
//            LogUtil.i(mData.toString());
        if(mInfoBMob==null && mData!=null){
            chatId=(!mData.getFrom().equals(MyApp.getUserInfo().getId()))?mData.getFrom():mData.getTo();
            chatName=(!mData.getFrom().equals(MyApp.getUserInfo().getId()))?mData.getFromUserName():mData.getToUserName();
            chatAvator=(!mData.getFrom().equals(MyApp.getUserInfo().getId()))?mData.getFromAvator():mData.getToAvator();
        }else {
            chatId=mInfoBMob.getId();
            chatName=mInfoBMob.getName();
            chatAvator=mInfoBMob.getAvator();
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onSendMessageClick(String msg) {
        LogUtil.i("========",chatId+" "+chatName+" "+chatAvator);
        MessageManager.getInstance().sendTxtMessage(msg,chatId,chatName,chatAvator);
    }

    @Override
    public void onPictureClick(List<Photo> picPath) {

    }

    @Override
    public void onBottomMove(int position) {

    }
}
