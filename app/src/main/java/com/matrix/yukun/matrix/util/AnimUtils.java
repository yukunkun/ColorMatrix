package com.matrix.yukun.matrix.util;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;

/**
 * Created by yukun on 17-2-21.
 */
public class AnimUtils {

    private static AnimatorSet set;
    private static AnimatorSet setback;
    //
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

    public static void setScaleXAnimation(View view) {
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0,1f);
        mObjectAnimator.setDuration(500);
        mObjectAnimator.setInterpolator(new AccelerateInterpolator());
        mObjectAnimator.start();
    }

    public static void setWeatherBG( View view){
        ScaleAnimation mObjectAnimator  = new ScaleAnimation(1f,1.8f,1f,1.8f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        ScaleAnimation mObjectAnimator  = new ScaleAnimation(view.getWidth()*0.8f,view.getWidth(),view.getHeight()*0.8f,view.getHeight(),view.getWidth()/2,view.getHeight()/2);
        mObjectAnimator.setDuration(15000);
        mObjectAnimator.setRepeatCount(-1);
        view.setAnimation(mObjectAnimator);
        mObjectAnimator.start();
    }

}
