package com.matrix.yukun.matrix.weather_module.bean;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.R;

import java.util.ArrayList;


/**
 * Created by yukun on 17-3-9.
 */
public class CityPickerFragment extends DialogFragment {

    private static CityPickerFragment fragment;
//    private OptionsPickerView pvOptions;
    Context context;

    public static CityPickerFragment newInstance(){

        if(fragment==null){
            fragment = new CityPickerFragment();
        }
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View picker=inflater.inflate(R.layout.picker,null);
        init();
        return picker;
    }

    private void init() {
//        ArrayList<String> data = new ArrayList<String>();
//        String json = AssetsUtils.readText(this, "city2.json");
//        data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
//        AddressPicker picker = new AddressPicker(this, data);
//        picker.setHideProvince(true);
//        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
//            @Override
//            public void onAddressPicked(String province, String city, String county) {
//                showToast(province + city + county);
//            }
//        });
//        picker.show();

//        ChineseZodiacPicker picker = new ChineseZodiacPicker(getActivity());
//        picker.setLineVisible(false);
//        picker.setSelectedItem("羊");
//        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//            @Override
//            public void onOptionPicked(String option) {
//                MyApp.showToast(option);
//            }
//        });
//        picker.show();
    }
}
