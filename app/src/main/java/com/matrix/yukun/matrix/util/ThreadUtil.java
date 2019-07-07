package com.matrix.yukun.matrix.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    private static int maximumThreadSize = 2 * (Runtime.getRuntime().availableProcessors() > 0 ? Runtime.getRuntime().availableProcessors() : 2) + 1;
    private static volatile ThreadUtil               instance;
    private static volatile ExecutorService          requester;
    private static volatile ScheduledExecutorService scheduler;
    private static HashMap<Runnable, ScheduledFuture> futures = new HashMap<Runnable, ScheduledFuture>();

    private ThreadUtil() {
        super();
    }

    private static ThreadUtil getInstance() {
        if (instance == null) {
            synchronized (ThreadUtil.class) {
                if (instance == null) {
                    instance = new ThreadUtil();
                }
            }
        }
        return instance;
    }

    private ExecutorService getRequester() {
        if (requester == null) {
            synchronized (ThreadUtil.class) {
                if (requester == null) {
                    requester = Executors.newFixedThreadPool(maximumThreadSize);
                }
            }
        }
        return requester;
    }

    private ScheduledExecutorService getScheduler() {
        if (scheduler == null) {
            synchronized (ThreadUtil.class) {
                if (scheduler == null) {
                    scheduler = Executors.newScheduledThreadPool((maximumThreadSize / 2) + 1);
                }
            }
        }
        return scheduler;
    }

    /**
     * 请求执行，适用于耗时的操作，线程会先进入队列
     *
     * @param runnable
     */
    public static void request(Runnable runnable) {
        getInstance().getRequester().execute(runnable);
    }

    /**
     * 定时任务，适用于定时的操作
     *
     * @param runnable
     */
    public static void schedule(Runnable runnable, long delay) {
        ScheduledFuture<?> future = getInstance().getScheduler().schedule(runnable, delay, TimeUnit.MILLISECONDS);
        futures.put(runnable, future);
    }

    /**
     * 定时任务，适用于定时的操作
     *
     * @param runnable
     */
    public static void schedule(Runnable runnable, long delay, long period) {
        getInstance().getScheduler().scheduleWithFixedDelay(runnable, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 取消定时任务，适用于定时的操作
     *
     * @param runnable
     */
    public static void cancelSchedule(Runnable runnable) {
        if (futures.containsKey(runnable)) {
            ScheduledFuture<?> future = futures.remove(runnable);
            future.cancel(true);
        }
    }

    /**
     * 释放所有定时任务
     */
    public static void releaseSchedules() {
        Iterator<Map.Entry<Runnable, ScheduledFuture>> it = futures.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Runnable, ScheduledFuture> r = it.next();
            ScheduledFuture<?> future = r.getValue();
            future.cancel(true);
        }
        futures.clear();
    }
}
