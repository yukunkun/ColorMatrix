package com.ykk.pluglin_video.play;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.adapter.ChatAdapter;
import com.ykk.pluglin_video.entity.ChatListInfo;
import com.ykk.pluglin_video.netutils.NetworkUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class ChatActivity extends BaseActivity {
    @BindView(R2.id.iv_backs)
    ImageView mIvBack;
    private String chatUrl = "http://op.juhe.cn/robot/index";
    private String KEY = "12b5b1b14c7e1d25f18902728b9655b6";
    @BindView(R2.id.name)
    TextView mName;
    @BindView(R2.id.title)
    RelativeLayout mTitle;
    @BindView(R2.id.rv_chatview)
    RecyclerView mRvChatview;
    @BindView(R2.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    @BindView(R2.id.et_messg)
    EditText mEtMessg;
    @BindView(R2.id.send_btn)
    Button mSendBtn;
    private ChatAdapter mChatAdapter;
    private List<ChatListInfo> mChatInfos = new ArrayList<>();
    private double firstTime;
    private double lastTime;
    private double timedevide = 5000;

    @Override
    public int getLayout() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return R.layout.activity_chat_video;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvChatview.setLayoutManager(linearLayoutManager);
        mChatAdapter = new ChatAdapter(this, mChatInfos);
        mRvChatview.setAdapter(mChatAdapter);
        getInfo("你好");
        setListener();
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

        mSrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mChatInfos.clear();
                mChatAdapter.notifyDataSetChanged();
                mSrRefresh.setRefreshing(false);
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
            chatListInfo.setIsShowTime(true);
        }
        chatListInfo.setMsgTime(lastTime);
        firstTime = lastTime;
        chatListInfo.setChatInfo(msg);
//        chatListInfo.setBitmap();
        chatListInfo.setType(1);
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
            chatListInfo.setIsShowTime(true);
        }
        chatListInfo.setMsgTime(lastTime);
        firstTime = lastTime;
        chatListInfo.setChatInfo(msg);
        chatListInfo.setType(2);
        mChatInfos.add(chatListInfo);
        mChatAdapter.notifyDataSetChanged();
        mRvChatview.smoothScrollToPosition(mChatInfos.size() - 1);

        if (msg.length() > 30) {
            getInfo(msg.substring(0, 30));
        } else {
            if (msg.length() <= 1) {
                getInfo(msg + "吗");
            } else {
                getInfo(msg);
            }
        }
    }

    @OnClick(R2.id.iv_backs)
    public void onViewClicked() {
        finish();
    }
}
