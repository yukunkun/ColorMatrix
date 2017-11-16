package com.matrix.yukun.matrix.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.matrix.yukun.matrix.R;

/**
 * Created by yukun on 17-2-21.
 */
public class AnimUtils {

    private static AnimatorSet set;
    private static AnimatorSet setback;

    public static void doAnimateOpen(View view, int index, int total, int radius,int time) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.toRadians(90)/(total - 1) * index;
        int translationX = (int) (radius * Math.sin(degree));
        int translationY = (int) (radius * Math.cos(degree));

        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1));
        //动画周期为500ms
        set.setDuration(1 * time).start();
    }

    public static void doAnimateClose(final View view, int index, int total,
                                      int radius,int time) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.PI * index / ((total - 1) * 2);
        int translationX = (int) (radius * Math.sin(degree));
        int translationY = (int) (radius * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.1f), //bug
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.1f),//0.1为了避免消失为0，会产生bug
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f));
        set.setDuration(1 * time).start();
    }

    public static void setSettingDown(Context context, View view){
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.back_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim.setFillAfter(true);
        view.startAnimation(operatingAnim);
    }

    public static void setSettingUp(Context context,View view){
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.back_anim_back);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim.setFillAfter(true);
        view.startAnimation(operatingAnim);

    }

    public static  void giftBigAnim(View view,Context context){
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        int width = ScreenUtils.instance().getWidth(context);
        int height = ScreenUtils.instance().getHeight(context);

        int translationX = width/2;
        int translationY = height/2;

        set = new AnimatorSet();
        setback = new AnimatorSet();

        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX,0),//左边为负,右边为正
                ObjectAnimator.ofFloat(view, "translationY",-translationY,0),//下为负,上为正
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f), //bug
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),//0.1为了避免消失为0，会产生bug
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f));
        setback.setStartDelay(800);
        set.setDuration(1 * 800).start();

        setback.playTogether(
                ObjectAnimator.ofFloat(view, "translationX",0, -translationX),//左边为负,右边为正
                ObjectAnimator.ofFloat(view, "translationY",0, -translationY),//下为负,上为正
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),//0.1为了避免消失为0，会产生bug
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f));
        setback.setStartDelay(1*800*5);
        setback.setDuration(1 * 800).start();

    }
    public static  void cancelAnim(){
        if(set!=null){
            set.cancel();
        }
        if(setback!=null){
            setback.cancel();
        }
    }

}
