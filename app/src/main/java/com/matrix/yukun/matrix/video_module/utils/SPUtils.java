package com.matrix.yukun.matrix.video_module.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.matrix.yukun.matrix.MyApp;

import java.util.Set;

/**
 * author: kun .
 * date:   On 2018/11/1
 */
public class SPUtils {

    private static SharedPreferences.Editor editor;
    private static SPUtils spUtils;
    private static SharedPreferences sharedPreferences;

    public static SPUtils getInstance(){
        if(spUtils==null){

            sharedPreferences = MyApp.myApp.getSharedPreferences("share", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            spUtils=new SPUtils();
        }
        return spUtils;
    }
    public void saveSet(String key,Set<String> value){
        editor.putStringSet(key,value);
        editor.apply();
    }

    public void saveString(String key,String value){
        editor.putString(key,value);
        editor.apply();
    }
    public void saveInt(String key,int value){
        editor.putInt(key,value);
    }

    public void saveLong(String key,long value){
        editor.putLong(key,value);
        editor.apply();
    }
    public void saveBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }

    public Set<String> getSet(String key){
        return sharedPreferences.getStringSet(key,null);
    }


    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }
    public int getInt(String key){
        return sharedPreferences.getInt(key,0);
    }
    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }
    public long getLong(String key){
        return sharedPreferences.getLong(key,0);
    }

    public void clearKey(String key){
        if(sharedPreferences.contains(key)){
            sharedPreferences.edit().remove(key);
        }
    }
}
