package com.le.skin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.recorder.bean.LivesInfo;
import com.letv.recorder.callback.LetvRecorderCallback;
import com.letv.recorder.controller.LetvPublisher;
import com.letv.recorder.letvrecorderskin.R;
import com.le.skin.ui.RecorderDialogBuilder;
import com.letv.recorder.ui.logic.RecorderConstance;
import com.letv.recorder.util.LeLog;

import java.util.ArrayList;

public class LePublisherSkinView extends BaseSkinView {
    private String userId;
    private String secretKey;
    private String activityId;
    private final static int ERROR_REQUEST_FAILED = 0x10;
    private final static int REQUEST_SUCCESS = 0x11;
    private Dialog machineDialog;
    private Dialog dialog;

    public LePublisherSkinView(Context context) {
        super(context);
    }

    public LePublisherSkinView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LePublisherSkinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean startPublisher() {
        super.startPublisher();
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(secretKey) || TextUtils.isEmpty(activityId)) {
            Log.d(TAG,"用户ID,秘钥和活动ID不可以为空");
            Toast.makeText(getContext(), "用户ID,秘钥和活动ID不可以为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        ((LetvPublisher) publisher).handleMachine(new LetvRecorderCallback<ArrayList<LivesInfo>>() {
            @Override
            public void onFailed(int code, String msg) {
                Log.d(TAG,"机位信息获取失败");
                Message message = leHandler.obtainMessage(ERROR_REQUEST_FAILED);
                message.arg1 = code;
                message.obj = msg;
                leHandler.sendMessage(message);
            }

            @Override
            public void onSucess(ArrayList<LivesInfo> data) {
                Log.d(TAG,"机位信息获取成功");
                Message message = leHandler.obtainMessage(REQUEST_SUCCESS);
                message.obj = data;
                leHandler.sendMessage(message);
            }
        });

        return true;
    }

    @Override
    protected void publisherMessage(Message msg) {
        switch (msg.what) {
            case RecorderConstance.LIVE_STATE_CANCEL_ERROR:
            case RecorderConstance.LIVE_STATE_END_ERROR:
                Log.w(TAG,"乐视云标准直播已结束");
                hideLoadingDialog();
                dialog = RecorderDialogBuilder.showCommentDialog(getContext(), "直播已结束，可再去创建您的直播活动","我知道了",null, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog = null;
                    }
                }, null);
                break;
            case RecorderConstance.LIVE_STATE_NOT_STARTED_ERROR:
                Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                Log.w(TAG,"乐视云标准直播还没有到开始");
                hideLoadingDialog();
                break;
            case RecorderConstance.LIVE_STATE_PUSH_COMPLETE:
                Log.d(TAG,"乐视云标准直接结束");
                Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_SHORT) .show();
                if(publisher.isRecording()){
                    stopPublisher();
                    openButton.setImageResource(R.drawable.letv_recorder_open);
                }
                break;
            case RecorderConstance.LIVE_STATE_NEED_RECORD:
                Bundle bundle = msg.getData();
                if(bundle != null){
                    isRec = bundle.getBoolean("bool",true);
                }
                break;
            case RecorderConstance.LIVE_STATE_TIME_REMAINING:
                break;
        }
    }

    private Handler leHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ERROR_REQUEST_FAILED:
                    Log.d(TAG,"接口请求失败,错误码:" + msg.arg1 + "," + msg.toString());
                    openButton.setImageResource(R.drawable.letv_recorder_open);
                    hideLoadingDialog();
                    Toast.makeText(getContext(), "接口请求失败,错误码:" + msg.arg1 + "," + msg.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_SUCCESS:
                    openButton.setImageResource(R.drawable.letv_recorder_open);
                    ArrayList<LivesInfo> data = (ArrayList<LivesInfo>) msg.obj;
                    int num = data.size();
                    if(skinParams.isFirstMachine() && num >0){
                        selectMachine(0);
                    }else {
                        switch (num) {
                            case 0:// 当前无机位
                                hideLoadingDialog();
                                Log.d(TAG,"无可用机位");
                                Toast.makeText(getContext(), "当前无机位", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:// 只有一个机位信息
                                selectMachine(0);
                                break;
                            default:// 多机位
                                hideLoadingDialog();
                                machineDialog = showMachineDialog(data);
                                break;
                        }
                    }

            }
        }
    };

    private Dialog showMachineDialog(ArrayList<LivesInfo> data) {
        Dialog dialog = new Dialog(getContext(), R.style.letvRecorderDialog);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.le_recorder_machine_layout,null);
        dialog.setContentView(view);
        TextView v1 = (TextView) view.findViewById(R.id.letv_recorder_machine_one);
        v1.setTag(0);
        v1.setOnClickListener(onClickListener);
        TextView v2 = (TextView) view.findViewById(R.id.letv_recorder_machine_two);
        v2.setTag(1);
        v2.setOnClickListener(onClickListener);
        TextView v3 = (TextView) view.findViewById(R.id.letv_recorder_machine_three);
        v3.setTag(2);
        v3.setOnClickListener(onClickListener);
        TextView v4 = (TextView) view.findViewById(R.id.letv_recorder_machine_four);
        v4.setTag(3);
        v4.setOnClickListener(onClickListener);
        for (int i = 0;i <data.size();i++){
            switch (i){
                case 0:
                    v1.setVisibility(View.VISIBLE);
                    if(data.get(0).status != 0){
                        v1.setBackgroundResource(R.drawable.letv_recorder_angle_gray);
                        v1.setClickable(false);
                    }
                    break;
                case 1:
                    v2.setVisibility(View.VISIBLE);
                    if(data.get(1).status != 0){
                        v2.setBackgroundResource(R.drawable.letv_recorder_angle_gray);
                        v2.setClickable(false);
                    }
                    break;
                case 2:
                    v3.setVisibility(View.VISIBLE);
                    if(data.get(2).status != 0){
                        v3.setBackgroundResource(R.drawable.letv_recorder_angle_gray);
                        v3.setClickable(false);
                    }
                    break;
                case 3:
                    v4.setVisibility(View.VISIBLE);
                    if(data.get(3).status != 0){
                        v4.setBackgroundResource(R.drawable.letv_recorder_angle_gray);
                        v4.setClickable(false);
                    }
                    break;
            }
        }
        dialog.show();
        return  dialog;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            showLoadingDialog();
            selectMachine((Integer) v.getTag());
        }
    };
    private void selectMachine(int numFlag) {
        openButton.setImageResource(R.drawable.letv_recorder_stop);
        if(machineDialog != null && machineDialog.isShowing()) {
            machineDialog.dismiss();
            machineDialog = null;
        }
        if (((LetvPublisher) publisher).selectMachine(numFlag)) {
            publisher.publish();
        } else {
            openButton.setImageResource(R.drawable.letv_recorder_open);
            hideLoadingDialog();
            Log.d(TAG,"该机位已经被其他人抢占了,请重新选择");
            Toast.makeText(getContext(), "该机位已经被其他人抢占了,请重新选择", Toast.LENGTH_SHORT).show();
        }

    }

    public void initPublish(String userId, String secretKey, String activityId) {
        Log.d(TAG,"initPublish，初始化");
        this.userId = userId;
        this.secretKey = secretKey;
        this.activityId = activityId;
        if(TextUtils.isEmpty(skinParams.getTitle())){
            nameView.setText(activityId);
        }
        LetvPublisher.init(activityId,userId,secretKey);
        publisher = LetvPublisher.getInstance();
        super.initPublish();
    }
}
