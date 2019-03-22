package com.matrix.yukun.matrix.download_module.bean;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class DownLoadError {
    int code;
    String errorMsg;

    public DownLoadError(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
