package com.matrix.yukun.matrix.chat_module.fragment.voicefragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.selfview.voice.HorVoiceView;
import com.matrix.yukun.matrix.selfview.voice.PlayStartView;
import com.matrix.yukun.matrix.util.log.LogUtil;

/**
 * @author Wangxx
 * @date 2017/2/8
 */

@SuppressLint("ValidFragment")
public class PlayFragment extends Fragment implements View.OnClickListener {
    private View          mRootView;
    private PlayStartView mPlay;
    private HorVoiceView mHorVoiceView;
    private Button        mCancel;
    private Button        mSend;
    private boolean       isPlay;
//    private VoiceClipMessage mVoiceClipMessage = null;
//    private ChatContainer mChatContainer;
    private int           mDuration;

    public PlayFragment(/*ChatContainer chatContainer, VoiceClipMessage vcm*/) {
//        this.mVoiceClipMessage = vcm;
//        this.mChatContainer = chatContainer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chat_play, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlay = (PlayStartView) mRootView.findViewById(R.id.btn_play);
        mHorVoiceView = (HorVoiceView) mRootView.findViewById(R.id.horvoiceview);
        mCancel = (Button) mRootView.findViewById(R.id.btn_cancel);
        mSend = (Button) mRootView.findViewById(R.id.btn_send);
        LogUtil.i("录音长度" + mVoiceClipMessage.getDuration());
//        mDuration = mVoiceClipMessage.getDuration() * 1000;
        mPlay.setMaxTime(mDuration);
        mHorVoiceView.setText(showTimeCount(mDuration));
        initListener();
    }

    private void initListener() {
        mSend.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mPlay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                LogUtil.i("开启播放" + isPlay);
                if (isPlay) {
                    mPlay.restore();
                    isPlay = false;
                }
                else {
                    mPlay.startPlay();
                    isPlay = true;
                }
            }
        });
        mPlay.setOnPlayListener(new PlayStartView.OnPlayListener() {
            @Override
            public void onStartPlay() {
//                PlayerManager.getInstance().play(mVoiceClipMessage.getFile().getAbsolutePath(), new PlayerManager.PlayCallback() {
//                    @Override
//                    public void onPrepared() {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void stop() {
//                        mHorVoiceView.setText(showTimeCount(mDuration));
//                        mHorVoiceView.stopRecord();
//                    }
//                });
            }

            @Override
            public void onPlaying(long progress) {
                mHorVoiceView.setText(showTimeCount(progress));
                mHorVoiceView.addElement((int) Math.round(Math.random() * 100 + 1));
            }

            @Override
            public void onStopPlay() {
//                PlayerManager.getInstance().stop();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) {
//            PlayerManager.getInstance().stop();
//            //从栈中将当前fragment退出
//            getFragmentManager().popBackStack();
        }
        else if (v.getId() == R.id.btn_send) {
//            PlayerManager.getInstance().stop();
//            boolean isSecret = mChatContainer.mSessionType == CubeSessionType.Secret;
//            VoiceClipMessage voiceClipMessage = MessageManager.getInstance().buildVoiceMessage(mChatContainer.mChatActivity, CubeSessionType.P2P, SpUtil.getCubeId(), mChatContainer.mChatId, mVoiceClipMessage, isSecret);
//            MessageManager.getInstance().sendMessage(mChatContainer.mChatActivity, voiceClipMessage).subscribe();
            //从栈中将当前fragment退出
            getFragmentManager().popBackStack();
        }
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
}
