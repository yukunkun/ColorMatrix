package com.matrix.yukun.matrix.gif_module.utils.gifencoder;

/**
 * author: kun .
 * date:   On 2019/2/20
 */
public interface GifEncoderListener {
    void startEncoder();
    void progressEncoder( int total,int progress);
    void endEncoder();

}
