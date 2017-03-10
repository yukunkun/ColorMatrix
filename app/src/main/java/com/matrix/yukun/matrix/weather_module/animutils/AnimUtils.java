package com.matrix.yukun.matrix.weather_module.animutils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.matrix.yukun.matrix.R;

/**
 * Created by yukun on 17-3-10.
 */
public class AnimUtils {

    public static void setBackUp(Context context,View view){
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.back_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim.setFillAfter(true);
        view.startAnimation(operatingAnim);
    }

    public static void setBackDown(Context context,View view){
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.back_anim_back);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim.setFillAfter(true);
        view.startAnimation(operatingAnim);
    }
    public static void setTitleUp(Context context, final View view){

        ValueAnimator animator = ValueAnimator.ofInt(0x00ffffff,0xC9101010);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                view.setBackgroundColor(curValue);
            }
        });

        animator.start();
    }
    public static void setTitleDown(Context context, final View view){

        ValueAnimator animator = ValueAnimator.ofInt(0xC9101010,0x00ffffff);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                view.setBackgroundColor(curValue);
            }
        });

        animator.start();
    }

    public static void setTempAnim(final View view){

        ValueAnimator animator = ValueAnimator.ofInt(10,0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                view.layout(view.getLeft(),view.getTop()+curValue,view.getRight(),view.getTop()+view.getHeight()+curValue);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }
}
