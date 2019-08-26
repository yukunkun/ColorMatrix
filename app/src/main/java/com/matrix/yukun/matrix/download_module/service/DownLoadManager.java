package com.matrix.yukun.matrix.download_module.service;

import com.matrix.yukun.matrix.download_module.bean.FileInfo;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public  interface DownLoadManager {

    void addDownload(String url, String imageUrl);

    void addDownload(String url);

    void pauseDownload(String url);

    void cancelDownload(String url);

    void reStartDownload(String url);

    List<FileInfo> getDownQuene();

    void removeAllDownload();
}
