package com.matrix.yukun.matrix.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志操作器。
 */
public abstract class BaseLogHandle {

    protected static String TAG = "DEFAULT_TAG";

    protected final StringBuilder buffer = new StringBuilder();

    private SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.UK);

    private Date date = new Date();

    /**
     * 获得日志句柄名称（默认类名）
     *
     * @return 返回日志句柄名称。
     */
    public String getLogHandleName() {
        return this.getClass().getName();
    }

    /**
     * 日志打印
     *
     * @param level      日志级别
     * @param tag        日志标签。
     * @param message    日志内容。
     * @param stackTrace 堆栈信息
     */
    public abstract void log(LogLevel level, String tag, String message, StackTraceElement[] stackTrace);

    /**
     * 获取tag
     *
     * @return
     */
    public String getTag() {
        return TAG;
    }

    /**
     * 设置tag
     *
     * @param tag
     */
    public void setTag(String tag) {
        TAG = tag;
    }

    /**
     * 获取tag
     *
     * @return
     */
    public String getDateTime() {
        date.setTime(System.currentTimeMillis());
        return timeFormat.format(date);
    }

    /**
     * 获取堆栈信息
     *
     * @param stackTrace
     *
     * @return
     */
    public String getStackTrace(StackTraceElement stackTrace) {
        if (stackTrace == null) {
            return "";
        }

        String format = "[(%s:%d)# %s -> %s]";
        String fileName = stackTrace.getFileName();
        int methodLine = stackTrace.getLineNumber();
        String methodName = stackTrace.getMethodName();
        String currentThread = Thread.currentThread().getName();
        //String className = fileName.substring(0, fileName.lastIndexOf("."));
        return String.format(Locale.CHINESE, format, fileName, methodLine, methodName, currentThread);
    }
}
