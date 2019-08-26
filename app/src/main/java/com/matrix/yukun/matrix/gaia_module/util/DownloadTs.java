package com.matrix.yukun.matrix.gaia_module.util;

import android.text.TextUtils;
import android.util.Log;

import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;


/**
 * Created by yukun on 16-11-22.
 */
public class DownloadTs {

    public int load_num;
    private byte[] key_byte=null;
    public void startLoadCon(){
        IsHaveFile(); //判断是否存在文件
        startTime();  //计时器
    }

    public void setKey_byte(byte[] key_byte) {
        this.key_byte = key_byte;
    }

    public void setUri(ArrayList<String> strings, ArrayList<String> names, int load_num){
        this.load_num =load_num;
        loadtime=0;
        for (int i = 0; i < strings.size(); i++) {
            int i1 = strings.get(i).lastIndexOf("/");
            names.add(strings.get(i).substring(i1+1,strings.get(i).length()));
        }

        if(strings.size()%3==1){
            if(key_byte!=null){
                load_1(strings.get(0).trim(),names.get(0).trim(),key_byte);
            }else {
                getKey(ParaTs.getTsKey(),strings,names);
            }
        }
    }

    private void IsHaveFile(){
        File dir = new File(StringUtils.TSPATH);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    double loadtime=0;
    boolean threadCon=true;

    public void setThreadCon(boolean threadCon) {
        this.threadCon = threadCon;
    }
    public void startTime(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadCon){
                    try {
                        Thread.sleep(100);
                        loadtime= (loadtime+0.1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private synchronized void load_1(final String uri, final String name, final byte[] key) {
//        Log.i("---OkHttpUtils_Uri",uri);
        OkHttpUtils
                .getInstance()
                .get()
                .url(uri)
                .tag("")
                .build().execute(new FileCallBack(StringUtils.TSPATH,name) {

            @Override
            public void onError(Call call, Exception e, int id) {
                OkHttpUtils.getInstance().cancelTag("");
                Log.i("------error_OkHttpUtils",""+e);
            }

            @Override
            public void onResponse(File response, int id) {
                String pathTs= StringUtils.TSPATH+"/"+name;
                String iv = ParaTs.getTsIV();
                Log.i("-----key_iv",pathTs+" key:"+key.length+" ivs:"+ iv);
                char[] keyChars = Hex.encodeHex(key);
                String keyHexStr = String.valueOf(keyChars);
                String subIv = iv.substring(2).trim();
                AES aes=new AES();
                aes.setThreadCon(threadCon);
                aes.setLoad_num(load_num,loadtime);
                aes.cbc_pkcs5_128_decrypt(new File(pathTs), keyHexStr, subIv);
                Log.i("------load_time",loadtime+"");
            }
        });
    }

    private void  getKey(String uri, final ArrayList<String> strings, final ArrayList<String> names) {
        OkHttpUtils.get().url(uri).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if(!TextUtils.isEmpty(response)){
                    String[] aesParams = response.split("o");
                    String aes = aesParams[0];
                    String key = aesParams[1];
                    String iv = aesParams[2];
                    AESFORKEY aesCrypt = new AESFORKEY();
                    String aesData = aesCrypt.cbc_pkcs5_128_decrypt(aes, key, iv);
                    String[] byteStr = aesData.trim().split("-");
                    byte[] bytes=new byte[byteStr.length];
                    for (int i = 0; i < byteStr.length; i++) {
                        //int 转换成bytes[]
                        bytes[i]=Integer.valueOf(byteStr[i]).byteValue();
                    }
                    key_byte=bytes;
                    load_1(strings.get(0).trim(),names.get(0).trim(),bytes);
                }else {
                    ToastUtils.showToast("下载失败");
                }
            }
        });
    }

    public void stopOkHttpUtils(){
        OkHttpUtils.getInstance().cancelTag("");
    }

}
