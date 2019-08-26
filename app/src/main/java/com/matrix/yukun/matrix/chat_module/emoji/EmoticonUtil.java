package com.matrix.yukun.matrix.chat_module.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.matrix.yukun.matrix.chat_module.emoji.gif.AnimatedGifDrawable;
import com.matrix.yukun.matrix.chat_module.emoji.gif.AnimatedImageSpan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情工具类
 *
 * @author PengZhenjin
 * @date 2017-1-12
 */
public class EmoticonUtil {
    public static final float DEF_SCALE   = 0.6f;
    public static final float SMALL_SCALE = 0.45F;

    public static ArrayList<AnimatedImageSpan> imageSpans = new ArrayList<>();

    public static SpannableStringBuilder gifEmoticons(Context context, TextView gifTextView, String value, float scale, int align) {
        Matcher matcher = EmoticonManager.getPattern().matcher(value);
        int len = 0;
        while (matcher.find()) {
            len++;
        }
        // TODO: 2018/5/22 大于10个时不显示动态图 但这个限制应放在业务层做
        if (len > 10) {
            Log.d("EmoticonUtil","EmoticonUtil:"+len);
            return replaceEmoticons(context, value, scale, align);
        }
        // TODO: 2018/5/22 开启会造成界面卡顿 找到新的gif解析方案前不建议打开动图emoji
        else {
            Log.d("EmoticonUtil","EmoticonUtil====:"+len);
            return replaceEmoticons(context, gifTextView, value, scale, align);
        }
    }

    private static SpannableStringBuilder replaceEmoticons(Context context, String value, float scale, int align) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }

        SpannableStringBuilder mSpannableString = new SpannableStringBuilder(value);
        Matcher matcher = EmoticonManager.getPattern().matcher(value);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String emot = value.substring(start, end);
            Drawable d = getEmotDrawable(context, emot, scale);
            if (d != null) {
                ImageSpan span = new ImageSpan(d, align);
                mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return mSpannableString;
    }

    private static SpannableStringBuilder replaceEmoticons(Context context, final TextView gifTextView, String value, float scale, int align) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }

        SpannableStringBuilder mSpannableString = new SpannableStringBuilder(value);
        Matcher matcher = EmoticonManager.getPattern().matcher(value);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String gif = value.substring(start, end);
            /**
             * 如果open这里不抛异常说明存在gif，则显示对应的gif
             * 否则说明gif找不到，则显示png
             */
            String gifPath = EmoticonManager.getGifPath(gif);
            if (!TextUtils.isEmpty(gifPath)) {
                InputStream is = null;
                try {
                    is = context.getAssets().open(gifPath);
                    AnimatedImageSpan animatedImageSpan = new AnimatedImageSpan(new AnimatedGifDrawable(is, new AnimatedGifDrawable.UpdateListener() {
                        @Override
                        public void update() {
                            gifTextView.postInvalidate();
                        }
                    }));
                    imageSpans.add(animatedImageSpan);
                    mSpannableString.setSpan(animatedImageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else {
                Drawable d = getEmotDrawable(context, gif, scale);
                if (d != null) {
                    ImageSpan span = new ImageSpan(d, align);
                    mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return mSpannableString;
    }

    private static Pattern mATagPattern = Pattern.compile("<a.*?>.*?</a>");

    public static SpannableStringBuilder makeSpannableStringTags(Context context, String value, float scale, int align) {
        return makeSpannableStringTags(context, value, DEF_SCALE, align, true);
    }

    public static SpannableStringBuilder makeSpannableStringTags(Context context, String value, float scale, int align, boolean bTagClickable) {
        ArrayList<ATagSpan> tagSpans = new ArrayList<ATagSpan>();
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        //a标签需要替换原始文本,放在moonutil类中
        Matcher aTagMatcher = mATagPattern.matcher(value);

        int start = 0;
        int end = 0;
        while (aTagMatcher.find()) {
            start = aTagMatcher.start();
            end = aTagMatcher.end();
            String atagString = value.substring(start, end);
            ATagSpan tagSpan = getTagSpan(atagString);
            value = value.substring(0, start) + tagSpan.getTag() + value.substring(end);
            tagSpan.setRange(start, start + tagSpan.getTag().length());
            tagSpans.add(tagSpan);
            aTagMatcher = mATagPattern.matcher(value);
        }

        SpannableStringBuilder mSpannableString = new SpannableStringBuilder(value);
        Matcher matcher = EmoticonManager.getPattern().matcher(value);
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            String emot = value.substring(start, end);
            Drawable d = getEmotDrawable(context, emot, scale);
            if (d != null) {
                ImageSpan span = new ImageSpan(d, align);
                mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (bTagClickable) {
            for (ATagSpan tagSpan : tagSpans) {
                mSpannableString.setSpan(tagSpan, tagSpan.start, tagSpan.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return mSpannableString;
    }

    public static void replaceEmoticons(Context context, Editable editable, int start, int count) {
        if (count <= 0 || editable.length() < start + count) {
            return;
        }

        CharSequence s = editable.subSequence(start, start + count);
        Matcher matcher = EmoticonManager.getPattern().matcher(s);
        int i = 0;
        while (matcher.find()) {
            synchronized (matcher) {
                if (i > 50) {
                    break;
                }
                int from = start + matcher.start();
                int to = start + matcher.end();
                String emot = editable.subSequence(from, to).toString();
                Drawable d = getEmotDrawable(context, emot, SMALL_SCALE);
                if (d != null) {
                    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                    editable.setSpan(span, from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                i++;
            }
        }
    }

    private static Drawable getEmotDrawable(Context context, String text, float scale) {
        Drawable drawable = EmoticonManager.getDrawable(context, text);

        // scale
        if (drawable != null) {
            int width = (int) (drawable.getIntrinsicWidth() * scale);
            int height = (int) (drawable.getIntrinsicHeight() * scale);
            drawable.setBounds(0, 0, width, height);
        }

        return drawable;
    }

    private static ATagSpan getTagSpan(String text) {
        String href = null;
        String tag = null;
        if (text.toLowerCase().contains("href")) {
            int start = text.indexOf("\"");
            int end = text.indexOf("\"", start + 1);
            if (end > start) {
                href = text.substring(start + 1, end);
            }
        }
        int start = text.indexOf(">");
        int end = text.indexOf("<", start);
        if (end > start) {
            tag = text.substring(start + 1, end);
        }
        return new ATagSpan(tag, href);
    }

    private static class ATagSpan extends ClickableSpan {
        private int    start;
        private int    end;
        private String mUrl;
        private String tag;

        ATagSpan(String tag, String url) {
            this.tag = tag;
            this.mUrl = url;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }

        public String getTag() {
            return tag;
        }

        public void setRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void onClick(View widget) {
            try {
                if (TextUtils.isEmpty(mUrl)) {
                    return;
                }
                Uri uri = Uri.parse(mUrl);
                String scheme = uri.getScheme();
                if (TextUtils.isEmpty(scheme)) {
                    mUrl = "http://" + mUrl;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
