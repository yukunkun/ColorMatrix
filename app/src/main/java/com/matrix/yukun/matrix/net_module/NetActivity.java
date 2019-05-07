package com.matrix.yukun.matrix.net_module;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.R2;
import com.matrix.yukun.matrix.barrage_module.views.CircleTextView;
import com.matrix.yukun.matrix.net_module.bean.TrafficBean;
import com.matrix.yukun.matrix.selfview.ClockView;
import com.matrix.yukun.matrix.selfview.floatingview.FloatingViewManager;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class NetActivity extends BaseActivity {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_history)
    TextView mTvHistory;
    @BindView(R2.id.iv_upload)
    ImageView mIvUpload;
    @BindView(R2.id.tv_upload)
    TextView mTvUpload;
    @BindView(R2.id.iv_download)
    ImageView mIvDownload;
    @BindView(R2.id.tv_download)
    TextView mTvDownload;
    @BindView(R2.id.clock_view)
    ClockView mClockView;
    @BindView(R2.id.switch_view)
    Switch mSwitchView;
    @BindView(R2.id.seekbar_tv)
    SeekBar mSeekbarTv;
    @BindView(R2.id.seekbar_bg)
    SeekBar mSeekbarBg;
    @BindView(R2.id.ll_float_contain)
    LinearLayout mLlFloatContain;
    @BindView(R2.id.ctv_uplolad_bg)
    CircleTextView mCtvUploladBg;
    @BindView(R2.id.ctv_download_bg)
    CircleTextView mCtvDownloadBg;
    @BindView(R2.id.ctv_bg)
    CircleTextView mCtvBg;
    private TextView mTvFUpload;
    private TextView mTvFDownload;
    private LinearLayout mRlFContain;
    private int type=0;
    @ColorInt int mFColorBf;
    private  Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    mTvFDownload.setText(String.format("%.2f", msg.obj) + "kb/s");
                }
                if (msg.what == 2) {
                    mTvFUpload.setText(String.format("%.2f", msg.obj) + "kb/s");
                }
                super.handleMessage(msg);
            }
        };
    private TrafficBean mTrafficBean;
    private View mInflate;
    private FloatingViewManager.Configs mConfigs;


    @Override
    public int getLayout() {
        return R.layout.activity_net;
    }

    @Override
    public void initView() {
        mInflate = LayoutInflater.from(NetActivity.this).inflate(R.layout.float_net_item,null);
        mTvFUpload = mInflate.findViewById(R.id.tv_upload);
        mTvFDownload= mInflate.findViewById(R.id.tv_download);
        mRlFContain = mInflate.findViewById(R.id.rl_contain);
        mSeekbarTv.setMax(20);
        mSeekbarTv.setProgress(10);
        mSeekbarBg.setMax(255);
    }

    @Override
    public void initDate() {
        mTrafficBean = new TrafficBean(NetActivity.this, handler);
        double netUploadSpeed = mTrafficBean.getTotalNet()/1024/1024;
        if(netUploadSpeed>1024){
            mClockView.setCompleteDegree((float) (100));
        }else {
            mClockView.setCompleteDegree((float) (netUploadSpeed/10));
        }
    }

    @Override
    public void initListener() {
        mSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mLlFloatContain.setVisibility(View.VISIBLE);
                    showFloatView();
                    mTrafficBean.startCalculateNetSpeed();
                }else {
                    mLlFloatContain.setVisibility(View.GONE);
                    if(mTrafficBean!=null){
                        mTrafficBean.stopCalculateNetSpeed();
                    }
                    FloatingViewManager.removeFloatingView();
                }
            }
        });
        mSeekbarTv.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser&&mTvFUpload!=null){
                    mTvFUpload.setTextSize(5+progress);
                    mTvFDownload.setTextSize(5+progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekbarBg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser&&mRlFContain!=null){
                    mRlFContain.getBackground().setAlpha(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showFloatView() {
        mConfigs = new FloatingViewManager.Configs();
        mConfigs.floatingViewX = ScreenUtil.getDisplayWidth();   // 设置悬浮窗的X坐标
        mConfigs.floatingViewY = 0/*ScreenUtil.getDisplayHeight()- ScreenUtils.dp2Px(NetActivity.this,200)*/;  // 设置悬浮窗的Y坐标
        mConfigs.overMargin = -ScreenUtil.dip2px(8); // 设置悬浮窗距离边缘的外边距
        FloatingViewManager.getInstance(NetActivity.this).addFloatingView(mInflate,mConfigs);
    }

    @OnClick({R2.id.iv_back, R2.id.tv_history, R2.id.ctv_uplolad_bg, R2.id.ctv_download_bg, R2.id.ctv_bg})
    public void onViewClicked(View view) {
        int id = view.getId();
        if( id==R.id.iv_back){
            finish();
        } if( id==R.id.tv_history){
            HistoryActivity.start(this);
        }
        if( id==R.id.ctv_uplolad_bg){
            type=0;
            showColorPicker();
        }
        if( id==R.id.ctv_download_bg){
            type=1;
            showColorPicker();
        }
        if( id==R.id.ctv_bg){
            type=2;
            showColorPicker();
        }
    }
    private void showColorPicker() {
        //传入的默认color
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder()/*.setColor(0)*/
                .setDialogTitle(R.string.color_picker)
                //设置dialog标题
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                //设置为自定义模式
                .setShowAlphaSlider(false)
                //设置有透明度模式，默认没有透明度
                .setDialogId(80)
                //设置Id,回调时传回用于判断
                .setAllowPresets(false)
                //不显示预知模式
                .create();
        //Buider创建
        colorPickerDialog.setColorPickerDialogListener(pickerDialogListener);
        //设置回调，用于获取选择的颜色
        colorPickerDialog.show(this.getFragmentManager(), "color-picker-dialog");
    }

    private ColorPickerDialogListener pickerDialogListener = new ColorPickerDialogListener() {
        @Override
        public void onColorSelected(int dialogId, @ColorInt int color) {
            if(type==0){
                mCtvUploladBg.setBackColor(color);
                if(mTvFUpload!=null){
                    mTvFUpload.setTextColor(color);
                }
            }else if(type==1){
                mCtvDownloadBg.setBackColor(color);
                if(mTvFDownload!=null){
                    mTvFDownload.setTextColor(color);
                }
            }else if(type==2){
                mFColorBf=color;
                mCtvBg.setBackColor(color);
                if(mRlFContain!=null){
                    mRlFContain.setBackgroundColor(color);
                }
            }
        }

        @Override
        public void onDialogDismissed(int dialogId) {

        }
    };
}
