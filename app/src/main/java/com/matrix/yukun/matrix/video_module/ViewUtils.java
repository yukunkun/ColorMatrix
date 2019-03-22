package com.matrix.yukun.matrix.video_module;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.util.ScreenUtils;

/**
 * Created by yukun on 17-9-29.
 */

public class ViewUtils {
    public static TextView getTextView(Context context){
        final TextView tv = new TextView(context);
        tv.setTextColor(context.getResources().getColor(R.color.color_dadada));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(ScreenUtils.dp2Px(context, 8), 10, ScreenUtils.dp2Px(context, 8), 10);
        tv.setPadding(ScreenUtils.dp2Px(context, 8), 8, ScreenUtils.dp2Px(context, 8), 8);
        tv.setLayoutParams(params);
        tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_write_bg));
        tv.setClickable(true);
        return tv;
    }
}
