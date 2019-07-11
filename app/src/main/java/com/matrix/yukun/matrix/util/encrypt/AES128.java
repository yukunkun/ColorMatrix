package com.matrix.yukun.matrix.util.encrypt;

import com.qq.e.comm.util.StringUtil;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES128 {

    /**
     * HEX String 转 字节数组
     *
     * @param hexString HEX String
     * @return 转码数据
     */
    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    /**
     * 字节数组转HEX String
     *
     * @param buf
     * @return 编码厚的HEX String
     */
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private final static String HEX = "0123456789abcdef";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

    /**
     * 编码
     *
     *
     *
     *
    *
     * @param seed 种子
     * @param cleartext 要编码的数据
     * @param ivStr iv
     * @return 编码后的数据
     * @throws Exception */

    public static String encrypt(String seed, String cleartext, String ivStr) throws Exception {
        byte[] src = (cleartext.getBytes(Charsets.UTF_8));
        byte[] key = Hex.decodeHex(seed.toCharArray());
        byte[] iv = Hex.decodeHex(ivStr.toCharArray());
        byte[] result = encrypt(key, src, iv);
        return toHex(result);
    }

    /**
     * 解码
     *
     * @param seed 种子
     * @param encrypted 待解码数据
     * @param ivStr iv
     * @return 解码后的数据
     * @throws Exception
     */
    public static String decrypt(String seed, String encrypted, String ivStr) throws Exception {
        byte[] rawKey = Hex.decodeHex(seed.toCharArray());
        byte[] enc = Hex.decodeHex(encrypted.toCharArray());
        byte[] iv = Hex.decodeHex(ivStr.toCharArray());
        byte[] result = decrypt(rawKey, enc, iv);
        return new String(result);
    }

    /**
     * 编码
     *
     * @param raw key
     * @param clear 待编码数据
     * @param iv iv
     * @return 编码后的数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] raw, byte[] clear, byte[] iv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    /**
     * 解码
     *
     * @param raw key
     * @param encrypted 贷解码数据
     * @param iv iv
     * @return 解码后的数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] raw, byte[] encrypted, byte[] iv) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}
