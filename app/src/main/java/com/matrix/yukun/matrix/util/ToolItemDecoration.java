package com.matrix.yukun.matrix.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by yukun on 17-1-19.
 */
public class ToolItemDecoration extends RecyclerView.ItemDecoration {

    int space;

    public ToolItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);

        outRect.left = space / 2;
        outRect.top = space / 2;
        outRect.right = space / 2;
        outRect.bottom = space / 2;

    }
}
