package com.ykk.pluglin_video.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by yukun on 17-8-18.
 */

public class DownLoadUtils {

    public static long download(Context context, String url, boolean isGif){
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
//        ToastUtils.showShortToast("downloading...");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.allowScanningByMediaScanner();
        //设置状态栏中显示Notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置Notification的标题
        request.setTitle("视频文件下载中...");
        //设置Notification的描述
        request.setDescription("this is description !!!");
        //设置下载的目录
//        request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/moral/", System.currentTimeMillis()+".mp4")));
        if(isGif){
            request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MediaCamera/", System.currentTimeMillis()+".gif" );
        }else {
            request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MediaCamera/", System.currentTimeMillis()+".png" );
        }
//        long downloadId = dm.enqueue(request);
        dm.enqueue(request);
        return 0;
    }
}
