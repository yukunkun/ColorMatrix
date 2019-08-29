package com.matrix.yukun.matrix.download_module.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.DownLoadActivity;
import com.matrix.yukun.matrix.download_module.bean.DownLoadError;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;
import com.matrix.yukun.matrix.util.FileUtil;

/**
 * author: kun .
 * date:   On 2019/1/28
 */
public class DownloadNotificationService extends Service implements DownLoadListener {

    private static DownloadNotificationService instance;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private PendingIntent mPendingIntent;
    private  boolean isAlive;
    String id = "my_channel_01";
    String name="渠道名字";
    FileInfo mFileInfo=new FileInfo();

    public static DownloadNotificationService getInstance(){
        if(instance==null){
            instance=new DownloadNotificationService();
        }
        return instance;
    }

    public void start(Context context){
        if(!isAlive){
            Intent intent = new Intent(context, DownloadNotificationService.class);
            context.startService(intent);
        }
        isAlive=true;
    }

    public void startDownload(final String url, final String  imageUrl){
        DownLoadEngine.getInstance().getDownManager().addDownload(url,imageUrl);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownLoadEngine.getInstance().getDownManager().addListener(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);
            mNotification = new Notification.Builder(this)
                    .setChannelId(id)
                    .setSmallIcon(R.mipmap.snail_image).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.snail_image)
                    .setOngoing(true)
                    .setAutoCancel(true);
            mNotification = notificationBuilder.build();
        }

        Intent completingIntent = new Intent();
        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        completingIntent.setClass(getApplicationContext(), DownLoadActivity.class);
        // 创建Notifcation对象，设置图标，提示文字,策略
        mPendingIntent = PendingIntent.getActivity(DownloadNotificationService.this, 0,  completingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        mPendingIntent = PendingIntent.getActivity(DownloadNotificationService.this, R.string.app_name, completingIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.icon = R.mipmap.snail_image;
//        mNotification.tickerText = "开始下载";
        mNotification.contentIntent = mPendingIntent;
        mNotification.contentView = new RemoteViews(getPackageName(), R.layout.app_download_layout);
        mNotification.contentView.setProgressBar(R.id.progressbar, 100, 1, false);
        mNotification.contentView.setTextViewText(R.id.tv_pro, "正在下载...");
        mNotification.contentView.setBitmap(R.id.iv_icon,"setImageBitmap", BitmapFactory.decodeResource(getResources(), R.mipmap.snail_image));
        mNotification.contentView.setTextViewText(R.id.tv_product, "当前进度：0%");
        mNotificationManager.notify(0, mNotification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isAlive=false;
        DownLoadEngine.getInstance().getDownManager().removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onStart(FileInfo fileInfo) {

    }

    @Override
    public void onFail(DownLoadError error) {
        Log.i("=============","onFail "+error.toString());
    }

    @Override
    public void onDownLoad(FileInfo fileInfo, long totalSize, long progress) {
        Log.i("=============","progress"+progress);
        if(TextUtils.isEmpty(mFileInfo.imageUrl)||!mFileInfo.url.equals(fileInfo.url)){
            mFileInfo=null;
            mFileInfo=fileInfo;
            mNotification.contentView.setTextViewText(R.id.tv_pro, fileInfo.fileName);
//            Glide.with(this).load(fileInfo.imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    mNotification.contentView.setImageViewBitmap(R.id.iv_icon,resource);
//                }
//            });
        }
        mNotification.contentView.setProgressBar(R.id.progressbar, (int)totalSize, (int) (progress), false);
        mNotification.contentView.setTextViewText(R.id.tv_product, "当前进度："+(int)((float)progress/totalSize*100)+"% "+ FileUtil.formatFileSize(fileInfo.size));
        mNotificationManager.notify(0, mNotification);
    }

    @Override
    public void onComplete(FileInfo fileInfo) {
        mNotificationManager.cancel(0);
    }

    @Override
    public void onDownLoadCancel(FileInfo fileInfo) {

    }

    @Override
    public void onDownLoadPause(FileInfo fileInfo) {

    }
}
