package com.matrix.yukun.matrix.leancloud_module.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.chat_module.mvp.InputPanel;
import com.matrix.yukun.matrix.leancloud_module.InputPanelManager;
import com.matrix.yukun.matrix.leancloud_module.MessageManager;
import com.matrix.yukun.matrix.leancloud_module.adapter.LeanChatAdapter;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;
import com.matrix.yukun.matrix.leancloud_module.entity.LeanChatMessage;
import com.matrix.yukun.matrix.main_module.entity.UserInfoBMob;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;
import com.matrix.yukun.matrix.util.GetPhotoFromPhotoAlbum;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.miracle.view.imageeditor.bean.EditorResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class LeanBaseActivity extends BaseActivity implements InputListener, MessageManager.onUpdateListener {

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
        mLeanChatAdapter = new LeanChatAdapter(mLeanChatMessages, this);
        rvChatview.setAdapter(mLeanChatAdapter);

        MessageManager.getInstance().setOnUpdateListener(this);
    }

    @Override
    public void onUpdateMessage(LeanChatMessage leanChatMessage) {
        mLeanChatMessages.add(leanChatMessage);
        mLeanChatAdapter.notifyItemInserted(mLeanChatMessages.size());
    }

    @Override
    public void initDate() {
        if (mInfoBMob == null && mData != null) {
            chatId = (!mData.getFrom().equals(MyApp.getUserInfo().getId())) ? mData.getFrom() : mData.getTo();
            chatName = (!mData.getFrom().equals(MyApp.getUserInfo().getId())) ? mData.getFromUserName() : mData.getToUserName();
            chatAvator = (!mData.getFrom().equals(MyApp.getUserInfo().getId())) ? mData.getFromAvator() : mData.getToAvator();
        } else {
            chatId = mInfoBMob.getId();
            chatName = mInfoBMob.getName();
            chatAvator = mInfoBMob.getAvator();
        }
        LogUtil.i(chatId+" "+MyApp.getUserInfo().getId());
        MessageManager.getInstance().initData(chatId);
        MessageManager.getInstance().createConversion(chatId, chatName, chatAvator);

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String photoPath;
        if (requestCode == InputPanel.ACTION_REQUEST_IMAGE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                photoPath = GetPhotoFromPhotoAlbum.getPath(this, data.getData());
                MessageManager.getInstance().sendImageMessage(photoPath);
                LeanChatMessage leanChatMessage = MessageManager.getInstance().imageToMessage(photoPath, chatId, chatName, chatAvator);
                refreshAdapter(leanChatMessage);
            } else {
                ToastUtils.showToast("send message error");
            }
        }

        if (resultCode == RESULT_OK && requestCode == InputPanel.ACTION_REQUEST_EDITOR) {
            if (data != null) {
                EditorResult editorResult = (EditorResult) data.getSerializableExtra(Activity.RESULT_OK + "");
                MessageManager.getInstance().sendImageMessage(editorResult.getEditor2SavedPath());
                LeanChatMessage leanChatMessage = MessageManager.getInstance().imageToMessage(editorResult.getEditor2SavedPath(), chatId, chatName, chatAvator);
                refreshAdapter(leanChatMessage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onShaked() {
        LeanChatMessage leanChatMessage = MessageManager.getInstance().txtToMessage("抖一抖", chatId, chatName, chatAvator);
        MessageManager.getInstance().sendTxtMessage("抖一抖", chatId, chatName, chatAvator);
        refreshAdapter(leanChatMessage);
    }

    private void refreshAdapter(LeanChatMessage leanChatMessage) {
        mLeanChatMessages.add(leanChatMessage);
        mLeanChatAdapter.notifyItemInserted(mLeanChatMessages.size());
    }

    @Override
    public void onSendMessageClick(String msg) {
        LeanChatMessage leanChatMessage = MessageManager.getInstance().txtToMessage(msg, chatId, chatName, chatAvator);
        MessageManager.getInstance().sendTxtMessage(msg, chatId, chatName, chatAvator);
        refreshAdapter(leanChatMessage);
    }

    @Override
    public void onPictureClick(List<Photo> picPath) {
        for (int i = 0; i < picPath.size(); i++) {
            LogUtil.i("path:" + picPath.get(i).path);
            MessageManager.getInstance().sendImageMessage(picPath.get(i).path);
//            MessageManager.getInstance().sendImageMessage("icon.png", AppConstant.APP_ICON_URl);
            LeanChatMessage leanChatMessage = MessageManager.getInstance().imageToMessage(picPath.get(i).path, chatId, chatName, chatAvator);
            refreshAdapter(leanChatMessage);
        }
    }

    @Override
    public void onBottomMove(int position) {

    }


}
