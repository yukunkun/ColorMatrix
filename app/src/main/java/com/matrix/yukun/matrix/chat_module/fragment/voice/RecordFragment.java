package com.matrix.yukun.matrix.chat_module.fragment.voice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatBaseActivity;
import com.matrix.yukun.matrix.selfview.voice.AudioRecordLayout;

/**
 * @author Wangxx
 * @date 2017/2/8
 */
@SuppressLint("ValidFragment")
public class RecordFragment extends Fragment {
    private AudioRecordLayout mAudioRecordLayout;
    private View              mRootView;
    private View     mChatContainer;
    private ChatBaseActivity mChatActivity;

    /**
     * Fragment必须要有空的构造函数，否则直接crash。因为Fragment源码中用到反射构造了对象，是无参数的构造函数
     * @SuppressLint("ValidFragment")按理说忽略警告是有用的，但是在这好像没用
     */
    public RecordFragment() {

    }

    public RecordFragment(View chatContainer, Activity chatActivity) {
        this.mChatContainer = chatContainer;
        this.mChatActivity = (ChatBaseActivity)chatActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chat_record, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAudioRecordLayout = (AudioRecordLayout) mRootView.findViewById(R.id.record_layout);
        mAudioRecordLayout.setChatActivity(mChatActivity);
        mAudioRecordLayout.setOnRecordStatusListener(new AudioRecordLayout.onRecordStatusListener() {

            @Override
            public void onRecordStart() {
                Log.v("test", "开始");
            }

            @Override
            public void onRecordComplete(/*VoiceClipMessage vcm*/) {
                Log.v("test", "完成");
//                boolean isSecret = mChatContainer.mSessionType == CubeSessionType.Secret;
//                VoiceClipMessage voiceClipMessage = MessageManager.getInstance().buildVoiceMessage(mChatContainer.mChatActivity, CubeSessionType.P2P, SpUtil.getCubeId(), mChatContainer.mChatId, vcm, isSecret);
//                MessageManager.getInstance().sendMessage(mChatContainer.mChatActivity, voiceClipMessage).subscribe();
            }

            @Override
            public void onAuditionStart(/*VoiceClipMessage vcm*/) {
//                LogUtil.e("试听" + vcm.getDuration());
//                //将当前fragment加入到返回栈中
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_contain, new PlayFragment(/*mChatContainer, vcm*/)).commit();
            }

            @Override
            public void onRecordCancel() {
                Log.v("test", "取消");
            }
        });
    }
}
