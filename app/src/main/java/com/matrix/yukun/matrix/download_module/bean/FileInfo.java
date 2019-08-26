package com.matrix.yukun.matrix.download_module.bean;

import org.litepal.crud.DataSupport;

import java.io.File;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class FileInfo extends DataSupport {
    public File file;
    public String fileName;
    public String filePath;
    public long size;
    public String url;
    public boolean pause;
    public long progress;
    public String imageUrl;

    public FileInfo(){

    }
    public FileInfo(File file) {
        this.file = file;
        fileName=file.getName();
        size=file.length();
        filePath=file.getPath();
    }

    public String getIMageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }
}
