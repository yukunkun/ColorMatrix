package com.matrix.yukun.matrix.util.log;

/**
 * 日志等级
 *
 * 提示：枚举类型中参数与系统log日志级别的参数一致
 * 从VERBOSE(2)——> ASSERT(7)保持递增
 */
public enum LogLevel {

    /**
     * VERBOSE 等级。
     */
    VERBOSE(2),

    /**
     * Debug 等级。
     */
    DEBUG(3),

    /**
     * Info 等级。
     */
    INFO(4),

    /**
     * Warn 等级。
     */
    WARN(5),

    /**
     * Error 等级。
     */
    ERROR(6),

    /**
     * ASSERT 等级。
     */
    ASSERT(7);

    private int code;

    LogLevel(int code) {
        this.code = code;
    }

    /**
     * 解析相应等级
     *
     * @param code
     *
     * @return
     */
    public static LogLevel parse(int code) {
        for (LogLevel level : values()) {
            if (level.getCode() == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("LogLevel code is illegal.");
    }

    public int getCode() {
        return this.code;
    }
}
