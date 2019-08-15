package com.matrix.yukun.matrix.gaia_module.adapter.create_person;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by haiyang-lu on 16-5-4.
 */
public class SpaceReDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public SpaceReDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        outRect.top=space/2;
        outRect.bottom=space;
        outRect.left=space;
        outRect.right=space;
    }
}
