package com.matrix.yukun.matrix.util.encrypt;

import android.content.Context;

/**
 * Created by yukun on 17-1-20.
 */
public class TestRSA {
//    String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvvMNaVMkofdzOYb8ZyC95oHbdP38AEdlmn1aAbFbAoH1v+w4mYPAJ/TzZ2iy32OWwQTSjCKq7LUyT03zcZs8if/Hl4HHoIjjLRAbTRv0VwY5zrA5d0n/ixbMEZ7NEo0I1ugCti+9kbtIH8lmjnesAr3d6E+lz3e8tknNsVVbJy2v+AoU8oloo8ODhABGeOY8M+cmWC3jLURYWcxYlPgLGvSvSeWs461EtJEH/IaVNr5yNd0yAMx9HNLV3niPZbmdTzBMNsbKvXPEgjT9rF2eOlrSNihQ17Kqfed8oZ+it/PaVp/VTwct97YkIK4nchBTm2tpvOqfKSEE/cs1LQthAwIDAQAB";
    String publicKeys="-----BEGIN PUBLIC KEY-----\n" +
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvvMNaVMkofdzOYb8ZyC9\n" +
        "5oHbdP38AEdlmn1aAbFbAoH1v+w4mYPAJ/TzZ2iy32OWwQTSjCKq7LUyT03zcZs8\n" +
        "if/Hl4HHoIjjLRAbTRv0VwY5zrA5d0n/ixbMEZ7NEo0I1ugCti+9kbtIH8lmjnes\n" +
        "Ar3d6E+lz3e8tknNsVVbJy2v+AoU8oloo8ODhABGeOY8M+cmWC3jLURYWcxYlPgL\n" +
        "GvSvSeWs461EtJEH/IaVNr5yNd0yAMx9HNLV3niPZbmdTzBMNsbKvXPEgjT9rF2e\n" +
        "OlrSNihQ17Kqfed8oZ+it/PaVp/VTwct97YkIK4nchBTm2tpvOqfKSEE/cs1LQth\n" +
        "AwIDAQAB\n" +
        "-----END PUBLIC KEY-----";

    public void test(Context applicationContext){
        try {

            String sources="helloworldyukun";

            String byPublic = RSAUtil.encryptByPublic(applicationContext, sources);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
