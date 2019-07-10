package com.matrix.yukun.matrix.gaia_module.net;

import com.matrix.yukun.matrix.util.log.LogUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public abstract class GaiCallBack extends StringCallback {
    @Override
    public void onResponse(String response, int id) {
        LogUtil.i(response.toString());
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(response.toString());
            int b = jsonObject.optInt("b");
            if(b==1){
                String data=null;
                if(jsonObject.has("a")){
                    data = jsonObject.optString("a");
                }else if (jsonObject.has("o")){
                    data = jsonObject.optString("o");
                }
                onDataSuccess(data,jsonObject.has("a"),jsonObject.has("b"),response);
            }else {
                JSONArray ec = jsonObject.optJSONArray("ec");
                onDateError(ec.toString());
            }
        } catch (JSONException e) {

        }
    }

    protected abstract void onDataSuccess(String data, boolean a, boolean b, String response);

    public abstract void onDateError(String error);

    @Override
    public void onError(Call call, Exception e, int id) {
        LogUtil.i(e.toString());
    }
}
