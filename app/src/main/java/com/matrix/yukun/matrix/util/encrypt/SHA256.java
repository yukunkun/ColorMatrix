package com.matrix.yukun.matrix.util.encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by root on 15-8-5.
 *
 * SHA256编码
 */
public class SHA256 {
    private static byte [] getHash(String password) {
        MessageDigest digest = null ;
        try {
            digest = MessageDigest. getInstance( "SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }

    /**
     * @param strForEncrypt 待编码字串
     * @return 编码后的SHA256字串
     */
    public static String bin2hex(String strForEncrypt) {
        byte [] data = getHash(strForEncrypt);
        return String.format( "%0" + (data.length * 2) + "X", new BigInteger(1, data)).toLowerCase();
    }
}
