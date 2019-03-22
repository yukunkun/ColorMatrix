package com.matrix.yukun.matrix.util.log;

import java.util.ArrayList;

/**
 * 日志管理器。
 */
public final class LogManager {

    /** 日志处理器列表。 */
    private ArrayList<BaseLogHandle> handles = new ArrayList<>();

    /** 当前日志等级。 */
    private LogLevel level = LogLevel.DEBUG;

    private boolean isLoggable = true;

    private static LogManager instance = new LogManager();

    /**
     * 构造函数。
     */
    private LogManager() {
        init();
    }

    /**
     * 初始化默认日志处理
     */
    private void init() {
        BaseLogHandle defaultLogHandle = new DefaultLogHandle();
        this.handles.add(defaultLogHandle);
    }

    /**
     * 获得管理器的单例。
     *
     * @return 返回管理器单例。
     */
    public static LogManager getInstance() {
        return instance;
    }

    /**
     * 设置日志等级。
     *
     * @param level 日志等级 {@link LogLevel} 。
     *
     * @see LogLevel
     */
    public void setLevel(LogLevel level) {
        this.level = level;
    }

    /**
     * 获得日志等级。
     *
     * @return 返回日志等级 {@link LogLevel} 。
     *
     * @see LogLevel
     */
    public LogLevel getLevel() {
        return this.level;
    }

    /**
     * 日志是否可用
     *
     * @return
     */
    public boolean isLoggable() {
        return isLoggable;
    }

    /**
     * 设置日志是否可用
     *
     * @param loggable
     */
    public void setLoggable(boolean loggable) {
        isLoggable = loggable;
    }

    /**
     * 设置日志的tag
     *
     * @param tag
     */
    public void setLogTag(String tag) {
        for (BaseLogHandle handle : this.handles) {
            handle.setTag(tag);
        }
    }

    /**
     * 记录日志。
     *
     * @param level      指定该日志的记录等级。
     * @param tag        指定日志标签。
     * @param message    指定日志内容。
     * @param stackTrace 堆栈信息
     */
    public void log(LogLevel level, String tag, String message, StackTraceElement[] stackTrace) {
        if (!this.isLoggable) {
            // 日志不可用
            return;
        }

        synchronized (this) {
            if (this.level.getCode() > level.getCode()) {
                // 过滤日志等级
                return;
            }

            for (BaseLogHandle handle : this.handles) {
                handle.log(level, tag, message, stackTrace);
            }
        }
    }

    /**
     * 获得指定名称的处理器。
     *
     * @param name 指定处理器名称。
     *
     * @return 返回指定名称的处理器。
     */
    public BaseLogHandle getHandle(String name) {
        synchronized (this) {
            for (BaseLogHandle handle : this.handles) {
                if (handle.getLogHandleName().equals(name)) {
                    return handle;
                }
            }
        }

        return null;
    }

    /**
     * 添加日志内容处理器。
     *
     * @param handle 需添加的日志处理器。
     */
    public void addHandle(BaseLogHandle handle) {
        synchronized (this) {
            if (this.handles.contains(handle)) {
                return;
            }

            for (BaseLogHandle h : this.handles) {
                if (h.getLogHandleName().equals(handle.getLogHandleName())) {
                    return;
                }
            }

            this.handles.add(handle);
        }
    }

    /**
     * 移除日志内容处理器。
     *
     * @param handle 需移除的日志处理器。
     */
    public void removeHandle(BaseLogHandle handle) {
        synchronized (this) {
            this.handles.remove(handle);
        }
    }

    /**
     * 移除所有日志内容处理器。
     */
    public void removeAllHandles() {
        synchronized (this) {
            this.handles.clear();
        }
    }
}
