package com.matrix.yukun.matrix.chat_module.emoji;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

/**
 * 表情TextView
 *
 * @author PengZhenjin
 * @date 2016/7/18
 */
public class CubeEmoticonTextView extends AppCompatTextView {
    private static final String TAG = CubeEmoticonTextView.class.getSimpleName();

    public CubeEmoticonTextView(Context context) {
        super(context);
    }

    public CubeEmoticonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CubeEmoticonTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            String str = text.toString();
            SpannableStringBuilder spannableStringBuilder = EmoticonUtil.gifEmoticons(this.getContext(), this, str, EmoticonUtil.DEF_SCALE, ImageSpan.ALIGN_BOTTOM);
            text = spannableStringBuilder;
            super.setText(text, type);
        }
    }
}
