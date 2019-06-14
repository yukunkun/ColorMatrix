package com.matrix.yukun.matrix.selfview.voice;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatBaseActivity;
import com.matrix.yukun.matrix.chat_module.inputListener.VoiceRecordListener;

import java.io.File;

/**
 * 自定义语音功能布局
 *
 * @author Wangxx
 * @date 2017/2/8
 */
public class AudioRecordLayout extends RelativeLayout implements VoiceRecordListener {

    private static final int UPDATE_TIME = 0;

    /**
     * 录制结果接口
     */
    private onRecordStatusListener mOnRecordStatusListener;
    /**
     * 松开试听按钮
     */
    private AuditionButton         mAuditionBtn;
    /**
     * 删除语音按钮
     */
    private AuditionButton         mDeleteVoiceBtn;
    /**
     * 录制状态文字显示
     */
    private TextView               mRecordText;
    /**
     * 录制按钮
     */
    private ImageView              mRecordBtn;
    /**
     * 录制抖动效果显示的View
     */
    private HorVoiceView           mHorVoiceView;
    /**
     * 用户当前按下的X坐标
     */
    private float                  currentX;
    /**
     * 用户当前按钮下的Y坐标
     */
    private float                  currentY;

    /**
     * 试听按钮中心点的X坐标
     */
    private float  auditionX;
    /**
     * 试听按钮中心点的X坐标
     */
    private float  auditionY;
    /**
     * 删除按钮中心带你的X坐标
     */
    private float  deleteX;
    /**
     * 删除按钮中心带你的Y坐标
     */
    private float  deleteY;
    /**
     * 试听按钮和录制按钮之间的距离
     */
    private double distance1;
    /**
     * 删除按钮和录制按钮之间的距离
     */
    private double distance2;
    /**
     * 删除按钮的宽度
     */
    private int    delBtnWidth;
    /**
     * 删除按钮的高度
     */
    private int    delBtnHeight;
    /**
     * 试听按钮的宽度
     */
    private int    auditionBtnWidth;
    /**
     * 试听按钮的高度
     */
    private int    auditionBtnHeight;

    //判断第一次进来
    private boolean isFlag     = true;
    /**
     * 最大录制时间
     */
    private long    mRecordMax = 59 * 1000;

    private long mTime = 0;

    /**
     * 波动数值
     */
    private Integer mWave;
    private Context mContext;

    /**
     * 录制状态
     */
    public enum RecordStatus {
        PREPARE_RECORD, START_RECORD, CANCEL_RECORD
    }

    /**
     * 默认是准备录制状态
     */
    private RecordStatus mStatus = RecordStatus.PREPARE_RECORD;

    private ChatBaseActivity mActivity;

    public void setChatActivity(ChatBaseActivity chatActivity) {
        this.mActivity = chatActivity;
    }

    /**
     * 当前录制的时间
     */
    private int currentTime;
    /**
     * 更新时间
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_TIME) {
                if (mStatus == RecordStatus.START_RECORD) {
                    if (currentTime >= mRecordMax) {
//                        VoiceClipMessage vcm = CubeEngine.getInstance().getMediaService().stopVoiceClipRecording();
//                        if (vcm != null && mOnRecordStatusListener != null) {
                            mOnRecordStatusListener.onAuditionStart(/*vcm*/);
//                            mOnRecordStatusListener.onRecordComplete(vcm);
//                            mStatus = RecordStatus.CANCEL_RECORD;
                            updateButton();
