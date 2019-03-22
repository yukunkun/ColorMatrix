package com.matrix.yukun.matrix.util.log;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiskLogHandle extends BaseLogHandle {

    private final static String LOG_FILE_NAME = "yyyy-MM-dd-HH";

    private static final int MAX_BYTES = 500 * 1024; // 500K averages to a 4000 lines per file

    private Handler handler;
    private Context mContext;
    private String  mFolderPath;

    public DiskLogHandle(Context context, String folderPath) {
        this.mContext = context;
        this.mFolderPath = folderPath;
        HandlerThread thread = new HandlerThread("DiskLogHandle");
        thread.start();
        handler = new WriteHandler(thread.getLooper());
    }

    @Override
    public void log(LogLevel level, String tag, String message, StackTraceElement[] stackTrace) {
        buffer.append(TAG);
        buffer.append(": ");
        buffer.append("[");
        buffer.append(level.name());
        buffer.append("] ");
        buffer.append(getDateTime());
        buffer.append(" ");
        buffer.append(getStackTrace(stackTrace[4]));
        buffer.append(" ");
        buffer.append(tag);
        buffer.append(" ");
        buffer.append(message);
        buffer.append("\n====================================分割线==========================================");

        handler.sendMessage(handler.obtainMessage(level.getCode(), buffer.toString()));

        buffer.delete(0, buffer.length());
    }

    /**
     * 写入日志文件处理者
     */
    class WriteHandler extends Handler {

        WriteHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // 判断写入权限
            if (checkPermission(mContext, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                String content = (String) msg.obj;
                LogLevel logLevel = LogLevel.parse(msg.what);
                // 通过时间戳生成文件名
                String fileName = String.valueOf(DateFormat.format(LOG_FILE_NAME, System.currentTimeMillis()));
                File logFile = getErrorLogFile(mFolderPath, fileName, logLevel);
                writeLogToFile(logFile, content);
            }
        }
    }

    /**
     * 权限检测
     *
     * @param permission
     *
     * @return
     */
    public boolean checkPermission(Context context, String permission) {
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 获取日志文件--文件大小超过指定大小时新建一个文件
     *
     * @param folderName
     * @param fileName
     *
     * @return
     */
    private static File getLogFile(String folderName, String fileName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        int newFileCount = 0;
        File newFile;
        File existingFile = null;

        newFile = new File(folder, String.format("%s.txt", fileName));
        while (newFile.exists()) {
            existingFile = newFile;
            newFileCount++;
            newFile = new File(folder, String.format("%s(%s).txt", fileName, newFileCount));
        }

        if (existingFile != null) {
            if (existingFile.length() >= MAX_BYTES) {
                return newFile;
            }
            return existingFile;
        }

        return newFile;
    }

    /**
     * 获取包含错误文件名的日志文件--文件大小超过指定大小时新建一个文件
     *
     * @param folderName
     * @param fileName
     * @param level
     *
     * @return
     */
    private static File getErrorLogFile(String folderName, String fileName, LogLevel level) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        int newFileCount = 0;
        File newFile;
        File existingFile = null;

        String normalFormat = "%s%s.txt";
        String warnFormat = LogLevel.WARN.name() + "_%s%s.txt";
        String errorFormat = LogLevel.ERROR.name() + "_%s%s.txt";
        while (true) {
            // 后缀名
            String postfix = newFileCount == 0 ? "" : "(" + newFileCount + ")";

            // 普通文件
            File normalFile = new File(folder, String.format(normalFormat, fileName, postfix));
            if (normalFile.exists()) {
                existingFile = normalFile;
                newFileCount++;
                continue;
            }

            // 错误文件
            File errorFile = new File(folder, String.format(errorFormat, fileName, postfix));
            if (errorFile.exists()) {
                existingFile = errorFile;
                newFileCount++;
                continue;
            }

            // 警告文件
            File warnFile = new File(folder, String.format(warnFormat, fileName, postfix));
            if (warnFile.exists()) {
                existingFile = warnFile;
                newFileCount++;
                continue;
            }

            // 新文件级别
            switch (level) {
                case ERROR:
                    newFile = errorFile;
                    break;

                case WARN:
                    newFile = warnFile;
                    break;

                default:
                    newFile = normalFile;
                    break;
            }
            break;
        }

        if (existingFile != null) {
            // 超过指定文件大小则启用新文件
            if (existingFile.length() >= MAX_BYTES) {
                return newFile;
            }

            String name = existingFile.getName();
            boolean containLevel = false;
            for (LogLevel logLevel : LogLevel.values()) {
                // 判断文件前缀日志等级
                if (name.startsWith(logLevel.name())) {
                    containLevel = true;
                    // 当前将打印日志等级大于文件原先等级
                    if (level.getCode() > logLevel.getCode()) {
                        File dest = new File(existingFile.getParent() + File.separator + name.replace(logLevel.name(), level.name()));
                        // 修改文件前缀日志等级
                        if (existingFile.renameTo(dest)) {
                            return dest;
                        }
                    }
                    break;
                }
            }

            // 不包含日志等级文件添加前缀日志等级
            if (!containLevel && level.getCode() >= LogLevel.WARN.getCode()) {
                File dest = new File(existingFile.getParent() + File.separator + level.name() + "_" + name);
                if (existingFile.renameTo(dest)) {
                    return dest;
                }
            }
            return existingFile;
        }

        return newFile;
    }

    /**
     * 写入日志到文件
     *
     * @param logFile
     * @param content
     */
    private static void writeLogToFile(File logFile, String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.write(content);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            Log.e(TAG, "writeLogToFile: ", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    Log.e(TAG, "writeLogToFile: ", e);
                }
            }
        }
    }
}
