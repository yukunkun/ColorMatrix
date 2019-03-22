package com.matrix.yukun.matrix.video_module.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by luxiansheng on 16/3/6.
 *
 */
public class NoScrolledGridView extends GridView {
    public NoScrolledGridView(Context context) {
        super(context);
    }

    public NoScrolledGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrolledGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
