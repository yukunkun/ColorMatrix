package com.matrix.yukun.matrix.util.log;

/**
 * 记录时间的model，用于方法耗时打印工具
 *
 * @author LiuFeng
 * @date 2018-8-30
 */
public class RecordTime {
    public String message;
    public long   time;

    @Override
    public String toString() {
        return "RecordTime{" + "message='" + message + '\'' + ", time=" + time + '}';
    }
}
