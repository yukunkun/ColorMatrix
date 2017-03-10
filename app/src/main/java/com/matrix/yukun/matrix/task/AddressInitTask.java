package com.matrix.yukun.matrix.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.matrix.yukun.matrix.weather_module.bean.EventCity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 获取地址数据并显示地址选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @version 2015/12/15
 */
public class AddressInitTask extends AsyncTask<String, Void, ArrayList<AddressPicker.Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideCounty=false;

    /**
     * 初始化为不显示区县的模式
     * @param activity
     * @param hideCounty   is hide County
     */
    public AddressInitTask(Activity activity, boolean hideCounty) {
        this.activity = activity;
        this.hideCounty=hideCounty;
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    public AddressInitTask(Activity activity) {
        this.activity = activity;
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }
    @Override
    protected ArrayList<AddressPicker.Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
        try {
            String json = AssetsUtils.readText(activity, "city.json");
            JsonParser jsonParser = new JsonParser();
            JsonElement parse = jsonParser.parse(json);
            if(parse.isJsonArray()) {
                JsonArray asJsonArray = parse.getAsJsonArray();
                Iterator<JsonElement> iterator = asJsonArray.iterator();
                while (iterator.hasNext()) {
                    JsonElement next = iterator.next();
                    AddressPicker.Province bean = new Gson().fromJson(next,AddressPicker.Province.class);
                    data.add(bean);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<AddressPicker.Province> result) {
        dialog.dismiss();
        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker(activity, result);
            picker.setHideCounty(hideCounty);
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(String province, String city, String county) {
                    if (city!=null){
                        EventBus.getDefault().post(new EventCity(city));
                    }
                }
            });
            picker.show();
        } else {
            Toast.makeText(activity, "数据初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

}
