package com.matrix.yukun.matrix.chat_module;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements ChatControl.View {

    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.iv_person)
    ImageView mIvPerson;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.rv_chatview)
    RecyclerView mRvChatview;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    @BindView(R.id.et_messg)
    EditText mEtMessg;
    @BindView(R.id.send_btn)
    Button mSendBtn;
    private ChatPresent mChatPresent;
    private ChatAdapter mChatAdapter;
    private List<ChatListInfo> mChatInfos=new ArrayList<>();
    private double firstTime;
    private double lastTime;
    private double timedevide=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
//        firstTime=System.currentTimeMillis();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mChatPresent = new ChatPresent(this);
        initView();
        setListener();
        //默认发送一条消息
        mChatPresent.getInfo("how are you");
    }

    @Override
    public void initView() {
        LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(this);
        mRvChatview.setLayoutManager(linearLayoutManager);
        mChatAdapter = new ChatAdapter(this,mChatInfos);
        mRvChatview.setAdapter(mChatAdapter);
        String spName = getSPString("name", "name");
        if(!spName.equals("name")){
            mName.setText(spName);
        }
    }

    @Override
    public void setListener() {
        mEtMessg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    SendInfo(mEtMessg.getText().toString());
                     /*隐藏软键盘*/
                    mEtMessg.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtMessg.getWindowToken(), 0);
                    return true;
                }else
                return true;
            }
        });

        mSrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mChatInfos.clear();
                mChatAdapter.notifyDataSetChanged();
                mSrRefresh.setRefreshing(false);
            }
        });

        //个人信息
        mIvPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.showToast("想知道我的隐私啊?就不告诉你");
//                Intent intent=new Intent(ChatActivity.this,ChatPersonActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        //发送消息
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendInfo(mEtMessg.getText().toString());
                mEtMessg.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtMessg.getWindowToken(), 0);
            }
        });
    }

    private void SendInfo(String msg) {
        if(null==msg||msg.length()==0){
            MyApp.showToast("发送为空，请输入内容");
            return;
        }

        ChatListInfo chatListInfo=new ChatListInfo();
        lastTime=System.currentTimeMillis();
        if(lastTime-firstTime>timedevide){
            chatListInfo.setIsShowTime(true);
        }
        chatListInfo.setMsgTime(lastTime);
        firstTime=lastTime;
        chatListInfo.setChatInfo(msg);
        chatListInfo.setType(2);
        mChatInfos.add(chatListInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size()-1);

        if(msg.length()>30){
            mChatPresent.getInfo(msg.substring(0,30));
        }else {
            if(msg.length()<=1){
                mChatPresent.getInfo(msg+"吗");
            }else {
                mChatPresent.getInfo(msg);
            }
        }
    }

    //消息回来的数据处理
    @Override
    public void getMsg(String msg) {
        if(msg==null){
            return;
        }
        ChatListInfo chatListInfo=new ChatListInfo();
        lastTime=System.currentTimeMillis();
        if(lastTime-firstTime>timedevide){
            chatListInfo.setIsShowTime(true);
        }
        chatListInfo.setMsgTime(lastTime);
        firstTime=lastTime;
        chatListInfo.setChatInfo(msg);
//        chatListInfo.setBitmap();
        chatListInfo.setType(1);
        mChatInfos.add(chatListInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size()-1);
    }

    public void ChatBack(View view) {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }

    private String getSPString(String spName,String defeat){
        SharedPreferences preferences=getSharedPreferences(spName, Context.MODE_PRIVATE);
        String result = preferences.getString(spName,defeat);
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChatAdapter!=null)
        mChatAdapter.cancenToast();
    }
}
