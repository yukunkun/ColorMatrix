package com.matrix.yukun.matrix.util.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 弱引用的handler，防止内存泄漏
 *
 * @author PengZhenjin
 * @date 2016-9-28
 */
public abstract class WeakReferenceHandler<T> extends Handler {
    private WeakReference<T> mReference;

    public WeakReferenceHandler(T reference) {
        this.mReference = new WeakReference<T>(reference);
    }

    @Override
    public void handleMessage(Message msg) {
        if (null == this.mReference.get()) {
            return;
        }
        this.handleMessage(this.mReference.get(), msg);
    }

    protected abstract void handleMessage(T reference, Message msg);
}
