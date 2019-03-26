package com.matrix.yukun.matrix.selfview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author Wangxx
 * @date 2017/1/10
 */

public class CubeRecyclerView extends RecyclerView {

    private GestureDetector mGestureDetector;

    private OnEventListener mListener;

    public CubeRecyclerView(Context context) {
        this(context, null);
    }

    public CubeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context);
    }

    private boolean isScroll = false;

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //把事件交给GestureDetector处理
        mGestureDetector.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_CANCEL || e.getAction() == MotionEvent.ACTION_UP) {
            isScroll = false;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //把事件交给GestureDetector处理
        mGestureDetector.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_CANCEL || e.getAction() == MotionEvent.ACTION_UP) {
            isScroll = false;
        }
        return super.onInterceptTouchEvent(e);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!isScroll) {
                if (mListener != null) {
                    mListener.onStartTouch();
                    isScroll = true;
                }
            }

            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isScroll) {
                if (mListener != null) {
                    mListener.onStartTouch();
                    isScroll = true;
                }
            }
            return true;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public interface OnEventListener {
        void onStartTouch();
    }

    public void setEventListener(OnEventListener listener) {
        this.mListener = listener;
    }
}
