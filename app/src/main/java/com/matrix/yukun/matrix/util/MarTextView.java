package com.matrix.yukun.matrix.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yukun on 17-3-21.
 */
public class MarTextView extends TextView {
    public MarTextView(Context context) {
        super(context);
    }

    public MarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
