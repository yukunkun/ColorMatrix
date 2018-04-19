package com.ykk.pluglin_video.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yukun on 17-7-5.
 */

public class SpacesDoubleDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int left;
    private int right;
    private int bottom;


    public SpacesDoubleDecoration(int top, int left, int right, int bottom) {
        this.top = top;
        this.left=left;
        this.right=right;
        this.bottom=bottom;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        if(childLayoutPosition%2==0) {//偶数位
            outRect.right = right;
            outRect.top= top;
            outRect.left=left;
            outRect.bottom=bottom;
        } else {//奇数位
            outRect.right = left;
            outRect.top= top;
            outRect.left=right;
            outRect.bottom=bottom;
        }
    }

}
