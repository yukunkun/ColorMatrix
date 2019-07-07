package com.matrix.yukun.matrix.chat_module.fragment.voice;

/**
 * author: kun .
 * date:   On 2019/6/10
 */
public interface MediaRecordListener {
    public void start(String path);
    public void recordStop(String path,long s);
    public void recordFail();
}
