package com.matrix.yukun.matrix.bean;

import com.matrix.yukun.matrix.MyApp;

/**
 * Created by cretin on 15/12/30.
 */
public class AppConstants {
    /**
     * 指定在MyRelativeLayout中滑动的方向
     */
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_RIGHT = 7;
    public static final String PATH = "yukun";
    public static final String YINGYONGBAOPATH = "http://app.qq.com/#id=detail&appid=1105962710\n";
    //和风天气 1163459047@qq.com
    //密码 :123456789ykk
    public static final String HEWEATHER_KEY = "449354b040d04805a3737fbcdb3c1339";

    public static final String APPID="1105962710";
    public static final String BANNER_ADID="3040126180953688\n";
    //URLhttp://api.letvcloud.com/open.php?user_unique=abcde12345&timestamp=1347091285
    // &api=video.list&format=json&ver=2.0&index=1&size=50&sign=fb3bd36b3383691b9e4f55b1dd452133

    public static final String user_unique="cnsmvgmgt8";
    public static final String timestamp=getCurrentTime();
    public static final String format="json";
    public static final String ver="2.0";
    public static final String Secret_Key="ca1d537e27d5c7433741222b52133246";

    public static String getCurrentTime(){
        long l = System.currentTimeMillis();
        return l+"";
    }

}
