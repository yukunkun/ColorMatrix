package com.matrix.yukun.matrix.desk_module.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: kun .
 * date:   On 2019/8/15
 */
public class ClockNormalView extends View {
    public ClockNormalView(Context context) {
        super(context);
        init(context,null,0);
    }

    public ClockNormalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public ClockNormalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

    }


}
