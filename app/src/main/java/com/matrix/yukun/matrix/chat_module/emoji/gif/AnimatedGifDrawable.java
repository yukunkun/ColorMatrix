package com.matrix.yukun.matrix.chat_module.emoji.gif;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AnimatedGifDrawable extends AnimationDrawable {

    private int mCurrentIndex = 0;
    private WeakReference<UpdateListener> updateListenerWeakReference;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    public AnimatedGifDrawable(InputStream source, UpdateListener listener) {
        updateListenerWeakReference = new WeakReference<UpdateListener>(listener);
        GifDecoder decoder = new GifDecoder();
        decoder.read(source);
        // Iterate through the gif frames, add each as animation frame
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            Bitmap bitmap = decoder.getFrame(i);
            bitmaps.add(bitmap);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            // Explicitly set the bounds in order for the frames to display
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            addFrame(drawable, decoder.getDelay(i));
            if (i == 0) {
                // Also set the bounds for this container drawable
                setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            }
        }
    }

    /**
     * Naive method to proceed to next frame. Also notifies listener.
     */
    public void nextFrame() {
        mCurrentIndex = (mCurrentIndex + 1) % getNumberOfFrames();
        UpdateListener updateListener = updateListenerWeakReference.get();
        if (updateListener != null) {
            updateListener.update();
        }
    }

    /**
     * Return display duration for current frame
     */
    public int getFrameDuration() {
        return getDuration(mCurrentIndex);
    }

    /**
     * Return drawable for current frame
     */
    public Drawable getDrawable() {
        return getFrame(mCurrentIndex);
    }

    public void clearBitmap() {
        for (Bitmap bitmap : bitmaps) {
            if(bitmap!=null&&!bitmap.isRecycled()){
                bitmap.recycle();
            }
        }
    }

    /**
     * Interface to notify listener to update/redraw Can't figure out how to
     * invalidate the drawable (or span in which it sits) itself to force redraw
     */
    public interface UpdateListener {
        void update();
    }
}