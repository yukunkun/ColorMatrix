package com.matrix.yukun.matrix.chat_module.emoji.gif;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.widget.TextView;

import com.matrix.yukun.matrix.util.ThreadUtil;

public class AnimatedImageSpan extends DynamicDrawableSpan {

    private Drawable mDrawable;

    private TextView view;

    private class AminatedTask implements Runnable {
        @Override
        public void run() {
            ((AnimatedGifDrawable) mDrawable).nextFrame();
        }
    }

    private AminatedTask aminatedTask = new AminatedTask();

    public AnimatedImageSpan(Drawable d) {
        super();
        mDrawable = d;
        // Use handler for 'ticks' to proceed to next frame 
        ThreadUtil.schedule(aminatedTask, 0, ((AnimatedGifDrawable) mDrawable).getFrameDuration());
    }

    public void stopAnimate() {
        ThreadUtil.cancelSchedule(aminatedTask);
        aminatedTask = null;
        ((AnimatedGifDrawable) mDrawable).stop();
        ((AnimatedGifDrawable) mDrawable).clearBitmap();
    }

    /*
     * Return current frame from animated drawable. Also acts as replacement for super.getCachedDrawable(),
     * since we can't cache the 'image' of an animated image.
     */
    @Override
    public Drawable getDrawable() {
        return ((AnimatedGifDrawable) mDrawable).getDrawable();
    }

    /*
     * Copy-paste of super.getSize(...) but use getDrawable() to get the image/frame to calculate the size,
     * in stead of the cached drawable.
     */
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        try {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();

            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;

                fm.top = fm.ascent;
                fm.bottom = 0;
            }

            return rect.right;
        } catch (Exception e) {
            return 0;
        }
    }

    /*
     * Copy-paste of super.draw(...) but use getDrawable() to get the image/frame to draw, in stead of
     * the cached drawable.
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (aminatedTask == null) {
            return;
        }
        Drawable b = getDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}

