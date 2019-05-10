package com.matrix.yukun.matrix.chat_module.inputListener;

import java.io.File;

/**
 * author: kun .
 * date:   On 2019/5/10
 */
public interface VoiceRecordListener {
    /**
     * 开始语音录制
     */
    public void onVoiceRecordStart();

    /**
     * 停止语音录制
     *
     * @param voice
     * @param duration
     */
    public void onVoiceRecordStop(File voice, int duration);

    /**
     * 丢弃语音录制
     */
    public void onDiscardVoiceRecord();

    /**
     * 摄像头切换
     *
     * @param volume
     */
    public void onVoiceVolumeChange(int volume);

    /**
     * 语音录制错误
     */
//    public void onVoiceRecordFailed(CubeError error);
}
