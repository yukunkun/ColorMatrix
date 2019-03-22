package com.matrix.yukun.matrix.video_module.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class TagLayout extends ViewGroup {
    private List<int[]> children;

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        children = new ArrayList<int[]>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount(); // tag的数量
        int left = 0; // 当前的左边距离
        int top = 0; // 当前的上边距离
        int totalHeight = 0; // WRAP_CONTENT时控件总高度
        int totalWidth = 0; // WRAP_CONTENT时控件总宽度

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();

            if (i == 0) { // 第一行的高度
                totalHeight = params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }

            if (left + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > getMeasuredWidth()) { // 换行
                left = 0;
                top += params.topMargin + child.getMeasuredHeight() + params.bottomMargin; // 每个TextView的高度都一样，随便取一个都行
                totalHeight += params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }

            children.add(new int[]{left + params.leftMargin, top + params.topMargin, left + params.leftMargin + child.getMeasuredWidth(), top + params.topMargin + child.getMeasuredHeight()});

            left += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;

            if (left > totalWidth) { // 当宽度为WRAP_CONTENT时，取宽度最大的一行
                totalWidth = left;
            }
        }

        int height = 0;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = totalHeight;
        }

        int width = 0;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = totalWidth;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            int[] position = children.get(i);
            child.layout(position[0], position[1], position[2], position[3]);
        }
    }
}
