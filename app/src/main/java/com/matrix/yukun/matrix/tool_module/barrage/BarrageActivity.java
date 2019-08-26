package com.matrix.yukun.matrix.tool_module.barrage;

import android.content.Context;
import android.graphics.Typeface;
import android.os.PowerManager;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.barrage.dialog.HistoryDialog;
import com.matrix.yukun.matrix.tool_module.barrage.dialog.SettingDialog;
import com.matrix.yukun.matrix.tool_module.barrage.views.VerticalTextView;

public class BarrageActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mRlBottonLayout;
    private ImageView mIvSetting;
    private ImageView mIvHistory;
    private EditText mEtMsg;
    private VerticalTextView mTvMsg;
    private TextView mTvSend;
    private SettingDialog mSettingDialog;

    private  boolean isBig;
    private  boolean isLean;
    private int speed=10;
    private int size=80;
    private RelativeLayout mRlBg;
    private RelativeLayout mRl_layout;

    private VerticalTextView mVerticalTextView;
    private int mBgColor=-1;
    private int mTextColor=-14575885;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public int getLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_barrage;
    }

    @Override
    public void initView() {
        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);

        if (powerManager != null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WakeLock");
        }

        mRlBottonLayout = findViewById(R.id.rl_bottom_layout);
        mIvSetting = findViewById(R.id.iv_setting);
        mIvHistory = findViewById(R.id.iv_history);
        mEtMsg = findViewById(R.id.et_msg);
        mTvSend = findViewById(R.id.tv_send);
        mRlBg = findViewById(R.id.rl_bg);
        mVerticalTextView = findViewById(R.id.vertical_textview);
        mRl_layout = findViewById(R.id.rl_barrage_layout);
        mVerticalTextView.startAnimation();
    }

    @Override
    public void initListener() {
        mTvSend.setOnClickListener(this);
        mIvHistory.setOnClickListener(this);
        mIvSetting.setOnClickListener(this);
        mRl_layout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWakeLock != null) {
            mWakeLock.acquire();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock != null) {
            mWakeLock.release();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_send:
                if(!TextUtils.isEmpty(mEtMsg.getText().toString())){
                    mVerticalTextView.setText(mEtMsg.getText().toString());
                    //关闭键盘
                    hintKeyBoard();
                    BarrageHistory barrageHistory=new BarrageHistory();
                    barrageHistory.setContent(mEtMsg.getText().toString());
                    barrageHistory.save();
                }else {
                    ToastUtils.showToast(getResources().getString(R.string.please_enter_msg));
                }
                break;
            case R.id.iv_setting:
                mSettingDialog = SettingDialog.getInstance(isBig,isLean,size,speed,mBgColor,mTextColor);
                mSettingDialog.show(getSupportFragmentManager(),"");
                mRlBottonLayout.setVisibility(View.GONE);
                break;
            case R.id.iv_history:
                HistoryDialog historyDialog= HistoryDialog.getInstance();
                historyDialog.show(getSupportFragmentManager(),"");
                mRlBottonLayout.setVisibility(View.GONE);
                break;
            case R.id.rl_barrage_layout:
                if(mRlBottonLayout.getVisibility()==View.VISIBLE){
                    mRlBottonLayout.setVisibility(View.GONE);
                }else {
                    mRlBottonLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void dialogDismiss(){
        mRlBottonLayout.setVisibility(View.VISIBLE);
    }
    public void getTextSpeed(int speed){
        this.speed=speed;
        mVerticalTextView.setSpeed(speed);
    }

    public void getTextSize(int size){
        this.size=size;
        mVerticalTextView.setWidth(size);
    }
    public void getBgColor(@ColorInt int color){
        mBgColor = color;
        mRlBg.setBackgroundColor(color);
    }
    public void getTextColor(@ColorInt int color){
        mTextColor = color;
        mVerticalTextView.setColor(color);
    }

    public void getIsBig(boolean isBig){
        this.isBig=isBig;
        if(isBig){
            Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
            mVerticalTextView.setFont(font);
        }else {
            Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
            mVerticalTextView.setFont(font);
        }
    }

    public void getIsLean(boolean isLean){
        this.isLean=isLean;
        if(isLean){
            Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
            mVerticalTextView.setFont(font);
        }else {
            Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
            mVerticalTextView.setFont(font);
        }
    }

    public void getHistoryContent(String historyContent){
        mEtMsg.setText(historyContent);
        mVerticalTextView.setText(historyContent);
    }


    @Override
    public void onBackPressed() {
        mVerticalTextView.stopAnimation();
        super.onBackPressed();
    }

    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
