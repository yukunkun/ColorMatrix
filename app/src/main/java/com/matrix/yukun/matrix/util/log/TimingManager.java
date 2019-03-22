package com.matrix.yukun.matrix.util.log;

import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个实用程序类，用来帮助记录在方法调用中的耗时。
 * 典型的用法是:
 *
 * <pre>
 *     TimingManager.getInstance().addRecord(label, "work");
 *     // ... do some work A ...
 *     TimingManager.getInstance().addRecord(label, "work A");
 *     // ... do some work B ...
 *     TimingManager.getInstance().addRecord(label, "work B");
 *     // ... do some work C ...
 *     TimingManager.getInstance().addRecord(label, "work C");
 *     TimingManager.getInstance().toLogTime(priority, label);
 * </pre>
 *
 * <p>toLog调用会将以下内容添加到日志中:</p>
 *
 * <pre>
 *     D/TAG     ( 3459): methodA: begin
 *     D/TAG     ( 3459): methodA:      9 ms, work A
 *     D/TAG     ( 3459): methodA:      1 ms, work B
 *     D/TAG     ( 3459): methodA:      6 ms, work C
 *     D/TAG     ( 3459): methodA: end, 16 ms
 * </pre>
 *
 * @author LiuFeng
 * @date 2018-8-30
 */
public class TimingManager {
    private static String TAG = "DEFAULT_TAG";

    private boolean isToLog = false; // 调用toLogTime是否打印到控制台

    private final StringBuilder buffer = new StringBuilder();           // 字符串处理

    private Map<String, List<RecordTime>> recordMap = new HashMap<>();  // 记录容器

    private final Object lock = new Object();  // 锁对象

    private static TimingManager instance = new TimingManager();

    /**
     * 单例
     *
     * @return
     */
    public static TimingManager getInstance() {
        return instance;
    }

    /**
     * 添加记录
     *
     * @param label   记录的标签
     * @param message 记录的日志内容。
     */
    public void addRecord(String label, String message) {
        synchronized (lock) {
            // 不包含此标签时，先创建集合
            if (!recordMap.containsKey(label)) {
                recordMap.put(label, new ArrayList<RecordTime>());
            }

            // 记录时间容器
            List<RecordTime> recordTimeList = recordMap.get(label);

            // 添加记录数量控制
            if (recordTimeList.size() > 100) {
                LogUtil.e(new IllegalStateException("添加时间记录数据过多：size=" + recordTimeList.size() + " 此方法是用于帮助记录方法调用中的耗时，应当调用toLogTime方法打印并释放其数据。"));
                return;
            }

            // 存入消息记录
            long now = SystemClock.elapsedRealtime();
            RecordTime recordTime = new RecordTime();
            recordTime.message = message;
            recordTime.time = now;
            recordTimeList.add(recordTime);
        }
    }

    /**
     * 获取记录数据
     *
     * @param label
     *
     * @return
     */
    private String getRecord(String label) {
        synchronized (lock) {
            // 记录容器
            List<RecordTime> recordTimeList = recordMap.get(label);

            if (recordTimeList == null || recordTimeList.isEmpty()) {
                return label + ": no record";
            }

            // 组装数据
            buffer.append("\n");
            buffer.append("label: ");
            buffer.append(label);
            buffer.append(": begin");
            buffer.append("\n");
            long first = recordTimeList.get(0).time;
            long now = first;
            int total = recordTimeList.size();
            long last = recordTimeList.get(total - 1).time;
            int maxSpaceLength = String.valueOf(last - first).length();

            // 取出数据计算差值并拼接
            for (int i = 0; i < total; i++) {
                now = recordTimeList.get(i).time;
                long prev = i == 0 ? first : recordTimeList.get(i - 1).time;
                long spaceTime = now - prev;
                int currentSpaceLength = String.valueOf(spaceTime).length();

                // 拼接
                buffer.append(getBlank(14 + (maxSpaceLength - currentSpaceLength)));
                buffer.append(spaceTime);
                buffer.append(" ms");
                buffer.append("  message: ");
                buffer.append(recordTimeList.get(i).message);
                buffer.append("\n");
            }

            buffer.append(": end, total: ");
            buffer.append(now - first);
            buffer.append(" ms");
            String record = buffer.toString();

            // 清空buffer下次使用
            buffer.delete(0, buffer.length());

            return record;
        }
    }

    public static String getBlank(int spaces) {
        String number = spaces <= 0 ? "" : String.valueOf(spaces);
        return String.format("%" + number + "s", "");
    }
    /**
     * 打印记录时间
     *
     * @param priority
     * @param label
     */
    public String toLogTime(int priority, String label) {
        synchronized (lock) {
            // 调取记录前再记录一次当前时间
            addRecord(label, "to log time");

            // 调取记录
            String record = getRecord(label);

            // 打印到控制台
            if (isToLog) {
                Log.println(priority, TAG, getRecord(label));
            }

            // 调取打印后清除此标记数据
            remove(label);
            return record;
        }
    }

    /**
     * 删除键
     *
     * @param label
     */
    public void remove(String label) {
        synchronized (lock) {
            recordMap.remove(label);
        }
    }

    /**
     * 清空全部数据
     */
    public void clearAll() {
        synchronized (lock) {
            recordMap.clear();
        }
    }
}
