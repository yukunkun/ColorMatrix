package com.matrix.yukun.matrix.util;

import android.graphics.ColorMatrix;

import java.util.ArrayList;

/**
 * Created by yukun on 17-1-25.
 */
public class MatrixArray {
    static ArrayList<ColorMatrix> colorMatrices=new ArrayList<>();
    public static void setColorMatrix(){
        ColorMatrix colorMatrix_0 = new ColorMatrix(new float[]{
                1,0,0,0,0,
                0,1,0,0,0,
                0,0,1,0,0,
                0,0,0,1,0
        });
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1/2f,1/2f,1/2f,0,0,
                1/3f,1/3f,1/3f,0,0,
                1/4f,1/4f,1/4f,0,0,
                0,0,0,1,0
        });
        ColorMatrix colorMatrix_1 = new ColorMatrix(new float[]{ //R,G,B
                -1,0,0,0,255,
                0,-1,0,0,255,
                0,0,-1,0,255,
                0,0,0,1,0
        });
        ColorMatrix colorMatrix_21 = new ColorMatrix(new float[]{
                0.213f, 0.715f, 0.072f, 0, 0,
                0.213f, 0.715f, 0.072f, 0, 0,
                0.213f, 0.715f, 0.072f, 0, 0,
                0,       0,    0, 1, 0,
        });
        ColorMatrix colorMatrix_2 = new ColorMatrix(new float[]{
                0, 1, 0, 0, 0,
                1, 0, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_3 = new ColorMatrix(new float[]{
                0, 0, 1, 0, 0,
                0, 1, 0, 0, 0,
                1, 0, 0, 0, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_4 = new ColorMatrix(new float[]{
                1, 0, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_7 = new ColorMatrix(new float[]{
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_8 = new ColorMatrix(new float[]{
                0, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_9 = new ColorMatrix(new float[]{
                1, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_10 = new ColorMatrix(new float[]{
                1f, 0.5f, 0, 0, 0,
                0.5f, 1f, 0, 0,0,
                0, 0, 1f, 0.5f, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_11 = new ColorMatrix(new float[]{
                0.5f, 0f, 0, 0, 0,
                0f, 1f, 0.5f, 0,0,
                0, 0.5f, 1f, 1, 0,
                0, 0, 0, 1, 0,
        });
        ColorMatrix colorMatrix_12 = new ColorMatrix(new float[]{
                1f, 0, 0.5f, 0, 0,
                0, 0.5f, 0, 0,0,
                0.5f, 0, 1f, 1, 0,
                0, 0, 0, 1, 0,
        });
        colorMatrices.add(colorMatrix_0);
        colorMatrices.add(colorMatrix);
        colorMatrices.add(colorMatrix_1);
        colorMatrices.add(colorMatrix_21);
        colorMatrices.add(colorMatrix_2);
        colorMatrices.add(colorMatrix_3);
        colorMatrices.add(colorMatrix_4);
        colorMatrices.add(colorMatrix_7);
        colorMatrices.add(colorMatrix_8);
        colorMatrices.add(colorMatrix_9);
        colorMatrices.add(colorMatrix_10);
        colorMatrices.add(colorMatrix_11);
        colorMatrices.add(colorMatrix_12);

    }

    public static ArrayList<ColorMatrix> getColorMatrices() {
        setColorMatrix();
        return colorMatrices;
    }
}
