package com.matrix.yukun.matrix.gaia_module.util;

import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Project: chew2.0 Comments: AES加密 JDK version used: <JDK1.8> Author： xinwu-yang Create Date：
 * 2015年10月16日 Version: v1.0
 */
public class AES {
  private int load_num;
  private double load_time;
  private boolean threadCon;
  private double aestime=0;

  public void setThreadCon(boolean threadCon) {
    this.threadCon = threadCon;
  }

  static class AESParams {
    public byte[] inputBytes;
    public byte[] keyBytes;
    public byte[] ivBytes;
    int mode;
  }

  public void setLoad_num(int load_num, double load_time) {
    this.load_num = load_num;
    this.load_time=load_time;
  }

//  public static String cbc_pkcs5_128_encrypt(String plainText, String keyHexString,
//      String ivHexString) {
//    String result = null;
//    byte[] bytes = cbc_pkcs5_128_compute(
//        prepareArguments(plainText, keyHexString, ivHexString, Cipher.ENCRYPT_MODE));
//    if (bytes != null) {
//      result = Hex.encodeHexString(bytes);
//    }
//    return result;
//  }


  public synchronized boolean  cbc_pkcs5_128_decrypt(File cipherText, String keyHexString,
                                                     String ivHexString) {

    byte[] bytes = cbc_pkcs5_128_compute(
        prepareArguments(cipherText, keyHexString, ivHexString, Cipher.DECRYPT_MODE));
    if (bytes != null) {
      FileOutputStream fileOutputStream=null;
      try {
        //写入文件
        fileOutputStream=new FileOutputStream(cipherText);
        fileOutputStream.write(bytes);
        Log.i("-------AES","成功写入"+load_num);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }finally {
        try {
          if(fileOutputStream!=null){
            fileOutputStream.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      Log.i("-------loadtime",aestime+"+"+load_time+"+"+(aestime+load_time));
      EventBus.getDefault().post(new EventPosition(load_num,aestime+load_time));
      aestime=0;
      threadCon=false;
    }
    return true;
  }

  private static byte[] cbc_pkcs5_128_compute(AESParams params) {
    byte[] result = null;
    try {
      if(params.inputBytes==null){
        return null;
      }

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

  private static AESParams prepareArguments(File input, String keyHexString, String ivHexString,
                                            int mode) {
    if (input == null) {
      throw new IllegalArgumentException("The input for aes_cbc_pkcs5_128 must be non-empty.");
    }

    if (ivHexString == null || ivHexString.isEmpty()) {
      throw new IllegalArgumentException(
              "The ivHexString for aes_cbc_pkcs5_128 must be non-empty.");
    }

    if (keyHexString == null || keyHexString.isEmpty()) {
      throw new IllegalArgumentException(
          "The keyHexString for aes_cbc_pkcs5_128 must be non-empty.");
    }

    AESParams params = new AESParams();
    switch (mode) {
//      case Cipher.ENCRYPT_MODE:
//        byte[] bs = new byte[(int)input.length()];
//
//        FileInputStream fileInputStream = null;
//        try {
//          fileInputStream = new FileInputStream(input);
//
//          int ch = 0;
//          while ((ch = fileInputStream.read()) != -1) {
//            fileInputStream.read(bs);
//          }
//
//        } catch (FileNotFoundException e) {
//          e.printStackTrace();
//
//        } catch (IOException e) {
//          e.printStackTrace();
//        }finally {
//          try {
//            //关闭流
//            fileInputStream.close();
//          } catch (IOException e) {
//            e.printStackTrace();
//          }
//        }
//        params.inputBytes = bs;
//        break;
      case Cipher.DECRYPT_MODE:

        FileInputStream inputStream = null;
        ByteArrayOutputStream bos=null;
        BufferedInputStream in=null;
        byte[] bs=null;
        try {
          if(!input.exists()){
            break;
          }

          inputStream = new FileInputStream(input);
          in = new BufferedInputStream(inputStream);
          bos=new ByteArrayOutputStream();
          int buf_size = 1024*10;
          byte[] buffer = new byte[buf_size];
          int len = 0;
          while (-1 != (len = in.read(buffer, 0, buf_size))) {
            bos.write(buffer, 0, len);
          }
          bs=bos.toByteArray();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (inputStream != null) {
            try {
              inputStream.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          if (bos!=null){
            try {
              bos.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
            try {
              in.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }

//        byte[] bs = new byte[2048];
//        FileInputStream fs = null;
//        ByteArrayOutputStream bos = null;
//        try {
//          fs = new FileInputStream(input);
//          bos = new ByteArrayOutputStream();
//          int ch;
//          while ((ch = fs.read(bs)) != -1) {
//            bos.write(bs,0,ch);
//          }
//          bs = bos.toByteArray();
//        } catch (Exception e) {
//          e.printStackTrace();
//        }finally {
//          try {
//            //关闭流
//            if(fs!=null){
//              fs.close();
//            }
//            if(bos!=null){
//              bos.close();
//            }
//          } catch (IOException e) {
//            e.printStackTrace();
//          }
//        }
        params.inputBytes = bs;
    }

    try {
      params.ivBytes = Hex.decodeHex(ivHexString.toCharArray());
    } catch (Exception e) {
      throw new IllegalArgumentException(
              "The ivHexString for aes_cbc_pkcs5_128 must be a valid Hex String.");
    }
    try {
      params.keyBytes = Hex.decodeHex(keyHexString.toCharArray());
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException(
          "The keyHexString for aes_cbc_pkcs5_128 must be a valid Hex String.");
    }
    params.mode = mode;
    return params;
  }

}
