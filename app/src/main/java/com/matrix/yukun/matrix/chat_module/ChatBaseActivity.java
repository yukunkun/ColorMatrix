package com.matrix.yukun.matrix.chat_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.adapter.ChatAdapter;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.entity.EventVoiceClick;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.chat_module.mvp.BasePresenter;
import com.matrix.yukun.matrix.chat_module.mvp.ChatControler;
import com.matrix.yukun.matrix.chat_module.mvp.ChatPresenter;
import com.matrix.yukun.matrix.chat_module.mvp.InputPanel;
import com.matrix.yukun.matrix.chat_module.mvp.MVPBaseActivity;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;
import com.matrix.yukun.matrix.util.GetPhotoFromPhotoAlbum;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.miracle.view.imageeditor.bean.EditorResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.OnClick;

public class ChatBaseActivity extends MVPBaseActivity implements ChatControler.View, InputListener {
    @BindView(R.id.iv_backs)
    ImageView mIvBack;
    @BindView(R.id.iv_member)
    ImageView mIvMem;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.rv_chatview)
    CubeRecyclerView mRvChatview;
    @BindView(R.id.sr_refresh)
    CubeSwipeRefreshLayout mSrRefresh;
    @BindView(R.id.fl)
    FrameLayout mRootView;
    ChatPresenter mChatPresenter;
    public static int TYPE_MEM=1;
    public static int TYPE_WOMEM=2;
    private ChatAdapter mChatAdapter;
    private List<ChatListInfo> mChatInfos = new ArrayList<>();
    private int type;
    private InputPanel mInputPanel;
    private int skip;
    private LinearLayoutManager mLinearLayoutManager;
    private File cameraSavePath;//拍照照片路径

    public static void start(Context context,int type){
        Intent intent=new Intent(context,ChatBaseActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return new ChatPresenter(this,this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_chat_video;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        type=getIntent().getIntExtra("type",0);
        mChatPresenter= (ChatPresenter) mPresenter;
        View view=LayoutInflater.from(this).inflate(R.layout.chat_header_view,null);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvChatview.setLayoutManager(mLinearLayoutManager);
        mSrRefresh.setHeaderView(view);
        if(type==TYPE_MEM){
            mName.setText(R.string.rebote);
        }else if (type==TYPE_WOMEM){
            mName.setText(R.string.robote_secretor);
        }
        mChatAdapter = new ChatAdapter(this, mChatInfos);
        mRvChatview.setAdapter(mChatAdapter);
        mInputPanel = new InputPanel(this,mRootView,this);
        setListener();
        loadHistoryMessage();
    }

    @Override
    public void initListener() {
        //软键盘挡住消息的问题
        mRvChatview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRvChatview.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mChatAdapter.getItemCount() > 0) {
                                mRvChatview.smoothScrollToPosition(mChatAdapter.getItemCount()-1);
                            }
                        }
                    });
                }
            }
        });
        //点击
        this.mRvChatview.setEventListener(new CubeRecyclerView.OnEventListener() {
            @Override
            public void onStartTouch() {
                mInputPanel.dismissLayout();
            }
        });
    }

    @Override
    public void initDate() {

    }

    /**
     * 加载历史消息
     */
    private void loadHistoryMessage() {
        List<ChatListInfo> chatListInfos = mChatPresenter.loadHistoryMsg(type,skip);
        List<ChatListInfo> list = new ArrayList();
        if(chatListInfos.size()>0){
            list.addAll(mChatInfos);
            mChatInfos.clear();
            mChatInfos.addAll(chatListInfos);
            mChatInfos.addAll(list);
            mChatAdapter.notifyDataSetChanged();
            mRvChatview.scrollToPosition(chatListInfos.size() - 1);
//            mRvChatview.smoothScrollToPosition(mChatAdapter.getItemCount()-1); //有动画
        }
    }

    private void setListener() {
        mSrRefresh.setOnPullRefreshListener(new CubeSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                skip=mChatInfos.size();
                loadHistoryMessage();
                mSrRefresh.setRefreshing(false);
            }

            @Override
            public void onPullDistance(int distance) {
            }

            @Override
            public void onPullEnable(boolean enable) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String photoPath;
        if (requestCode == InputPanel.ACTION_REQUEST_IMAGE && resultCode == RESULT_OK) {
            if(data.getData()!=null){
                photoPath = GetPhotoFromPhotoAlbum.getPath(this, data.getData());
                sendImageMsg(photoPath);
            }else {
                ToastUtils.showToast("send message error");
            }
        }
        if (requestCode == InputPanel.ACTION_REQUEST_VIDEO && resultCode == RESULT_OK) { //视频
            String videoPath = GetPhotoFromPhotoAlbum.getPath(this, data.getData());
            ChatListInfo videoChatInfo = mChatPresenter.createVideoChatInfo(videoPath, type, false);
            mChatInfos.add(videoChatInfo);
            mChatAdapter.notifyDataSetChanged();
            mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
        }
        if (requestCode == InputPanel.ACTION_REQUEST_FILE && resultCode == RESULT_OK) { //文件
             String videoPath = GetPhotoFromPhotoAlbum.getPath(this, data.getData());
            if(!TextUtils.isEmpty(videoPath)){
                ChatListInfo videoChatInfo = mChatPresenter.createFileChatInfo(videoPath, type, false);
                mChatInfos.add(videoChatInfo);
                mChatAdapter.notifyDataSetChanged();
                mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
            }else {
                ToastUtils.showToast("文件选择失败");
            }
        }
        if (requestCode == InputPanel.ACTION_REQUEST_CAMERA && resultCode == RESULT_OK) {
            cameraSavePath = new File(InputPanel.cameraSavePath);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
                sendImageMsg(photoPath);
//            } else {
//                photoPath = String.valueOf(cameraSavePath);
//                sendImageMsg(photoPath);
//            }
        }if (resultCode == RESULT_OK && requestCode == InputPanel.ACTION_REQUEST_EDITOR) {
            if(data!=null){
                EditorResult editorResult = (EditorResult) data.getSerializableExtra(Activity.RESULT_OK + "");
                sendImageMsg(editorResult.getEditor2SavedPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendImageMsg(String imagePath){
        if(!TextUtils.isEmpty(imagePath)){
            ChatListInfo imageChatInfo = mChatPresenter.createImageChatInfo(imagePath, type, false);
            notifyAdapter(imageChatInfo);
            sendDefaultMsg("我不喜欢图片");
        }else {
            ToastUtils.showToast("发送失败");
        }
    }

    @Override
    public void getRoboteMessage(String msg) {
        sendDefaultMsg(msg);
    }

    public void sendDefaultMsg(String msg) {
        ChatListInfo chatInfo = mChatPresenter.createTextChatInfo(msg, type,true);
        mChatInfos.add(chatInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
    }

    /**
     * 发送
     * @param msg
     */
    private void sendInfo(String msg) {
        String sendMsg;
        ChatListInfo chatInfo = mChatPresenter.createTextChatInfo(msg, type,false);
        notifyAdapter(chatInfo);
        if (msg.length() > 30) {
            sendMsg=msg.substring(0, 30);
        } else {
            sendMsg=msg;
        }
        mChatPresenter.sendRoboteMessage(sendMsg);
    }

    public void sendShakeListener(){
        ChatListInfo chatListInfo = mChatPresenter.sendShakeListener(type);
        notifyAdapter(chatListInfo);
        mChatPresenter.sendRoboteMessage(getString(R.string.shake_you));
    }

    @OnClick({R.id.iv_backs, R.id.iv_member})
    public void onViewClicked(View view) {
        if(view.getId()== R.id.iv_backs){
            finish();
        }if(view.getId()== R.id.iv_member){
            ChatMemberActivity.start(this);
        }
    }

    @Override
    public void onSendMessageClick(String msg) {
        sendInfo(msg);
    }

    @Override
    public void onPictureClick(List<Photo> picPath) {
        for (int i = 0; i < picPath.size(); i++) {
            sendImageMsg(picPath.get(i).path);
        }
        mInputPanel.dismissLayout();
    }

    @Override
    public void onBottomMove(int position) {

    }

    @Override
    public void onShaked() {

    }

    private void  notifyAdapter(ChatListInfo chatListInfo){
        mChatInfos.add(chatListInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
    }

    public void sendVoiceMsg(String voicePath,long mDuration){
        ChatListInfo chatListInfo = mChatPresenter.creatVoiceChatInfo(voicePath,mDuration,type,false);
        notifyAdapter(chatListInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getVoiceClick(EventVoiceClick eventVoiceClick){
        ChatListInfo chatListInfo = eventVoiceClick.chatListInfo;
        for (int i = 0; i < mChatInfos.size(); i++) {
            if(chatListInfo.getTimeStamp()==mChatInfos.get(i).getTimeStamp()){
                mChatInfos.get(i).setAudioIsPlay(true);
            }else {
                mChatInfos.get(i).setAudioIsPlay(false);
            }
        }
        LogUtil.i("======",mChatInfos.toString());
        mChatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}