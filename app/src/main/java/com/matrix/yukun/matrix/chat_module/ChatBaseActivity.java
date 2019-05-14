package com.matrix.yukun.matrix.chat_module;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.chat_module.adapter.ChatAdapter;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.chat_module.mvp.BasePresenter;
import com.matrix.yukun.matrix.chat_module.mvp.ChatControler;
import com.matrix.yukun.matrix.chat_module.mvp.ChatPresenter;
import com.matrix.yukun.matrix.chat_module.mvp.InputPanel;
import com.matrix.yukun.matrix.chat_module.mvp.MVPBaseActivity;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;
import com.matrix.yukun.matrix.util.getPhotoFromPhotoAlbum;import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class ChatBaseActivity extends MVPBaseActivity implements ChatControler.View, InputListener {
    @BindView(R2.id.iv_backs)
    ImageView mIvBack;
    @BindView(R2.id.iv_member)
    ImageView mIvMem;
    @BindView(R2.id.name)
    TextView mName;
    @BindView(R2.id.title)
    RelativeLayout mTitle;
    @BindView(R2.id.rv_chatview)
    CubeRecyclerView mRvChatview;
    @BindView(R2.id.sr_refresh)
    CubeSwipeRefreshLayout mSrRefresh;
    @BindView(R2.id.fl)
    FrameLayout mRootView;
    ChatPresenter mChatPresenter;
    public static int TYPE_MEM=1;
    public static int TYPE_WOMEM=2;
    private ChatAdapter mChatAdapter;
    private List<ChatListInfo> mChatInfos = new ArrayList<>();
    private Uri uri;
    private File cameraSavePath;//拍照照片路径
    private int type;
    private InputPanel mInputPanel;
    private boolean isFirst;
    private int skip;
    private LinearLayoutManager mLinearLayoutManager;

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
        type=getIntent().getIntExtra("type",0);
        cameraSavePath = new File(AppConstant.IMAGEPATH +"/yk_"+System.currentTimeMillis()+".jpg");
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

        mRvChatview.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                if(velocityY<0){
                    mInputPanel.dismissLayout();
                }
                return false;
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
        if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            sendImageMsg(photoPath);
        }if (requestCode == 1 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
                sendImageMsg(photoPath);
            } else {
                photoPath = uri.getEncodedPath();
                sendImageMsg(photoPath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendImageMsg(String imagePath){
        if(!TextUtils.isEmpty(imagePath)){
            ChatListInfo imageChatInfo = mChatPresenter.createImageChatInfo(imagePath, type, false);
            mChatInfos.add(imageChatInfo);
            mChatAdapter.notifyDataSetChanged();
            mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
            sendDefaultMsg("我不喜欢图片");
        }else {
            ToastUtils.showToast("发送失败");
        }
    }

    public void openPhoto(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(ChatBaseActivity.this, "com.xykj.customview.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        this.startActivityForResult(intent, 1);
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
        mChatInfos.add(chatInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
        if (msg.length() > 30) {
            sendMsg=msg.substring(0, 30);
        } else {
            sendMsg=msg;
        }
        mChatPresenter.sendRoboteMessage(sendMsg);
    }

    @OnClick({R2.id.iv_backs,R2.id.iv_member})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.iv_backs){
            finish();
        }if(view.getId()==R.id.iv_member){
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
        translateTop(position+50);
    }

    private void translateTop(int pos){
        mChatPresenter.translateTop(mRvChatview,pos);
    }
}