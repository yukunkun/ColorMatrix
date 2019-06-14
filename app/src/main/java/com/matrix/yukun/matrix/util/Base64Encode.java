package com.matrix.yukun.matrix.util;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * author: kun .
 * date:   On 2019/6/14
 */
public class Base64Encode {
    /**
     * BASE64解密
     *
     * @return
     * @throws Exception
     */
    public static String setDecrypt(String encodeWord){

        try {
            return new String(Base64.decode(encodeWord, Base64.NO_WRAP), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    /**
     * 加密
     * oldWord：需要加密的文字/比如密码
     */
    public static String setEncryption(String oldWord){
        try {
            return  Base64.encodeToString(oldWord.getBytes("utf-8"), Base64.NO_WRAP);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
