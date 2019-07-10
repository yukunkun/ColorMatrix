package com.matrix.yukun.matrix.video_module.netutils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

/**
 * Created by yukun on 17-11-20.
 */

public class NetworkUtils {
//    public static String APPKEY="cef1e7fb4bc84616a716f52d8c99a23e";
    public static String APPKEY="263c66bfddc55a5a288201e1ef87ada1";
    public static GetBuilder networkGet(String url){
        GetBuilder build = OkHttpUtils.get().url(url);
        return build;
    }

    public static PostFormBuilder networkPost(String url){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(url);
        return postFormBuilder;
    }
}
