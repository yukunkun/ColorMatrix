package com.matrix.yukun.matrix.chat_module.emoji;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

/**
 * 表情EditText
 *
 * @author PengZhenjin
 * @date 2016/7/18
 */
public class CubeEmoticonEditText extends AppCompatEditText {

    public CubeEmoticonEditText(Context context) {
        super(context);
    }

    public CubeEmoticonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CubeEmoticonEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        this.updateText(start, lengthAfter);
    }

    public void updateText(final int start, final int lengthAfter) {
        Editable text = getText();
        CharSequence s = text.subSequence(start, start + lengthAfter);
        EmoticonUtil.replaceEmoticons(getContext(), text, start, lengthAfter);
    }

    /**
     * 当前是否处于回复消息的状态
     *
     * @return
     */
    public boolean isInReplyMode() {
        Editable text = getText();
        ImageSpan[] imageSpans = text.getSpans(0, text.length(), ImageSpan.class);
        if (imageSpans != null && imageSpans.length > 0) {
            for (ImageSpan imageSpan : imageSpans) {
                String source = imageSpan.getSource();
            }
        }
        return false;
    }

    public String getReplyContentSource() {
        Editable text = getText();
        ImageSpan[] imageSpans = text.getSpans(0, text.length(), ImageSpan.class);
        if (imageSpans != null && imageSpans.length > 0) {
            for (ImageSpan imageSpan : imageSpans) {
                String source = imageSpan.getSource();
            }
        }
        return "";
    }
}
