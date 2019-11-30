package com.matrix.yukun.matrix.chat_module.holderwrapper;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.entity.EventVoiceClick;
import com.matrix.yukun.matrix.chat_module.fragment.voice.PlayerManager;
import com.matrix.yukun.matrix.chat_module.holder.RightVoiceHolder;
import com.matrix.yukun.matrix.main_module.activity.LoginActivity;
import com.matrix.yukun.matrix.main_module.activity.PersonCenterActivity;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;
import com.matrix.yukun.matrix.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * author: kun .
 * date:   On 2019/3/14
 */
public class RightVoiceHolderWrapper {
    static RightVoiceHolderWrapper mRightTextHolderWrapper;
    Context mContext;
    private MediaPlayer mPlayer;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mVoiceHolder.mSeekBar.setProgress(mPlayer.getCurrentPosition());
                    mHandler.sendEmptyMessageDelayed(1,500);
                    break;
            }
        }
    };
    private RightVoiceHolder mVoiceHolder;

    public static RightVoiceHolderWrapper getInstance(){
        if(mRightTextHolderWrapper==null){
            mRightTextHolderWrapper=new RightVoiceHolderWrapper();
        }
        return mRightTextHolderWrapper;
    }

    public void content(Context context, final ChatListInfo chatListInfo, final RightVoiceHolder holder){
        this.mContext=context;
        if(chatListInfo.isShowTime()){
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");//获取当前时间
            String str = formatter.format(chatListInfo.getMsgTime());
            (holder).mTextViewRightTime.setText(str);
            (holder).mTextViewRightTime.setVisibility(View.VISIBLE);
        }else {
            ( holder).mTextViewRightTime.setVisibility(View.GONE);
        }
        if(MyApp.userInfo==null){
            GlideUtil.loadOptionsImage("",(holder).mImageViewRight,GlideUtil.getOptions(R.drawable.head_2));
        }else {
            GlideUtil.loadImage(MyApp.getUserInfo().getAvator(),(holder).mImageViewRight);
        }
        holder.tvSenond.setText(chatListInfo.getDuration()/1000+"''");
        if(!chatListInfo.isAudioIsPlay()){
            holder.mSeekBar.setVisibility(View.GONE);
            holder.mIvPlay.setImageResource(R.mipmap.icon_video_play);
        }else {
            mVoiceHolder=holder;
            holder.mSeekBar.setVisibility(View.VISIBLE);
            holder.mIvPlay.setImageResource(R.mipmap.icon_video_pause);
            if(new File(chatListInfo.getVideoPath()).exists()){
                PlayerManager.getInstance().play(chatListInfo.getVideoPath(), new PlayerManager.PlayCallback() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mPlayer = mediaPlayer;
                        holder.mSeekBar.setMax(mPlayer.getDuration());
                        holder.mSeekBar.setProgress(0);
                        mHandler.sendEmptyMessage(1);
                    }
                    @Override
                    public void onComplete() {
                        holder.mIvPlay.setImageResource(R.mipmap.icon_video_play);
                        holder.mSeekBar.setVisibility(View.GONE);
                        chatListInfo.setAudioIsPlay(false);
                        chatListInfo.saveOrUpdate();
                        if(mHandler!=null){
                            mHandler.removeMessages(1);
                        }
                    }
                    @Override
                    public void stop() {

                    }

                    @Override
                    public void progress(int size, int progress) {
                        LogUtil.i("==========",size+" "+progress);
                    }
                });
            }else {
                ToastUtils.showToast("语音播放出错");
            }
        }

        (holder).mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApp.userInfo!=null) {
                    Intent intent=new Intent(mContext, PersonCenterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }else {
                    Intent intent=new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.mIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventVoiceClick(chatListInfo));
            }
        });
    }
}
