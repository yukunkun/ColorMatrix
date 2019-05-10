package com.matrix.yukun.matrix.chat_module;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.chat_module.adapter.ChatAdapter;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.entity.ChatType;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.chat_module.mvp.BasePresenter;
import com.matrix.yukun.matrix.chat_module.mvp.ChatControler;
import com.matrix.yukun.matrix.chat_module.mvp.ChatPresenter;
import com.matrix.yukun.matrix.chat_module.mvp.InputPanel;
import com.matrix.yukun.matrix.chat_module.mvp.MVPBaseActivity;
import com.matrix.yukun.matrix.constant.AppConstant;
import com.matrix.yukun.matrix.selfview.CubeRecyclerView;
import com.matrix.yukun.matrix.selfview.CubeSwipeRefreshLayout;
import com.matrix.yukun.matrix.util.getPhotoFromPhotoAlbum;
import com.matrix.yukun.matrix.video_module.netutils.NetworkUtils;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

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
    @BindView(R2.id.et_messg)
    EditText mEtMessg;
    @BindView(R2.id.send_btn)
    Button mSendBtn;
    @BindView(R2.id.bt_add)
    Button mBtnAdd;
    @BindView(R2.id.fl_contain)
    FrameLayout mFlLayout;
    @BindView(R2.id.fl)
    FrameLayout mRootView;
    public static int TYPE_MEM=1;
    public static int TYPE_WOMEM=2;
    private String chatUrl = "http://op.juhe.cn/robot/index";
    private String KEY = "12b5b1b14c7e1d25f18902728b9655b6";
    private ChatAdapter mChatAdapter;
    private List<ChatListInfo> mChatInfos = new ArrayList<>();
    private double firstTime;
    private double lastTime;
    private double timedevide = 8000;
    private Uri uri;
    private File cameraSavePath;//拍照照片路径
    private int type;
    private int limit=50;
    private int skip=0;
    private InputPanel mInputPanel;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvChatview.setLayoutManager(linearLayoutManager);
//        getSupportFragmentManager().beginTransaction().add(R.id.fl_contain, ChatToolFragment.getInstance(this)).commit();
        View view=LayoutInflater.from(this).inflate(R.layout.chat_header_view,null);
        mSrRefresh.setHeaderView(view);
        if(type==TYPE_MEM){
            mName.setText("客服机器人");
        }else if (type==TYPE_WOMEM){
            mName.setText("小蜜");
        }
        mChatAdapter = new ChatAdapter(this, mChatInfos);
        mRvChatview.setAdapter(mChatAdapter);
        mInputPanel = new InputPanel(this,mRootView,this);
        setListener();
        loadHistoryMessage();
        cameraSavePath = new File(AppConstant.IMAGEPATH +"/yk_"+System.currentTimeMillis()+".jpg");
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
                                mRvChatview.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public void initDate() {

    }

    private void loadHistoryMessage() {
        List<ChatListInfo> chatListInfos = DataSupport.where("typeSn = ?",type+"").limit(limit).offset(skip).find(ChatListInfo.class);
        mChatInfos.addAll(chatListInfos);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.scrollToPosition(mChatInfos.size()-1);
    }

    private void setListener() {
        mEtMessg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    SendInfo(mEtMessg.getText().toString());
                    /*隐藏软键盘*/
                    mEtMessg.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtMessg.getWindowToken(), 0);
                    return true;
                } else
                    return true;
            }
        });

        mEtMessg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlLayout.setVisibility(View.GONE);
            }
        });
        mEtMessg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    mBtnAdd.setVisibility(View.VISIBLE);
                    mSendBtn.setVisibility(View.GONE);
                }else {
                    mBtnAdd.setVisibility(View.GONE);
                    mSendBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSrRefresh.setOnPullRefreshListener(new CubeSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                mSrRefresh.setRefreshing(false);
            }

            @Override
            public void onPullDistance(int distance) {
            }

            @Override
            public void onPullEnable(boolean enable) {
            }
        });

        //发送消息
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendInfo(mEtMessg.getText().toString());
                mEtMessg.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtMessg.getWindowToken(), 0);
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlLayout.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtMessg.getWindowToken(), 0);
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
            ChatListInfo chatListInfo = new ChatListInfo();
            lastTime = System.currentTimeMillis();
            if (lastTime - firstTime > timedevide) {
                chatListInfo.setShowTime(true);
            }
            firstTime = lastTime;
            chatListInfo.setTimeStamp(System.currentTimeMillis());
            chatListInfo.setMsgType(ChatType.IMAGE.getName());
            chatListInfo.setReceive(false);
            chatListInfo.setTypeSn(type);
            chatListInfo.setImagePath(imagePath);
            mChatInfos.add(chatListInfo);
            chatListInfo.save();
            mChatAdapter.notifyDataSetChanged();
            mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
            getMsg("我不喜欢图片");

        }else {
            ToastUtils.showToast("发送失败");
        }
    }

    public void openPhoto(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
        mFlLayout.setVisibility(View.GONE);
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
        mFlLayout.setVisibility(View.GONE);

    }

    private void getInfo(String info) {
        NetworkUtils.networkGet(chatUrl)
                .addParams("info", info)
                .addParams("key", KEY)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                getMsg("不想和你聊天了");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.optJSONObject("result");
                    String text = result.optString("text");
                    getMsg(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getMsg(String msg) {
        if (msg == null) {
            return;
        }
        ChatListInfo chatListInfo = new ChatListInfo();
        lastTime = System.currentTimeMillis();
        if (lastTime - firstTime > timedevide) {
            chatListInfo.setShowTime(true);
        }
        firstTime = lastTime;
        chatListInfo.setChatInfo(msg);
        chatListInfo.setTypeSn(type);
        chatListInfo.setTimeStamp(System.currentTimeMillis());
        chatListInfo.setMsgType(ChatType.TEXT.getName());
        chatListInfo.setReceive(true);
        //保存到数据库
        chatListInfo.save();
        mChatInfos.add(chatListInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
    }

    private void SendInfo(String msg) {
        if (null == msg || msg.length() == 0) {
            return;
        }
        ChatListInfo chatListInfo = new ChatListInfo();

        lastTime = System.currentTimeMillis();
        if (lastTime - firstTime > timedevide) {
            chatListInfo.setShowTime(true);
        }
        firstTime = lastTime;
        chatListInfo.setMsgTime(System.currentTimeMillis());
        chatListInfo.setChatInfo(msg);
        chatListInfo.setTypeSn(type);
        chatListInfo.setReceive(false);
        chatListInfo.setMsgType(ChatType.TEXT.getName());
        mChatInfos.add(chatListInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);
        chatListInfo.save();
        if (msg.length() > 30) {
            //保存到数据库
            getInfo(msg.substring(0, 30));
        } else {
            if (msg.length() <= 1) {
                getInfo(msg + "吗");
            } else {
                getInfo(msg);
            }
        }
    }

    @OnClick({R2.id.iv_backs,R2.id.iv_member})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.iv_backs){
            finish();
        }if(view.getId()==R.id.iv_member){
            ChatMemberActivity.start(this);
        }
    }
}
