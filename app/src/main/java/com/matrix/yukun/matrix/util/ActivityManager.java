package com.matrix.yukun.matrix.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Activity管理器
 *
 * @author LiuFeng
 * @date 2017-11-01
 */
public class ActivityManager {

    private static ActivityManager mInstance = new ActivityManager();

    private Stack<Activity> mActivityStack = new Stack<>();

    private static Handler handler = new Handler();

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        return mInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(final Activity activity) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (null != ActivityManager.this.mActivityStack) {
                    ActivityManager.this.mActivityStack.add(activity);
                }
            }
        });
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {

        return null == ActivityManager.this.mActivityStack || ActivityManager.this.mActivityStack.size() <= 0 ? null : ActivityManager.this.mActivityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (null != ActivityManager.this.mActivityStack) {
                    Activity activity = ActivityManager.this.mActivityStack.lastElement();
                    ActivityManager.this.finishActivity(activity);
                }
            }
        });
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(final Activity activity) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (null != ActivityManager.this.mActivityStack && null != activity) {
                    if (ActivityManager.this.mActivityStack.contains(activity)) {
                        ActivityManager.this.mActivityStack.remove(activity);
                        if (!activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取指定类名的Activity
     */
    public Activity findActivity(Class<? extends Activity>... activityClass) {
        if (null != ActivityManager.this.mActivityStack && !ActivityManager.this.mActivityStack.isEmpty()) {
            for (Activity activity : mActivityStack) {
                if (Arrays.asList(activityClass).contains(activity.getClass())) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(final Class<? extends Activity>... activityClass) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (null != ActivityManager.this.mActivityStack && !ActivityManager.this.mActivityStack.isEmpty()) {
                    Iterator<Activity> iterator = mActivityStack.iterator();
                    while (iterator.hasNext()) {
                        Activity activity = iterator.next();
                        if (Arrays.asList(activityClass).contains(activity.getClass())) {
                            iterator.remove();
                            activity.finish();
                        }
                    }
                }
            }
        });
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (null != ActivityManager.this.mActivityStack && ActivityManager.this.mActivityStack.size() > 0) {
                    for (Activity activity : ActivityManager.this.mActivityStack) {
                        activity.finish();
                    }
                    ActivityManager.this.mActivityStack.clear();
                }
            }
        });
    }

    /**
     * 结束所有Activity  排除  LoginActivity
     */
    public void finishActivityExcludeLogin() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != ActivityManager.this.mActivityStack && ActivityManager.this.mActivityStack.size() > 0) {
                        Iterator<Activity> activityIterator = mActivityStack.iterator();
                        while (activityIterator.hasNext()) {
                            Activity activity = activityIterator.next();
                            if (!activity.getLocalClassName().contains("LoginActivity")) {
                                activity.finish();
                                activityIterator.remove();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 结束所有Activity  排除  MainActivity
     */
    public void finishActivityExcludeMain() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != ActivityManager.this.mActivityStack && !ActivityManager.this.mActivityStack.isEmpty()) {
                        Iterator<Activity> activityIterator = mActivityStack.iterator();
                        while (activityIterator.hasNext()) {
                            Activity activity = activityIterator.next();
                            if (!activity.getLocalClassName().contains("MainActivity")) {
                                activity.finish();
                                activityIterator.remove();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ActivityManager.this.finishAllActivity();
                ActivityManager.this.mActivityStack = null;
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
    }

    /**
     * 是否退出应用程序
     *
     * @return
     */
    public boolean isAppExit() {
        return null == ActivityManager.this.mActivityStack || ActivityManager.this.mActivityStack.isEmpty();
    }

    /**
     * 判断某个activity是否在栈顶
     *
     * @param context
     * @param activityClass 某个activity
     *
     * @return
     */
    public boolean isTopActivity(Context context, Class activityClass) {
        if (null == context || null == activityClass) {
            return false;
        }
        String activityName = activityClass.getName();
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> runningTaskInfoList = null;
        if (am != null) {
            runningTaskInfoList = am.getRunningTasks(1);
        }
        if (runningTaskInfoList != null && runningTaskInfoList.size() > 0) {
            ComponentName cpn = runningTaskInfoList.get(0).topActivity;
            return activityName.equals(cpn.getClassName());
        }
        return false;
    }

    public int getActivitySizes() {
        return mActivityStack.size();
    }
}
