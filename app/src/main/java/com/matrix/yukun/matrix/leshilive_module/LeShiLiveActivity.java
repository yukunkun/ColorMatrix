package com.matrix.yukun.matrix.leshilive_module;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.sdk.videoview.mobile.MobileLiveVideoView;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeShiLiveActivity extends AppCompatActivity {
    @BindView(R.id.text_change)
    TextView textChange;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edit_chat)
    EditText editChat;
    @BindView(R.id.gift_list)
    ImageView giftList;
    private RelativeLayout videoContainer;
    private IMediaDataVideoView videoView;

    //mActionId,活动 id, 可调用OpenApi接口批量获取
    private String mActionId = "A2017032800000dh";
    private List<String> stringList=new ArrayList<>();
    //mUseHls = true,表示使用 hls协议播放;mUseHls = false,表示使用 rtmp协议播放;
    //默认使用 rtmp协议播放
    private boolean mUseHls = false;

    private static final String playPath = "http://cache.utovr.com/201601131107187320.mp4";
    private static final String LivePath="rtmp://19113.mpull.live.lecloud.com/live/mylive";
    private RecAcatAdapter adapter;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //视频播放界面，保持屏幕常亮利于视频观看体验
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_le_shi_live);
        ButterKnife.bind(this);
        mActionId = getIntent().getStringExtra("activity_id");
        String ActivityName = getIntent().getStringExtra("activity_name");
        init();
        setInfo();
        setListener();
    }

    private void init() {
        videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoView = new MobileLiveVideoView(this, mActionId);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        videoContainer.addView((View) videoView, params);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecAcatAdapter(this,stringList);
        recyclerView.setAdapter(adapter);
    }

    private void setInfo() {
        videoView.setDataSource(playPath);
    }
    boolean tag=false;
    @OnClick({R.id.text_change, R.id.gift_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_change:
                videoView.resetPlayer();
                if(!tag){
                    tag=true;
                    videoView.setDataSource(LivePath);
                    textChange.setText("切换源视频");
                }else {
                    tag=false;
                    videoView.setDataSource(playPath);
                    textChange.setText("切换直播间");
                }
                break;
            case R.id.gift_list:
                showGift();
                break;
        }
    }

    private void showGift() {
        View view=LayoutInflater.from(this).inflate(R.layout.popu_layout,null);
        ImageView imageViewGift_1= (ImageView) view.findViewById(R.id.gift_1);
        ImageView imageViewGift_2= (ImageView) view.findViewById(R.id.gift_2);
        ImageView imageViewGift_3= (ImageView) view.findViewById(R.id.gift_3);
        ImageView imageViewGift_4= (ImageView) view.findViewById(R.id.gift_4);
        imageViewGift_1.setOnClickListener(new GiftListener());
        imageViewGift_2.setOnClickListener(new GiftListener());
        imageViewGift_3.setOnClickListener(new GiftListener());
        imageViewGift_4.setOnClickListener(new GiftListener());
        popupWindow = new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT,180);
        popupWindow.setAnimationStyle(R.style.popwindow_style);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        LinearLayout layout= (LinearLayout) findViewById(R.id.lin_bottom);
        int[] location = new int[2];
        layout.getLocationOnScreen(location);

        popupWindow.showAtLocation(layout,Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());
//        popupWindow.showAtLocation(LeShiLiveActivity.this.findViewById(R.id.parent), Gravity.BOTTOM,0,0);
    }
    //礼物的点击时事件
    class GiftListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.gift_1:
                    popupWindow.dismiss();
                    break;
                case R.id.gift_2:
                    popupWindow.dismiss();
                    break;
                case R.id.gift_3:
                    popupWindow.dismiss();
                    break;
                case R.id.gift_4:
                    popupWindow.dismiss();
                    break;
            }
        }
    }

    private void setListener() {
        VideoViewListener videoViewListener = new VideoViewListener() {
            @Override
            public void onStateResult(int event, Bundle bundle) {
                handlePlayerEvent(event, bundle);//处理播放器事件
            }

            @Override
            public String onGetVideoRateList(LinkedHashMap<String, String> linkedHashMap) {
                return null;
            }
        };

        videoView.setVideoViewListener(videoViewListener);
        editChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEND){
                    if(editChat.getText().toString()!=null&&editChat.getText().toString().length()>=1){
                        stringList.add(editChat.getText().toString());
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(stringList.size());
                        editChat.setText("");
                    }else {
                        MyApp.showToast("消息不能为空");
                    }
                    closeKeybord(editChat,LeShiLiveActivity.this);
                    return true;
                }
                return false;
            }
        });

    }
    public static void closeKeybord(EditText mEditText, Context mContext){

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 处理播放器本身事件，具体事件可以参见
     * IPlayer
     * 类
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {
            case PlayerEvent.PLAY_PREPARED:
                //播放器准备完成，此刻调用 start() 就可以进行播放了
                if (videoView != null) {
                    videoView.onStart();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
    }

    public void PlayBack(View view) {
        finish();
    }
}
