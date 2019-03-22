package com.matrix.yukun.matrix.video_module.utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by yukun on 17-12-8.
 */

public class MyType implements TypeEvaluator<PointF>{
    private PointF pointFFirst,pointFSecond;

    public MyType(PointF start, PointF end){
        this.pointFFirst =start;
        this.pointFSecond = end;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        PointF result = new PointF();
        float left = 1 - fraction;
        result.x = (float) (startValue.x*Math.pow(left,3)+3*pointFFirst.x*Math.pow(left,2)*fraction+3*pointFSecond.x*Math.pow(fraction, 2)*left+endValue.x*Math.pow(fraction,3));
        result.y= (float) (startValue.y*Math.pow(left,3)+3*pointFFirst.y*Math.pow(left,2)*fraction+3*pointFSecond.y*Math.pow(fraction, 2)*left+endValue.y*Math.pow(fraction,3));
        return result;
    }
}
