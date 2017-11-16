package com.matrix.yukun.matrix.leshilive_module;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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

import com.bumptech.glide.Glide;
import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lecloud.sdk.videoview.mobile.MobileLiveVideoView;
import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.bean.AppConstants;
import com.matrix.yukun.matrix.leshilive_module.adapter.ViewAdapter;
import com.matrix.yukun.matrix.leshilive_module.bean.OnEventNumber;
import com.matrix.yukun.matrix.leshilive_module.giftview.GiftFrameLayout;
import com.matrix.yukun.matrix.leshilive_module.giftview.GiftSendModel;
import com.matrix.yukun.matrix.selfview.GiftView;
import com.matrix.yukun.matrix.util.AnimUtils;
import com.matrix.yukun.matrix.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @BindView(R.id.gift_big)
    ImageView giftBig;
    @BindView(R.id.gift_layout1)
    GiftFrameLayout giftFrameLayout1;
    @BindView(R.id.gift_layout2)
    GiftFrameLayout giftFrameLayout2;
    @BindView(R.id.gift)
    GiftView giftView;
    private RelativeLayout videoContainer;
    private IMediaDataVideoView videoView;
    private int GIFTCOUNT = 1;

    //mActionId,活动 id, 可调用OpenApi接口批量获取
    private String mActionId = "A2017032800000dh";
    private List<String> stringList = new ArrayList<>();
    //mUseHls = true,表示使用 ;mUseHls = false,表示使用 rtmp协议播放;

    private static final String playPath = "http://cache.utovr.com/201601131107187320.mp4";
//    private static final String playPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private static final String LivePath = "rtmp://19113.mpull.live.lecloud.com/live/mylive";
    private RecChatAdapter adapter;
    private PopupWindow popupWindow;
    private ArrayList<Integer> giftsList;
    private List<GiftSendModel> giftSendModelList = new ArrayList<GiftSendModel>();
    private String[] strings;
    private ViewAdapter adapter1;
    private int giftViewNum=7*1000;
    private int giftViewCount=1;

    //使用handler进行消息的处理，不断进行重绘
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                giftViewCount=giftViewCount+100;
                //重绘
                giftView.invalidate();
                mHandler.sendEmptyMessageDelayed(1, 100);
                //控制flow动画消失
                if(giftViewCount>giftViewNum){
                    giftView.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //视频播放界面，保持屏幕常亮利于视频观看体验
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_le_shi_live);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mActionId = getIntent().getStringExtra("activity_id");
        init();
        giftsList = AppConstants.getGift();
        strings = getApplicationContext().getResources().getStringArray(R.array.gift);
        setInfo();
        setListener();
        //礼物的屏幕
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        giftView.SetView(dm.heightPixels, dm.widthPixels);
        giftView.setVisibility(View.GONE);
        update();
    }

    private void init() {
        videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoView = new MobileLiveVideoView(this, mActionId);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        videoContainer.addView((View) videoView, params);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecChatAdapter(this, stringList);
        recyclerView.setAdapter(adapter);
    }

    private void setInfo() {
        videoView.setDataSource(playPath);
    }

    boolean tag = false;

    @OnClick({R.id.text_change, R.id.gift_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_change:
                videoView.resetPlayer();
                if (!tag) {
                    tag = true;
                    videoView.setDataSource(LivePath);
                    textChange.setText("切换源视频");
                } else {
                    tag = false;
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
        View view = LayoutInflater.from(this).inflate(R.layout.popu_layout, null);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        adapter1 = new ViewAdapter(getApplicationContext());
        viewPager.setAdapter(adapter1);
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, dip2px(this, 180.0f));
        popupWindow.setAnimationStyle(R.style.popwindow_style);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x9000000);
        popupWindow.setBackgroundDrawable(dw);
        LinearLayout layout = (LinearLayout) findViewById(R.id.lin_bottom);
        int[] location = new int[2];
        layout.getLocationOnScreen(location);
        popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());
    }

    @Subscribe
    public void getnumGift(OnEventNumber eventNumber) {
        int position = eventNumber.position;
        int number = eventNumber.number;
        GIFTCOUNT=number;
        if(position<11){
            //左边的动画
            startAnim(number,position);
            adapter1.setnum(1);//让num变成1个,回到初状态
        }else if(position<16){
            //大动画
            Glide.with(this).load(giftsList.get(position)).into(giftBig);
            AnimUtils.cancelAnim();
            AnimUtils.giftBigAnim(giftBig, this);
            adapter1.setnum(1);//让num变成1个,回到初状态
        }else if(position<20){
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessageDelayed(1, 100);
            giftView.produceGiftRandom(GIFTCOUNT);
            giftViewCount=1;
            if(position==16){
                giftView.getDrawables(AppConstants.getFlowGiftMap());
            }
            if(position==17){
                giftView.getDrawables(AppConstants.getFlowGiftRing());
            }
            if(position==18){
                giftView.getDrawables(AppConstants.getFlowGiftSocks());
            }
            if(position==19){
                giftView.getDrawables(AppConstants.getFlowGiftFish());
            }
            adapter1.setnum(1);//让num变成1个,回到初状态
            if(giftView.getVisibility()==View.GONE){
                giftView.setVisibility(View.VISIBLE);
            }
        }
    }
    //礼物的动画
    public void startAnim(int number,int pos) {
        GiftSendModel giftSendModel = new GiftSendModel();
        giftSendModel.setGiftCount(number-1);
        giftSendModel.setImage_id(giftsList.get(pos));
        giftSendModel.setSig("送你一个"+strings[pos]);
        starGiftAnimation(giftSendModel);
    }

    private void starGiftAnimation(GiftSendModel model) {

        if (!giftFrameLayout1.isShowing()) {
            //显示view
            if(giftFrameLayout1.getVisibility()==View.GONE){
                giftFrameLayout1.setVisibility(View.VISIBLE);
            }
            sendGiftAnimation(giftFrameLayout1,model);
        }else if(!giftFrameLayout2.isShowing()){
            //显示view
            if(giftFrameLayout2.getVisibility()==View.GONE){
                giftFrameLayout2.setVisibility(View.VISIBLE);
            }
            sendGiftAnimation(giftFrameLayout2,model);
        }else{
            giftSendModelList.add(model);
        }
    }

    private void sendGiftAnimation(final GiftFrameLayout view, GiftSendModel model){
        view.setModel(model);
        AnimatorSet animatorSet = view.startAnimation(model.getGiftCount());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (giftSendModelList) {
                    if (giftSendModelList.size() > 0) {
                        view.startAnimation(giftSendModelList.get(giftSendModelList.size() - 1).getGiftCount());
                        giftSendModelList.remove(giftSendModelList.size() - 1);
                    }
                }
            }
        });
    }

    //大礼物的动画
    public void update() {
        //发送延迟消息
//        mHandler.sendEmptyMessageDelayed(1, 100);
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
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (editChat.getText().toString() != null && editChat.getText().toString().length() >= 1) {
                        stringList.add(editChat.getText().toString());
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(stringList.size());
                        editChat.setText("");
                    } else {
                        MyApp.showToast("消息不能为空");
                    }
                    closeKeybord(editChat, LeShiLiveActivity.this);
                    return true;
                }
                return false;
            }
        });
    }

    public static void closeKeybord(EditText mEditText, Context mContext) {
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
        EventBus.getDefault().unregister(this);
        mHandler.removeMessages(1);
        mHandler=null;

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

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