//                            mAuditionBtn.setSelected(false);
//                            mDeleteVoiceBtn.setSelected(false);
//                        }
//                        mHandler.removeMessages(UPDATE_TIME);
                        return;
                    }
                    currentTime += 1000;
                    mHorVoiceView.setText(showTimeCount(currentTime));
                    mHandler.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
                }
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置监听器
     *
     * @param mOnRecordStatusListener
     */
    public void setOnRecordStatusListener(onRecordStatusListener mOnRecordStatusListener) {
        this.mOnRecordStatusListener = mOnRecordStatusListener;
    }

    public AudioRecordLayout(Context context) {
        this(context, null);
    }

    public AudioRecordLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioRecordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取播放View
        mAuditionBtn = (AuditionButton) findViewById(R.id.img_playView);
        mDeleteVoiceBtn = (AuditionButton) findViewById(R.id.img_delView);
        mRecordText = (TextView) findViewById(R.id.tv_record);
        mRecordBtn = (ImageView) findViewById(R.id.btn_record);
        mHorVoiceView = (HorVoiceView) findViewById(R.id.horvoiceview);
        onMeasureWidthAndHeight();
    }

    /**
     * 测量各控件宽度和高度
     */
    public void onMeasureWidthAndHeight() {
        //获取删除按钮宽和高
        ViewTreeObserver vto = mDeleteVoiceBtn.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mDeleteVoiceBtn.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                delBtnWidth = mDeleteVoiceBtn.getWidth();
                delBtnHeight = mDeleteVoiceBtn.getHeight();
            }
        });
        //获取试听按钮的宽和高
        ViewTreeObserver vto1 = mAuditionBtn.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mAuditionBtn.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                auditionBtnWidth = mAuditionBtn.getWidth();
                auditionBtnHeight = mAuditionBtn.getHeight();
            }
        });
    }

    /**
     * 获取各按钮中心点的坐标
     */
    public void onCenterCoordinates() {
        //获取试听按钮中心点的坐标
        auditionX = mAuditionBtn.getX() + auditionBtnWidth / 2;
        auditionY = mAuditionBtn.getY() + auditionBtnHeight / 2;
        //获取删除按钮中心点的坐标
        deleteX = mDeleteVoiceBtn.getX() + delBtnWidth / 2;
        deleteY = mDeleteVoiceBtn.getY() + delBtnHeight / 2;
    }

    /**
     * @param event
     *
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = event.getX();
        currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                long tempTime = System.currentTimeMillis();
                if (tempTime - mTime < 500) {
                    break;
                }
                mTime = tempTime;
                //判断当前按下的坐标是否在按钮范围里面
                if (containRecordBtn()) {
                   //开始录制
                    mStatus = RecordStatus.START_RECORD;
//                    CubeEngine.getInstance().getMediaService().startVoiceClipRecording(this);
                    updateButton();
                    mHorVoiceView.setText("00:00");
                    mHandler.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
                    if (mOnRecordStatusListener != null) {
                        mOnRecordStatusListener.onRecordStart();
                    }
                    if (isFlag) {
                        onCenterCoordinates();
                        distance1 = getCoordinateDistance(auditionX, auditionY, currentX, currentY);
                        distance2 = getCoordinateDistance(currentX, currentY, deleteX, deleteY);
                        isFlag = false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStatus == RecordStatus.START_RECORD) {
                    //松开
                    if (containDeleteView()) {
                        hideTimeLayout();
                        mRecordText.setText("松开取消");
                    }
                    else if (containPlayView()) {
                        hideTimeLayout();
                        mRecordText.setText("松开播放");
                    }
                    else {
                        showTimeLayout();
                        mHorVoiceView.setText(showTimeCount(currentTime));
                    }
                    double distance = getCoordinateDistance(auditionX, auditionY, currentX, currentY);
                    mAuditionBtn.setDistance(distance / distance1);

                    double distance1 = getCoordinateDistance(currentX, currentY, deleteX, deleteY);
                    mDeleteVoiceBtn.setDistance(distance1 / distance2);
                }
                break;
            case MotionEvent.ACTION_UP:
                mStatus = RecordStatus.CANCEL_RECORD;
                resetTime();
                if (containDeleteView()) {
//                    CubeEngine.getInstance().getMediaService().discardVoiceClipRecording();
                    if (mOnRecordStatusListener != null) {
                        mOnRecordStatusListener.onRecordCancel();
                    }
                }
                else if (containPlayView()) {
//                    VoiceClipMessage vcm = CubeEngine.getInstance().getMediaService().stopVoiceClipRecording();
                    if (/*vcm != null &&*/ mOnRecordStatusListener != null) {
                        mOnRecordStatusListener.onAuditionStart(/*vcm*/);
                    }
                }
                else if (containRecordBtn()) {
//                    VoiceClipMessage vcm = CubeEngine.getInstance().getMediaService().stopVoiceClipRecording();
                    if (/*vcm != null && */ mOnRecordStatusListener != null) {
                        mOnRecordStatusListener.onRecordComplete(/*vcm*/);
                    }
                }else {
                    if (/*vcm != null &&*/ mOnRecordStatusListener != null) {
                        mOnRecordStatusListener.onRecordCancel(/*vcm*/);
                    }
                }
                updateButton();
                mAuditionBtn.setSelected(false);
                mDeleteVoiceBtn.setSelected(false);
        }
        return true;
    }

    /**
     * 隐藏录制时间布局
     */
    public void hideTimeLayout() {
        mRecordText.setVisibility(VISIBLE);
        mHorVoiceView.setVisibility(GONE);
    }

    /**
     * 显示录制时间布局
     */
    public void showTimeLayout() {
        mRecordText.setVisibility(GONE);
        mHorVoiceView.setVisibility(VISIBLE);
    }

    /**
     * 松开按钮重置时间
     */
    public void resetTime() {
        mHandler.removeMessages(UPDATE_TIME);
        mRecordText.setText("00:00");
        currentTime = 0;
    }

    /**
     * 更新显示
     */
    public void updateButton() {
        if (mStatus == RecordStatus.START_RECORD) {
            mAuditionBtn.setVisibility(VISIBLE);
            mDeleteVoiceBtn.setVisibility(VISIBLE);
            //波动效果显示
            showTimeLayout();
        }
        else {
            mRecordText.setText("按住说话");
            //试听功能
            mAuditionBtn.setDistance(1f);
            mAuditionBtn.setVisibility(GONE);
            //删除功能
            mDeleteVoiceBtn.setDistance(1f);
            mDeleteVoiceBtn.setVisibility(GONE);
            //波动效果隐藏
            hideTimeLayout();
        }
    }

    /**
     * 获取二个坐标点之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     *
     * @return
     */
    public double getCoordinateDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 判断当前当前按住的点是否在录制按钮里面
     *
     * @return
     */
    public boolean containRecordBtn() {
        return (mRecordBtn.getX() < currentX && (mRecordBtn.getX() + mRecordBtn.getWidth()) > currentX) && (mRecordBtn.getY() < currentY && (mRecordBtn.getY() + mRecordBtn.getHeight()) > currentY);
    }

    /**
     * 判断当前滑动范围是否在试听按钮里面
     */
    public boolean containPlayView() {
        if ((mAuditionBtn.getX() < currentX && (mAuditionBtn.getX() + mAuditionBtn.getWidth()) > currentX) && (mAuditionBtn.getY() < currentY && (mAuditionBtn.getY() + mAuditionBtn.getHeight()) > currentY)) {
            mRecordText.setText("试听");
            mAuditionBtn.setSelected(true);
            return true;
        }
        mAuditionBtn.setSelected(false);
        return false;
    }

    /**
     * 判断当前滑动范围是否在删除里面
     */
    public boolean containDeleteView() {
        if ((mDeleteVoiceBtn.getX() < currentX && (mDeleteVoiceBtn.getX() + mDeleteVoiceBtn.getWidth()) > currentX) && (mDeleteVoiceBtn.getY() < currentY && (mDeleteVoiceBtn.getY() + mDeleteVoiceBtn.getHeight()) > currentY)) {
            mDeleteVoiceBtn.setSelected(true);
            return true;
        }
        mDeleteVoiceBtn.setSelected(false);
        return false;
    }

    /**
     * 转换时间
     *
     * @param time
     *
     * @return
     */
    public String showTimeCount(long time) {
        if (time >= 360000000) {
            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time / 3600000;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600000) / (60000);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());
        timeCount = minue + ":" + sec;
        return timeCount;
    }

    /**
     * 录制状态监听器
     */
    public interface onRecordStatusListener {

        /**
         * 录制开始
         */
        void onRecordStart();

        /**
         * 录制完成
         *
         * @param
         */
        void onRecordComplete(/*VoiceClipMessage vcm*/);

        /**
         * 开始试听
         *
         * @param
         */
        void onAuditionStart(/*VoiceClipMessage vcm*/);

        /**
         * 录制取消
         */
        void onRecordCancel();
    }

    @Override
    public void onVoiceRecordStart() {

    }

    @Override
    public void onVoiceRecordStop(File file, int i) {

    }

    @Override
    public void onDiscardVoiceRecord() {

    }

    @Override
    public void onVoiceVolumeChange(int i) {
        mWave = i * 5;
        if (mWave > 100) {
            mWave = 100;
        }
        mHorVoiceView.addElement(mWave);
    }

//    @Override
//    public void onVoiceRecordFailed(CubeError cubeError) {
//        LogUtil.i("fldy", "onVoiceRecordFailed" + cubeError.code + " " + cubeError.desc);
//        if (mChatActivity.isFinishing()) {
//            return;
//        }
//        if (CubeErrorCode.convert(cubeError.code) == CubeErrorCode.VoiceClipTooShort) {
//            ToastUtil.showToastTime(mContext, R.string.record_too_short, 800);
//        }
//        if (CubeErrorCode.convert(cubeError.code) == CubeErrorCode.VoiceClipError) {
//            ToastUtil.showToast(mContext, R.string.record_error);
//        }
//    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            reset();
        }
    }

    public void reset() {
        mStatus = RecordStatus.PREPARE_RECORD;
        resetTime();
        updateButton();
    }
}
