package com.matrix.yukun.matrix.download_module.service;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class DownLoadEngine {
    DownLoadServiceImpl mDownLoadServiceImpl;
    static DownLoadEngine mDownLoadEngine;
    public static DownLoadEngine getInstance(){
        if(mDownLoadEngine==null){
            mDownLoadEngine=new DownLoadEngine();
        }
        return mDownLoadEngine;
    }

    public DownLoadServiceImpl getDownManager(){
       return mDownLoadServiceImpl;
    }

    public DownLoadServiceImpl startDownLoadServiceImpl(){
        if(mDownLoadServiceImpl==null){
            mDownLoadServiceImpl=DownLoadServiceImpl.getInstance();
        }
        return mDownLoadServiceImpl;
    }
}
