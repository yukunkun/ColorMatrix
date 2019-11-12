package com.matrix.yukun.matrix.chat_module.fragment.voice;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.util.log.LogUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 语音播放管理类
 *
 * @author PengZhenjin
 * @date 2016-12-6
 */
public class PlayerManager {

    /**
     * 外放模式
     */
    public static final int MODE_SPEAKER = 0;

    /**
     * 耳机模式
     */
    public static final int MODE_HEADSET = 1;

    /**
     * 听筒模式
     */
    public static final int MODE_EARPIECE = 2;

    /**
     * 蓝牙模式
     */
    public static final int MODE_BLE = 3;

    private static PlayerManager instance;

    private AudioManager audioManager;
    private MediaPlayer  mediaPlayer;
    private PlayCallback callback;
    private Context      context;

    private boolean isPause = false;
    private String filePath;

    private int savedAudioMode;
    private int currentMode = MODE_SPEAKER;
    private Timer mTimer=new Timer();
    private int                                     currentTime               = 0;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = null;

    public static PlayerManager getInstance() {
        if (instance == null) {
            synchronized (PlayerManager.class) {
                instance = new PlayerManager();
            }
        }
        return instance;
    }

    private PlayerManager() {
        this.context = MyApp.myApp;
        this.initMediaPlayer();
        this.audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * 初始化音频管理器
     */
    private void initAudioManager() {
        this.savedAudioMode = this.audioManager.getMode();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }
        else {
            this.audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
        if (!audioManager.isWiredHeadsetOn()) {
            this.audioManager.setSpeakerphoneOn(true);            // 默认为扬声器播放
        }
        else {
            changeToHeadsetMode();
        }
        //Build.VERSION.SDK_INT表示当前SDK的版本，Build.VERSION_CODES.ECLAIR_MR1为SDK 7版本 ，
        //因为AudioManager.OnAudioFocusChangeListener在SDK8版本开始才有。
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1) {
            mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //失去焦点之后的操作
                        if (isPlaying()) {
                            pause();
                        }
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //获得焦点之后的操作
                        if (isPause()) {
                            resume();
                        }
                    }
                }
            };
        }
    }

    private void close() {
        if (this.audioManager != null) {
            abandonAudioFocus();
            this.audioManager.setMode(savedAudioMode);
            this.audioManager.abandonAudioFocus(null);
        }
    }

    /**
     * 开始播放
     *
     * @param path     音频文件路径
     * @param callback 播放回调函数
     */
    public void play(String path, final PlayCallback callback) {
        this.initAudioManager();
        this.filePath = path;
        this.callback = callback;
        try {
            mTimer.cancel();
            this.mediaPlayer.reset();
            this.mediaPlayer.setDataSource(this.context, Uri.parse(this.filePath));
            this.mediaPlayer.prepareAsync();
            this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    callback.onPrepared(mp);
                    mediaPlayer.start();
                    requestAudioFocus();
                }
            });
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    resetPlayMode();
                    abandonAudioFocus();
                    callback.onComplete();
                    mTimer.cancel();
                }
            });

//            mTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    callback.progress(mediaPlayer.getDuration(),mediaPlayer.getCurrentPosition());
//                }
//            },0,1000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 停止播放
     */
    public void stop() {
        this.close();
        if (this.isPlaying()) {
            try {
                this.mediaPlayer.stop();
                this.callback.stop();
                mTimer.cancel();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否正在播放
     *
     * @return 正在播放返回true, 否则返回false
     */
    public boolean isPlaying() {
        return this.mediaPlayer != null && this.mediaPlayer.isPlaying();
    }

    /**
     * 是否暂停
     *
     * @return
     */
    public boolean isPause() {
        return this.isPause;
    }

    /**
     * 暂停
     */
    public void pause() {
        if (this.isPlaying()) {
            this.isPause = true;
            this.mediaPlayer.pause();
        }
    }

    /**
     * 恢复
     */
    public void resume() {
        if (this.isPause) {
            this.isPause = false;
            this.mediaPlayer.start();
        }
    }

    /**
     * 切换到扬声器模式
     */
    public void changeToSpeakerMode() {
        this.currentMode = MODE_SPEAKER;
        this.audioManager.setSpeakerphoneOn(true);
    }

    /**
     * 切换到听筒模式
     */
    public void changeToEarpieceMode() {
        this.currentMode = MODE_EARPIECE;
        this.audioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, this.audioManager.getStreamMaxVolume(AudioManager.MODE_IN_COMMUNICATION), AudioManager.FX_KEY_CLICK);
        }
        else {
            this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, this.audioManager.getStreamMaxVolume(AudioManager.MODE_IN_CALL), AudioManager.FX_KEY_CLICK);
        }
    }

    /**
     * 切换到耳机模式
     */
    public void changeToHeadsetMode() {
        this.currentMode = MODE_HEADSET;

        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(false);
    }

    ///**
    // * 切换到外放
    // */
    //public void changeToSpeaker(){
    //    //注意此处，蓝牙未断开时使用MODE_IN_COMMUNICATION而不是MODE_NORMAL
    //    audioManager.setMode(bluetoothIsConnected ? AudioManager.MODE_IN_COMMUNICATION : AudioManager.MODE_NORMAL);
    //    audioManager.stopBluetoothSco();
    //    audioManager.setBluetoothScoOn(false);
    //    audioManager.setSpeakerphoneOn(true);
    //}
    /**
     * 切换到蓝牙音箱
     */
    public void changeToHeadsetBox(){
        this.currentMode = MODE_BLE;
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.startBluetoothSco();
        audioManager.setBluetoothScoOn(true);
        audioManager.setSpeakerphoneOn(false);
    }
    /************************************************************/
    //注意：以下两个方法还未验证
    /************************************************************/
    /**
     * 切换到耳机模式
     */
    public void changeToHeadset(){
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(false);
    }
    /**
     * 切换到听筒
     */
    public void changeToReceiver() {
        audioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }
        else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    /**
     * 获取当前播放模式
     *
     * @return
     */
    public int getCurrentMode() {
        return this.currentMode;
    }

    /**
     * 获取当前播放文件路径
     *
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 重置播放模式
     */
    public void resetPlayMode() {
        if (this.audioManager.isWiredHeadsetOn()) {
            this.changeToHeadsetMode();
        }
        else {
            this.changeToSpeakerMode();
        }
    }

    /**
     * 调大音量
     */
    public void raiseVolume() {
        int currentVolume = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume < this.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
    }

    /**
     * 调小音量
     */
    public void lowerVolume() {
        int currentVolume = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume > 0) {
            this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
    }

    /**
     * 播放回调接口
     */
    public interface PlayCallback {

        /**
         * 准备完毕
         */
        void onPrepared(MediaPlayer mediaPlayer);

        /**
         * 播放完成
         */
        void onComplete();
        /**
         * 停止播放
         */
        void stop();
        /**
         * 停止播放
         */
        void progress(int size,int progress);
    }

    /**
     * 请求音频焦点
     */
    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return;
        }
        if (this.audioManager == null) {
            this.audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        }
        if (this.audioManager != null) {
            LogUtil.i("Request audio focus");
            int ret = this.audioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (ret != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                LogUtil.i("request audio focus fail. " + ret);
            }
        }
    }

    /**
     * 放弃焦点
     */
    private void abandonAudioFocus() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return;
        }
        if (this.audioManager != null) {
            LogUtil.i("Abandon audio focus");
            this.audioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }
}
