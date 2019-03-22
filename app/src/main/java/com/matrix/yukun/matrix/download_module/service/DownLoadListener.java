package com.matrix.yukun.matrix.download_module.service;

import com.matrix.yukun.matrix.download_module.bean.DownLoadError;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public interface DownLoadListener {

    /**
     *
     * @param fileInfo
     */
    void onStart(FileInfo fileInfo);

    /**
     *
     * @param
     */
    void onFail(DownLoadError error);

    /**
     *
     * @param fileInfo
     * @param totalSize
     * @param progress
     */
    void onDownLoad(FileInfo fileInfo, long totalSize, long progress);

    /**
     *
     * @param fileInfo
     */
    void onComplete(FileInfo fileInfo);

    /**
     *
     * @param fileInfo
     */
    void onDownLoadCancel(FileInfo fileInfo);

    /**
     *
     * @param fileInfo
     */
    void onDownLoadPause(FileInfo fileInfo);
}
