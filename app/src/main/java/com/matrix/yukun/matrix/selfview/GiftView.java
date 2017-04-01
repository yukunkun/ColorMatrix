package com.matrix.yukun.matrix.selfview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yukun on 17-4-1.
 */
public class GiftView extends View {
    public GiftView(Context context) {
        this(context,null);
    }

    public GiftView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final Random random = new Random();

    //准备礼物的图片数组
    private List<Integer> drawables=new ArrayList<>();

    // 用于画礼物的画笔
    private final Paint myPaint = new Paint();

    //坐标类的数组---礼物的位置
    private Coordinate[] gifts = new Coordinate[80];

    //窗体的初始高宽
    int sHeight = 0;
    int sWidth = 0;
    //记录礼物的个数
    private int giftCount = 0;

    /**
     * 设置当前窗体的实际宽高
     */
    public void SetView(int height, int width) {
        sHeight = height - 100;
        sWidth = width;
        drawables.add(R.mipmap.gift_1);
    }

    /**
     * 随机的产生礼物的位置
     */
    public void produceGiftRandom(int count) {
        giftCount = count;
        for (int i = 0; i < count; i++) {
            //横坐标和纵坐标都是随机产生的
            gifts[i] = new Coordinate(random.nextInt(sWidth), -random.nextInt(sHeight));
        }
    }

    public void getDrawables(List<Integer> list){
        drawables=list;
    }

    /**
     * 通过画笔将礼物绘制上去
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < giftCount; x += 1) {
            if (gifts[x].mY >= sHeight) {
                gifts[x].mY = 0;
            }
            // 礼物下落的数值速度
            gifts[x].mY += 20;
            // 让礼物飘动起来
            if (random.nextBoolean()) {
                //让水平方向有一个随机移动的速度
                int ran = random.nextInt(20);
                gifts[x].mX += 2 - ran;
                if(gifts[x].mX < 0){
                    gifts[x].mX = sWidth;
                }else if(gifts[x].mX > sWidth){
                    gifts[x].mX = 0;
                }
            }
            Resources mResources = getResources();
            int drawableIndex=random.nextInt(drawables.size());
            //不断的切换十张图片造成闪烁的效果
            canvas.drawBitmap(((BitmapDrawable) mResources.getDrawable(drawables.get(drawableIndex))).getBitmap(), ((float) gifts[x].mX),
                    ((float) gifts[x].mY), myPaint);
        }
    }

    /**
     * 自定义一个坐标类
     */
    private class Coordinate{
        public int mX;
        public int mY;

        public Coordinate(int x, int y) {
            mX = x;
            mY = y;
        }
    }
}
