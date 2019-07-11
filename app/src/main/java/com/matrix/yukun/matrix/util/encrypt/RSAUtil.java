package com.matrix.yukun.matrix.util.encrypt;

import android.content.Context;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by yukun on 17-1-22.
 */
public class RSAUtil {
    public static final String RSA_PUBLIC = "";
    public static final String RSA = "RSA";// 非对称加密密钥算法
    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     * @throws
     * @throws Exception
     */
    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception
    {
        byte[] decodeKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param content
     * @return
     */
    public static String encryptByPublic(Context context, String content)
    {
        try
        {
            String pubKey = getPublicKeyFromAssets(context);
            PublicKey publicKey = getPublicKeyFromX509(RSA, pubKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] plaintext = content.getBytes();
            byte[] output = cipher.doFinal(plaintext);

            String str = Base64.encodeToString(output, Base64.DEFAULT);
            return str;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取公钥
     *
     * @param
     * @return
     */
    private static String getPublicKeyFromAssets(Context context)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getResources().getAssets().open("public_key.pem"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
            {
                if (line.charAt(0) == '-')
                {
                    continue;
                }
                Result += line;
            }
            return Result;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
