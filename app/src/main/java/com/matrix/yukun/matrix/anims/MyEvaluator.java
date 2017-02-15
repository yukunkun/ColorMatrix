package com.matrix.yukun.matrix.anims;

import android.animation.TypeEvaluator;

/**
 * Created by yukun on 17-1-12.
 */
public class MyEvaluator implements TypeEvaluator<Integer> {
    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }
}
