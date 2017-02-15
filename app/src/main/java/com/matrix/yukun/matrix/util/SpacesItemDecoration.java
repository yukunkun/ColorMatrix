package com.matrix.yukun.matrix.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yukun on 17-1-19.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    int space;
    public SpacesItemDecoration(int space) {
        this.space = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);

        if(childLayoutPosition%2==0) {//偶数位
            outRect.right = space/2;
//            outRect.top=space/2;
            outRect.left = space/2;

        } else {//奇数位
            outRect.left = space/2;
//            outRect.top=space/2;
            outRect.right = space/2;

        }
    }
}
