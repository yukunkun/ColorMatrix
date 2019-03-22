package com.matrix.yukun.matrix.util.log;

import android.util.Log;

public class DefaultLogHandle extends BaseLogHandle {

    @Override
    public void log(LogLevel level, String tag, String message, StackTraceElement[] stackTrace) {
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

        Log.println(level.getCode(), TAG, buffer.toString());

        buffer.delete(0, buffer.length());
    }
}
