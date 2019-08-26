package com.matrix.yukun.matrix.tool_module.gif.utils.gifencoder;

/**
 * author: kun .
 * date:   On 2019/2/20
 */
public interface GifEncoderListener {
    void startEncoder();
    void progressEncoder(int total, int progress);
    void endEncoder();

}
