package com.matrix.yukun.matrix.chat_module.holderwrapper;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.entity.ChatListInfo;
import com.matrix.yukun.matrix.chat_module.entity.EventVoiceClick;
import com.matrix.yukun.matrix.chat_module.fragment.voice.PlayerManager;
import com.matrix.yukun.matrix.chat_module.holder.RightTextHolder;
import com.matrix.yukun.matrix.chat_module.holder.RightVoiceHolder;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.MyApplication;
import com.matrix.yukun.matrix.video_module.play.AboutUsActivity;
import com.matrix.yukun.matrix.video_module.play.JokeDetailActivity;
import com.matrix.yukun.matrix.video_module.play.LoginActivity;

import org.greenrobot.eventbus.EventBus;

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
        if(MyApplication.userInfo==null){
            Glide.with(mContext).load(chatListInfo.getBitmap()).placeholder(R.drawable.head_2).into((holder).mImageViewRight);
        }else {
            Glide.with(mContext).load(MyApplication.getUserInfo().getImg()).into((holder).mImageViewRight);
        }
        holder.tvSenond.setText(chatListInfo.getDuration()/1000+"''");
        if(!chatListInfo.isAudioIsPlay()){
            holder.mSeekBar.setVisibility(View.GONE);
            holder.mIvPlay.setImageResource(R.mipmap.icon_video_play);
        }else {
            mVoiceHolder=holder;
            holder.mSeekBar.setVisibility(View.VISIBLE);
            holder.mIvPlay.setImageResource(R.mipmap.icon_video_pause);
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
                    if(mHandler!=null){
                        mHandler.removeMessages(1);
                    }
                }
                @Override
                public void stop() {

                }
            });
        }

        (holder).mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.userInfo!=null) {
                    Intent intent=new Intent(mContext, AboutUsActivity.class);
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
