package com.matrix.yukun.matrix.gaia_module.net;

import android.util.Log;

import com.google.gson.JsonObject;
import com.matrix.yukun.matrix.gaia_module.bean.MaterialDetailInfo;
import com.matrix.yukun.matrix.gaia_module.bean.VideoDetailInfo;
import com.qq.e.comm.util.StringUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * author: kun .
 * date:   On 2019/7/13
 */
public class VideoUtils {
    private static VideoUtils mVideoUtils=new VideoUtils();
    private static int uid=8636;

    public static VideoUtils instance(){
        if(mVideoUtils==null){
            mVideoUtils=new VideoUtils();
        }
        return mVideoUtils;
    }

    public static void getWorkVideo(int wid,GetVideoListener getVideoListener){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid",uid);
            jsonObject.put("wid", wid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Api.BASE_URL+Api.WORKVIDEOURL)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                getVideoListener.getVideo(response);
            }

            @Override
            public void onDateError(String error) {
                getVideoListener.getError(error);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                getVideoListener.getError(e.toString());
            }
        });
    }

    public static void getVideoRecomend(String t, String key,GetVideoListener getVideoListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("t", t);
            if (key != null) {
                jsonObject.put("key", key);
            }
            jsonObject.put("pi", 1);
            jsonObject.put("ps", 20);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Api.BASE_URL+Api.WORKRECOND)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                getVideoListener.getVideo(data);
            }

            @Override
            public void onDateError(String error) {
                getVideoListener.getError(error);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                getVideoListener.getError(e.toString());
            }
        });
    }

    public static void getMaterialVideo(int mid,GetVideoListener getVideoListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("mid", mid);
            Log.d("MaterialDetail", "getDetail" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Api.BASE_URL+Api.MATERCIALVIDEOURL)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                getVideoListener.getVideo(response);
            }

            @Override
            public void onDateError(String error) {
                getVideoListener.getError(error);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                getVideoListener.getError(e.toString());
            }
        });
    }

    public static void getMaterialRecomend(int t,GetVideoListener getVideoListener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", t);
            jsonObject.put("pi", 1);
            jsonObject.put("ps", 20);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Api.BASE_URL+Api.MATRERIALRECOND)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new GaiCallBack() {
            @Override
            protected void onDataSuccess(String data, boolean a, boolean b, String response) {
                getVideoListener.getVideo(data);
            }

            @Override
            public void onDateError(String error) {
                getVideoListener.getError(error);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                getVideoListener.getError(e.toString());
            }
        });
    }


    public static String selectDefaultUri(VideoDetailInfo detailInfo) {
        String defaultPath = null;
        int have4k = detailInfo.getWorks().getHave4K();
        int have2k = detailInfo.getWorks().getHave2K();
        int have1080 = detailInfo.getWorks().getHave1080();
        int have720 = detailInfo.getWorks().getHave720();
        if (have720 == 1) {
            defaultPath = detailInfo.getResource().getP720();
        } else if (have1080 == 1) {
            defaultPath = detailInfo.getResource().getP1080();
        } else if (have2k == 1) {
            defaultPath = detailInfo.getResource().getK2();
        } else if (have4k == 1) {
            defaultPath = detailInfo.getResource().getK4();
        }
        if (defaultPath == null) {
            defaultPath = detailInfo.getResource().getMp4();
        } else {
            return defaultPath;
        }
        return defaultPath;
    }
    public static String selectDefaultUri(MaterialDetailInfo detailInfo) {
        String defaultPath = null;
        int have4k = detailInfo.getMaterial().getHave4K();
        int have2k = detailInfo.getMaterial().getHave2K();
        int have1080 = detailInfo.getMaterial().getHave1080();
        int have720 = detailInfo.getMaterial().getHave720();
        if (have720 == 1) {
            defaultPath = detailInfo.getResource().getP720();
        } else if (have1080 == 1) {
            defaultPath = detailInfo.getResource().getP1080();
        } else if (have2k == 1) {
            defaultPath = detailInfo.getResource().getK2();
        } else if (have4k == 1) {
            defaultPath = detailInfo.getResource().getK4();
        }
        if (defaultPath == null) {
            defaultPath = detailInfo.getResource().getMp4();
        } else {
            return defaultPath;
        }
        return defaultPath;
    }

    public interface GetVideoListener{
        void getVideo(String data);
        void getError(String error);
    }

}