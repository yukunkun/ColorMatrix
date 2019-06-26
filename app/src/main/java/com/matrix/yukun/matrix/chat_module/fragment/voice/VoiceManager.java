package com.matrix.yukun.matrix.chat_module.fragment.voice;

import android.media.MediaRecorder;

import com.matrix.yukun.matrix.chat_module.inputListener.VoiceRecordListener;
import com.matrix.yukun.matrix.constant.AppConstant;
import java.io.File;
import java.io.IOException;

/**
 * author: kun .
 * date:   On 2019/6/10
 */
public class VoiceManager {
    public static VoiceManager mVoiceManager;
    private MediaRecorder mMediaRecorder;
    private File mRecorderFile;
    private long mStartRecorderTime;
    private long mStopRecorderTime;
    private MediaRecordListener mMediaRecordListener;
    private boolean isRecord;

    public  void setMediaRecordListener(MediaRecordListener mediaRecordListener) {
        mMediaRecordListener = mediaRecordListener;
    }

    public static VoiceManager getInstance(){
        if(mVoiceManager==null)
            synchronized (""){
                if(mVoiceManager==null){
                    mVoiceManager=new VoiceManager();
                }
            }
        return mVoiceManager;
    }

    public VoiceManager() {

    }

    public void startRecord(final String name){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                start(name);
            }
        });
        thread.start();
    }

    private void start(String name){
        if(!isRecord){
            isRecord=true;
            mMediaRecorder = new MediaRecorder();
            //创建录音文件
            mRecorderFile = new File(AppConstant.VOICEPATH,name);
            if (!mRecorderFile.getParentFile().exists()) {
                mRecorderFile.getParentFile().mkdirs();
            }
            //从麦克风采集
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //保存文件为MP4格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //所有android系统都支持的适中采样的频率
            mMediaRecorder.setAudioSamplingRate(44100);
            //通用的AAC编码格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置音质频率
            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置文件录音的位置
            mMediaRecorder.setOutputFile(mRecorderFile.getAbsolutePath());
            //开始录音
            try {
                mMediaRecordListener.start(mRecorderFile.getAbsolutePath());
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                mStartRecorderTime = System.currentTimeMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            mMediaRecordListener.recordFail();
            isRecord=false;
        }
    }

    public void stop(){
        try {
            mMediaRecorder.stop();
            mStopRecorderTime = System.currentTimeMillis();
            final int second = (int) (mStopRecorderTime - mStartRecorderTime) / 1000;
            //按住时间小于3秒钟，算作录取失败，不进行发送
            if (second < 3){
                mMediaRecordListener.recordFail();
                isRecord=false;
                return;
            }
            mMediaRecordListener.recordStop(mRecorderFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void release(){
        if(mMediaRecorder!=null){
            mMediaRecorder.release();
            mMediaRecorder=null;
        }
    }
}
