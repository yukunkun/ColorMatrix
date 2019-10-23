package com.matrix.yukun.matrix.download_module.service;
import android.text.TextUtils;
import android.util.Log;

import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.download_module.bean.DownLoadError;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;
import com.matrix.yukun.matrix.util.log.LogUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class DownLoadServiceImpl implements DownLoadManager {
    static DownLoadServiceImpl mDownLoadService;
    private static String TAG="DownLoadServiceImpl";
    private List<FileInfo> mDownloadQuen =new ArrayList<>();
    List<DownLoadListener> mDownLoadListeners=new ArrayList<>();

    public static DownLoadServiceImpl getInstance(){
        LogUtil.i(TAG,"start");
        if(mDownLoadService==null){
            mDownLoadService=new DownLoadServiceImpl();
        }
        return mDownLoadService;
    }

    //继续下载
    public DownLoadServiceImpl(){
        List<FileInfo> all = DataSupport.findAll(FileInfo.class);
        if(all.size()>0){
            Collections.reverse(all);
            mDownloadQuen.addAll(all);
            download(mDownloadQuen.get(0));
        }
    }

    public void addListener(DownLoadListener downLoadListener){
        if(!mDownLoadListeners.contains(downLoadListener)){
            mDownLoadListeners.add(downLoadListener);
        }
    }

    public void removeListener(DownLoadListener downLoadListener){
        if(mDownLoadListeners.contains(downLoadListener)){
            mDownLoadListeners.remove(downLoadListener);
        }
    }

    @Override
    public List<FileInfo> getDownQuene() {
        return mDownloadQuen;
    }


    @Override
    public void addDownload(String url,String imageUrl) {
        LogUtil.i(TAG,"addDownload");
        if(TextUtils.isEmpty(url)){
            for (int i = 0; i < mDownLoadListeners.size(); i++) {
                mDownLoadListeners.get(i).onFail(new DownLoadError(0,"url为空"));
            }
            return;
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.url = url;
        if(!TextUtils.isEmpty(imageUrl)){
            fileInfo.imageUrl=imageUrl;
        }
        fileInfo.fileName="yk_"+System.currentTimeMillis()+".mp4";
        fileInfo.filePath= AppConstant.VIDEOPATH;
        mDownloadQuen.add(fileInfo);
        //存数据库
        fileInfo.save();
        if(mDownloadQuen.size()>1){
            return;
        }
        download(mDownloadQuen.get(0));
    }

    @Override
    public void addDownload(String url) {
        addDownload(url,null);
    }

    private void download(final FileInfo fileInfo){
        //子线程
        String name = Thread.currentThread().getName();
        if(!name.equals("main")){
            downloadThread(fileInfo);
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downloadThread(fileInfo);
                }
            }).start();
        }
    }

    private void downloadThread(final FileInfo fileInfo){
        DownLoadUtils.getInstance().download(fileInfo, new DownLoadUtils.OnDownloadListener() {
            @Override
            public void onStartDownload() {
                for (int i = 0; i < mDownLoadListeners.size(); i++) {
                    mDownLoadListeners.get(i).onStart(mDownloadQuen.get(0));
                }
            }

            @Override
            public void onDownloading(long processed, long total) {
                for (int i = 0; i < mDownLoadListeners.size(); i++) {
                    mDownLoadListeners.get(i).onDownLoad(mDownloadQuen.get(0),total,processed);
                }
            }

            @Override
            public void onDownloadPause(long processed, long total) {
                for (int i = 0; i < mDownLoadListeners.size(); i++) {
                    mDownLoadListeners.get(i).onDownLoadPause(mDownloadQuen.get(0));
                }
            }

            @Override
            public void onDownloadCancel(File file) {
                Log.i("===========onCancel",file.getPath());
            }

            @Override
            public void onDownloadSuccess(File file) {
                for (int i = 0; i < mDownLoadListeners.size(); i++) {
                    mDownLoadListeners.get(i).onComplete(mDownloadQuen.get(0));
                }
                if(mDownloadQuen.size()>0){
                    mDownloadQuen.remove(0);
                    DataSupport.deleteAll(FileInfo.class,"url = ?",fileInfo.url);
                }
                if(mDownloadQuen.size()>0){
                    download(mDownloadQuen.get(0));
                }
//                Log.i("===========onSuccess",file.getPath());
            }

            @Override
            public void onDownloadFailed(Exception e) {
                for (int i = 0; i < mDownLoadListeners.size(); i++) {
                    mDownLoadListeners.get(i).onFail(new DownLoadError(1001,"下载失败"));
                }
                if(mDownloadQuen.size()>0){
                    mDownloadQuen.remove(0);
                    DataSupport.deleteAll(FileInfo.class,"url = ?",fileInfo.url);
                }
                if(mDownloadQuen.size()>0){
                    download(mDownloadQuen.get(0));
                }
            }
        });
    }
    @Override
    public void removeAllDownload() {
        mDownloadQuen.clear();
    }

    @Override
    public void pauseDownload(String url) {

    }

    @Override
    public void cancelDownload(String url) {

    }

    @Override
    public void reStartDownload(String url) {

    }


}
