package com.matrix.yukun.matrix.leancloud_module.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * author: kun .
 * date:   On 2019/12/16
 */
public class GsonUtils {
    static Gson mGson=new Gson();

    public GsonUtils() {
    }

    public static <T>Object stringToBean(String s, Class<T> aClass){
        Object o = mGson.fromJson(s, aClass);
        return o;
    }

    public static <T> String toJson(String s, Class<T> aClass){
        String toJson = mGson.toJson(s, aClass);
        return toJson;
    }

    public static <T> List<T> toList(String value) {
        return (List)(new Gson()).fromJson(value, List.class);
    }

    public static <T> Object toObject(String value, Class<T> clazz) {
        return (new Gson()).fromJson(value, clazz);
    }

    public static <T> Object toObject(JsonElement value, Class<T> clazz) {
        return (new Gson()).fromJson(value, clazz);
    }



    public static <T> String mapToJson(Map<String, T> map) {
        return (new Gson()).toJson(map);
    }

    public static Map<String, Object> toMap(String json) {
        return (Map)(new Gson()).fromJson(json, (new TypeToken<Map<String, Object>>() {
        }).getType());
    }
}
