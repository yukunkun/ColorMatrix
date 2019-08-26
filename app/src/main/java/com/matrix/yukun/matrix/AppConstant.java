package com.matrix.yukun.matrix;

import android.os.Environment;

/**
 * Created by cretin on 15/12/30.
 */
public class AppConstant {

    //https://www.apiopen.top/createUserKey?appId=com.matrix.yukun.matrix&passwd=123456
    //:{"appId":"com.matrix.yukun.matrix","appkey":"bde00694d5afdde94ff38bc52570cadb"}}

    public static final String AppId = "com.matrix.yukun.matrix";
    public static final String Appkey = "bde00694d5afdde94ff38bc52570cadb";
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_RIGHT = 7;
    public static final String PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/yukun";
    public static final String IMAGEPATH = PATH+"/matrix";
    public static final String GIFPATH = IMAGEPATH+"/gif_temp";
    public static final String GIFLOAD = PATH+"/gif";
    public static final String GIFVIDEO = PATH+"/gifvideo";
    public static final String VIDEOPATH = PATH+"/video";
    public static final String VOICEPATH = PATH+"/voice";
    public static final String NOTEPATH = PATH+"/note";
    public static final String HEWEATHER_KEY = "449354b040d04805a3737fbcdb3c1339"; //
    public static final String APP_STORE="https://android.myapp.com/myapp/detail.htm?apkName=com.matrix.yukun.matrix";
    public static final String APP_ICON_URl="https://upload-images.jianshu.io/upload_images/3001453-9261aeda99deaa4d.png";
    public static final String APPID="1105962710";
    public static final String BANNER_ADID="3040126180953688";
    public static final String format="json";
    public static final String JUHE_DIR_APPKEY="c3564ef1b3cccb2d3c9f06656e9a6e5d";
    public static final String JUHE_PHONE_APPKEY="e232241b95458275f5c9ffc0db90dccb";
    public static final String LOG = PATH+"/log";

}