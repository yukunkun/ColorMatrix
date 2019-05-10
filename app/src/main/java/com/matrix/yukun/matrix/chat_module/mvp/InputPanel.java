package com.matrix.yukun.matrix.chat_module.mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.selfview.voice.AudioRecordLayout;
import com.matrix.yukun.matrix.video_module.fragment.PlayFragment;

/**
 * author: kun .
 * date:   On 2019/5/10
 */
public class InputPanel {
    private View mRootView;
    private InputListener mInputListener;
    private Context mContext;
    private EditText mEtMessage;
    private Button mBtSend;
    private Button mBtAdd;
    private ImageView mIvSendPhoto;
    private FrameLayout mFrameLayout;
    private AudioRecordLayout mAudioRecordLayout;

    public InputPanel(Context context,View rootView, InputListener inputListener) {
        mRootView = rootView;
        mContext=context;
        mInputListener = inputListener;
        initView();
        initListener();
    }

    private void initView() {
        mEtMessage = mRootView.findViewById(R.id.et_messg);
        mBtSend = mRootView.findViewById(R.id.send_btn);
        mBtAdd = mRootView.findViewById(R.id.bt_add);
        mIvSendPhoto = mRootView.findViewById(R.id.iv_photo);
        mFrameLayout = mRootView.findViewById(R.id.fl_contain);
        mAudioRecordLayout = mRootView.findViewById(R.id.record_layout);
        mAudioRecordLayout.setChatActivity((Activity) mContext);

    }

    private void initListener() {
        mAudioRecordLayout.setOnRecordStatusListener(new AudioRecordLayout.onRecordStatusListener() {

            @Override
            public void onRecordStart() {
                Log.v("test", "开始");
            }

            @Override
            public void onRecordComplete(/*VoiceClipMessage vcm*/) {
                Log.v("test", "完成");
//                boolean isSecret = mChatContainer.mSessionType == CubeSessionType.Secret;
//                VoiceClipMessage voiceClipMessage = MessageManager.getInstance().buildVoiceMessage(mChatContainer.mChatActivity, CubeSessionType.P2P, CubeSpUtil.getCubeUser().getCubeId(), mChatContainer.mChatId, vcm, isSecret);
//                MessageManager.getInstance().sendMessage(mChatContainer.mChatActivity, voiceClipMessage).subscribe(new OnNoneSubscriber<>());
            }

            @Override
            public void onAuditionStart(/*VoiceClipMessage vcm*/) {
//                LogUtil.e("试听" + vcm.getDuration());
//                //将当前fragment加入到返回栈中
//                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.voice_view, new PlayFragment(mChatContainer, vcm)).commit();
            }

            @Override
            public void onRecordCancel() {
                Log.v("test", "取消");
            }
        });
    }

}
