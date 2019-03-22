package com.matrix.yukun.matrix.video_module;

import com.matrix.yukun.matrix.video_module.entity.UserInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import org.litepal.LitePalApplication;

import okhttp3.OkHttpClient;

/**
 * Created by yukun on 17-11-17.
 */

public class MyApplication extends LitePalApplication {
    public static MyApplication myApp;
    public static UserInfo userInfo;

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo user) {
        userInfo=new UserInfo();
        userInfo=user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static MyApplication getMyApp() {
        return myApp;
    }

}
