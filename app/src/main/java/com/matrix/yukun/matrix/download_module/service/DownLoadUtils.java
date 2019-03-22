package com.matrix.yukun.matrix.download_module.service;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.matrix.yukun.matrix.download_module.bean.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author: kun .
 * date:   On 2019/1/22
 */
public class DownLoadUtils {

    OkHttpClient okHttpClient;
    private Map<String, Call> callMap      = new HashMap<>();
    private Map<String, Boolean> onPauseMap   = new HashMap<>();
    private Map<String, Boolean> onCancelMap  = new HashMap<>();
    private int                  mRefreshTime = 50;
    private long lastRefreshTime;  //最后一次刷新的时间

    public static DownLoadUtils getInstance() {
        return DownloadHolder.INSTANCE;
    }

    private DownLoadUtils() {
        okHttpClient = new OkHttpClient();
    }

    private static class DownloadHolder {
        private static final DownLoadUtils INSTANCE = new DownLoadUtils();
    }
    /**
     * 文件下载，支持断点下载
     *
     * @param fileInfo
     * @param listener
     */
    public void download(final FileInfo fileInfo, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(fileInfo.url).header("Range", "bytes=" + fileInfo.progress + "-").build();
        Call newCall = okHttpClient.newCall(request);
        if (!callMap.containsKey(fileInfo.url)) {
            callMap.put(fileInfo.url, newCall);
        }
        if (newCall.isExecuted()) {
            return;
        }
        // 开始下载
        UIHandler.run(new Runnable() {
            @Override
            public void run() {
                listener.onStartDownload();
            }
        });
        // 初始化
        onPauseMap.put(fileInfo.url, false);
        onCancelMap.put(fileInfo.url, false);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                // 下载失败监听回调
                UIHandler.run(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDownloadFailed(e);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    return;
                }

                InputStream in = responseBody.byteStream();
                final long total = responseBody.contentLength();
                long sum = fileInfo.progress;
                fileInfo.size = total;
                fileInfo.pause = false;
                RandomAccessFile randomAccessFile = null;
                try {
                    final File tempFile = getTempFile(fileInfo.fileName, fileInfo.filePath, false);
                    randomAccessFile = new RandomAccessFile(tempFile, "rw");
                    randomAccessFile.seek(sum);
                    byte[] buffer = new byte[1024];

                    int len;
                    long curTime;
                    boolean isFreshSpace;
                    boolean isFinish = false;
                    while ((len = in.read(buffer)) != -1) {
                        // 写入文件
                        randomAccessFile.write(buffer, 0, len);
                        sum += len;
                        fileInfo.progress = sum;

                        // 暂停
                        if (onPauseMap.get(fileInfo.url)) {
                            fileInfo.pause = true;
                            final long finalSum = sum;
                            UIHandler.run(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onDownloadPause(finalSum, total);
                                }
                            });
                            break;
                        }

                        // 取消
                        if (onCancelMap.get(fileInfo.url)) {
                            UIHandler.run(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onDownloadCancel(tempFile);
                                }
                            });
                            break;
                        }

                        // 进度
                        curTime = SystemClock.elapsedRealtime();
                        isFreshSpace = curTime - lastRefreshTime >= mRefreshTime;
                        isFinish = sum == total;
                        if (isFreshSpace || isFinish) {
                            lastRefreshTime = curTime;
                            final long finalSum1 = sum;
                            UIHandler.run(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onDownloading(finalSum1, total);
                                }
                            });
                        }

                        // 完成
                        if (isFinish) {
                            callMap.remove(fileInfo.url);
                            fileInfo.pause = false;
                            UIHandler.run(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onDownloadSuccess(tempFile);
                                }
                            });
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("--download---" , e.getMessage());
                } finally {
                    try {
                        in.close();
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private File getTempFile(String name, String path, boolean isNew) throws IOException {
        if (name != null) {
            File tempFile = new File(path, name);
            if (!tempFile.exists()) {
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdirs();
                }
                tempFile.createNewFile();
                tempFile.delete();
            }
            else if (isNew) {
                tempFile.delete();
                tempFile.createNewFile();
            }

            return tempFile;
        }
        else {
            return null;
        }
    }

    /**
     * 暂停下载
     *
     * @param url
     */
    public synchronized void pause(final String url) {
        if (callMap.containsKey(url)) {
            onPauseMap.put(url, true);
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callMap.get(url).cancel();
                    callMap.remove(url);
                    onPauseMap.remove(url);
                }
            }, 50);
        }
    }

    /**
     * 取消下载
     *
     * @param url
     */
    public synchronized void cancel(final String url) {
        if (callMap.containsKey(url)) {
            onCancelMap.put(url, true);
            UIHandler.getInstance().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callMap.get(url).cancel();
                    callMap.remove(url);
                    onCancelMap.remove(url);
                }
            }, 50);
        }
    }

    public interface OnDownloadListener {
        /**
         * 准备开始下载
         */
        void onStartDownload();

        /**
         * 下载进度
         *
         * @param processed
         * @param total
         */
        void onDownloading(long processed, long total);

        /**
         * 暂停下载
         *
         * @param processed
         * @param total
         */
        void onDownloadPause(long processed, long total);

        /**
         * 取消下载
         *
         * @param file
         */
        void onDownloadCancel(File file);

        /**
         * 下载成功后的文件
         *
         * @param file 下载成功后的文件
         */
        void onDownloadSuccess(File file);

        /**
         * 下载异常信息
         *
         * @param e 下载异常信息
         */
        void onDownloadFailed(Exception e);
    }

}
