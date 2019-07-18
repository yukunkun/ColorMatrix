package com.matrix.yukun.matrix.gaia_module.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Project: chew2.0 Comments: AES加密 JDK version used: <JDK1.8> Author： xinwu-yang Create Date：
 * 2015年10月16日 Version: v1.0
 */
public class AESFORKEY {

  static class AESParams {
    public byte[] inputBytes;
    public byte[] keyBytes;
    public byte[] ivBytes;
    int mode;
  }

  public static String cbc_pkcs5_128_encrypt(String plainText, String keyHexString,
                                             String ivHexString) throws IOException {
    String result = null;
    byte[] bytes = cbc_pkcs5_128_compute(
        prepareArguments(plainText, keyHexString, ivHexString, Cipher.ENCRYPT_MODE));
    if (bytes != null) {
      result = Hex.encodeHexString(bytes);
    }
    return result;
  }

  public static String cbc_pkcs5_128_decrypt(String cipherText, String keyHexString,
                                             String ivHexString) {
    String result = null;
    byte[] bytes = cbc_pkcs5_128_compute(
        prepareArguments(cipherText, keyHexString, ivHexString, Cipher.DECRYPT_MODE));
    if (bytes != null) {
      result = StringUtils.newStringUtf8(bytes);
    }
    return result;
  }

  private static byte[] cbc_pkcs5_128_compute(AESParams params) {
    byte[] result = null;
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(params.mode, new SecretKeySpec(params.keyBytes, "AES"),
          new IvParameterSpec(params.ivBytes));
      result = cipher.doFinal(params.inputBytes);
    } catch (Exception e) {
      e.printStackTrace();
      result = null;
    }
    return result;
  }

  private static AESParams prepareArguments(String input, String keyHexString, String ivHexString,
                                            int mode) {
    if (input == null) {
      throw new IllegalArgumentException("The input for aes_cbc_pkcs5_128 must be non-empty.");
    }
    if (keyHexString == null || keyHexString.isEmpty()) {
      throw new IllegalArgumentException(
          "The keyHexString for aes_cbc_pkcs5_128 must be non-empty.");
    }
    if (ivHexString == null || ivHexString.isEmpty()) {
      throw new IllegalArgumentException(
          "The ivHexString for aes_cbc_pkcs5_128 must be non-empty.");
    }
    AESParams params = new AESParams();
    switch (mode) {
      case Cipher.ENCRYPT_MODE:
        try {
          params.inputBytes = Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
          e.printStackTrace();
        }
        break;
      case Cipher.DECRYPT_MODE:
        try {
          params.inputBytes = Hex.decodeHex(input.toCharArray());
        } catch (Exception e) {
          throw new IllegalArgumentException(
              "The input for aes_cbc_pkcs5_128_decrypt must be a valid Hex String.");
        }
        break;
    }
    try {
      params.keyBytes = Hex.decodeHex(keyHexString.toCharArray());
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException(
          "The keyHexString for aes_cbc_pkcs5_128 must be a valid Hex String.");
    }
    try {
      params.ivBytes = Hex.decodeHex(ivHexString.toCharArray());
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "The ivHexString for aes_cbc_pkcs5_128 must be a valid Hex String.");
    }
    params.mode = mode;
    return params;
  }
}
